package model.map;

import org.junit.Test;
import shared.locations.HexLocation;
import shared.model.map.Robber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class RobberTests {

    @Test
    public void testDefaultConstructor() {
        Robber juan = new Robber();
        assertEquals(juan.getLocation().getX(), 0);
        assertEquals(juan.getLocation().getY(), 0);
    }

    @Test
    public void testLocationConstructor() {
        Robber carlos = new Robber(new HexLocation(1, 2));
        assertEquals(carlos.getLocation().getX(), 1);
        assertEquals(carlos.getLocation().getY(), 2);

        try {
            new Robber(null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetLocation() {
        Robber pablo = new Robber();
        pablo.setLocation(new HexLocation(1, 2));
        assertEquals(pablo.getLocation().getX(), 1);
        assertEquals(pablo.getLocation().getY(), 2);

        pablo.setLocation(new HexLocation(-1, -1));
        assertEquals(pablo.getLocation().getX(), -1);
        assertEquals(pablo.getLocation().getY(), -1);

        try {
            pablo.setLocation(null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
}
