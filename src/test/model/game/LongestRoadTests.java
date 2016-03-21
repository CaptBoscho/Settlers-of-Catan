package model.game;

import org.junit.Test;
import shared.model.game.LongestRoad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class LongestRoadTests {

    /**
     * Verify the default setup of the LongestRoad object
     */
    @Test
    public void testDefaultConstructor() {
        LongestRoad lr = new LongestRoad();
        assertEquals(lr.getOwner(), -1);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 0);
    }

    @Test
    public void testPlayerIdConstructor() {
        LongestRoad lr = new LongestRoad(0);
        assertEquals(lr.getOwner(), 0);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 0);

        try {
            new LongestRoad(-5);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new LongestRoad(10);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetOwner() {
        LongestRoad lr = new LongestRoad(2);
        lr.setOwner(3, 10);

        assertEquals(lr.getOwner(), 3);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 10);

        try {
            lr.setOwner(-5, 11);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            lr.setOwner(5, 11);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            lr.setOwner(3, 11);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            lr.setOwner(2, 4);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        lr.setOwner(1, 14);

        assertEquals(lr.getOwner(), 1);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 14);
    }

    @Test
    public void testUpdateRoadSize() {
        LongestRoad lr = new LongestRoad(2);
        lr.updateRoadSize(4);

        assertEquals(lr.getOwner(), 2);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 4);

        try {
            lr.updateRoadSize(-4);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            lr.updateRoadSize(4);
            assertTrue(false);  // should never reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }

        lr.updateRoadSize(5);

        assertEquals(lr.getOwner(), 2);
        assertEquals(lr.getPointWorth(), 2);
        assertEquals(lr.getSize(), 5);
    }
}
