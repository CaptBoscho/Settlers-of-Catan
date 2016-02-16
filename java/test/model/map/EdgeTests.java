package model.map;

import org.junit.Test;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.map.Edge;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class EdgeTests {

    /**
     * Test non-null data-validation assertions
     *
     * You shouldn't be able to create an Edge with a null EdgeLocation nor set the road to null
     */
    @Test
    public void testNullAssertions() {
        try {
            new Edge(null);
            assertTrue(false); // shouldn't reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        final Edge edge = new Edge(new EdgeLocation(new HexLocation(2, 2), EdgeDirection.North));
        try {
            edge.setRoad(null);
            assertTrue(false); // shouldn't reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    /**
     * Validate setup of an Edge. The road should be null to start with
     */
    @Test
    public void testInitialState() {
        final EdgeLocation edgeLoc = new EdgeLocation(new HexLocation(2, 2), EdgeDirection.North);
        final Edge edge = new Edge(edgeLoc);
        assertFalse(edge.hasRoad());
        assertEquals(edge.getRoad(), null);
        assertEquals(edge.getEdgeLoc(), edgeLoc);
    }
}
