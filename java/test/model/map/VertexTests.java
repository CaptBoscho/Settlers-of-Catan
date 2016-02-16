package model.map;

import org.junit.Test;
import shared.definitions.PortType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.map.Port;
import shared.model.map.Vertex;
import shared.model.structures.City;
import shared.model.structures.Settlement;

import static org.junit.Assert.*;

/**
 * @author Derek Argueta
 */
public class VertexTests {

    @Test
    public void testDefaultSetup() {
        Vertex v = new Vertex(new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));

        // this tile should have nothing
        assertFalse(v.hasBuilding());
        assertFalse(v.hasCity());
        assertFalse(v.hasPort());
        assertFalse(v.hasSettlement());

        assertNull(v.getCity());
        assertNull(v.getPort());
        assertNull(v.getSettlement());
        assertEquals(-1, v.getPlayerID());

        // since it's empty, it should be buildable
        assertTrue(v.canBuildSettlement());

        // but cities require a settlement to be there first
        assertFalse(v.canBuildCity());
    }

    @Test
    public void testBuildingSettlement() {
        Vertex v = new Vertex(new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
        v.buildSettlement(new Settlement(3));

        assertEquals(3, v.getPlayerID());

        assertTrue(v.hasSettlement());
        assertFalse(v.canBuildSettlement());
        assertTrue(v.canBuildCity());

        assertNotNull(v.getSettlement());
        assertNull(v.getCity());
    }

    @Test
    public void testBuildingCity() {
        Vertex v = new Vertex(new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
        v.buildSettlement(new Settlement(3));
        v.buildCity(new City(3));

        assertEquals(3, v.getPlayerID());

        // should switch from settlement to city
        assertFalse(v.hasSettlement());
        assertTrue(v.hasCity());

        // maxed out on building
        assertFalse(v.canBuildSettlement());
        assertFalse(v.canBuildCity());

        // settlement no-longer exists
        assertNull(v.getSettlement());
        assertNotNull(v.getCity());
    }

    @Test
    public void testPlayerBuildingCityIsSameAsSettlement() {
        Vertex v = new Vertex(new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
        v.buildSettlement(new Settlement(3));

        try {
            v.buildCity(new City(1)); // not the same player who made the settlement
            assertTrue(false); // should not reach this line
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    // TODO - port constructor is sloppy. Why does it also require a vertex location?
    @Test
    public void testPorts() {
        Vertex v = new Vertex(new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
        v.setPort(new Port(PortType.THREE, new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast)));

        assertEquals(-1, v.getPlayerID()); // vertex is owned by no-one
        assertTrue(v.hasPort());
        assertNotNull(v.getPort());
    }
}
