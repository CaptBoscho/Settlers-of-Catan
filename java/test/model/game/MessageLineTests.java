package model.game;

import com.google.gson.JsonObject;
import org.junit.Ignore;
import org.junit.Test;
import shared.model.game.MessageLine;

import static org.junit.Assert.*;

/**
 * @author Derek Argueta
 */
public class MessageLineTests {

    @Test
    public void testMessageLineComposition() {
        try {
            new MessageLine(null, null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new MessageLine("", null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new MessageLine(null, "");
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            new MessageLine("", "");
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            MessageLine msg = new MessageLine("Derek", "aloha");
            assertEquals("Derek", msg.getPlayer());
            assertEquals("aloha", msg.getMessage());
        } catch (AssertionError e) {
            assertTrue(false);
        }
    }

    @Test
    @Ignore
    public void testJsonComposition() {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("message","this is a message");
            obj.addProperty("source", "corbin");
            new MessageLine(obj);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("message", "hi");
            new MessageLine(obj);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("source", "Derek");
            new MessageLine(obj);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("source", "Derek");
            obj.addProperty("message", "hi");
            MessageLine msg = new MessageLine(obj);
            assertEquals("Derek", msg.getPlayer());
            assertEquals("hi", msg.getMessage());
        } catch (AssertionError e) {
            assertTrue(false);
        }
    }
}
