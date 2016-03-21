package model.map;

import org.junit.Test;
import shared.definitions.PortType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.map.Port;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class PortTests {

    @Test
    public void testPortConstruction() {
        Port p = new Port(PortType.THREE, new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
        assertEquals(PortType.THREE, p.getPortType());
    }

    @Test
    public void testBadPortConstruction() {
        try {
            new Port(null, null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new Port(PortType.THREE, null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new Port(null, new VertexLocation(new HexLocation(2, 2), VertexDirection.SouthEast));
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
}
