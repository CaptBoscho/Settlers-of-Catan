package model.locations;

import com.google.gson.JsonObject;
import org.junit.Test;
import shared.locations.HexLocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class HexLocationTests {

    @Test
    public void testDeserialization() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("x", 2);
        obj.addProperty("y", 2);

        final HexLocation loc = new HexLocation(obj);
        assertEquals(loc.getX(), 2);
        assertEquals(loc.getY(), 4);
    }

    /**
     * Test deserializing a JSON object without anything in it.
     */
    @Test
    public void testBadSerializationOne() {
        final JsonObject obj = new JsonObject();
        try {
            new HexLocation(obj);
            assertTrue(false); // shouldn't reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    /**
     * Test deserializing a JSON object without an x value
     */
    @Test
    public void testBadSerializationTwo() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("y", 5);
        try {
            new HexLocation(obj);
            assertTrue(false); // shouldn't reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    /**
     * Test deserializing a JSON object without a y value
     */
    @Test
    public void testBadSerializationThree() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("x", 3);
        try {
            new HexLocation(obj);
            assertTrue(false); // shouldn't reach here
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
}
