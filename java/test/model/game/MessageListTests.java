package model.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;

import static org.junit.Assert.*;

/**
 * @author Derek Argueta
 */
public class MessageListTests {

    @Test
    public void testMessageLogComposition() {
        MessageList list = new MessageList();
        try {
            list.addMessage(null);
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }

        list.addMessage(new MessageLine("Derek", "aloha"));

        assertEquals(1, list.getMessages().size());
    }

    @Test
    public void testJsonComposition() {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        for(int i = 0; i < 10; i++) {
            JsonObject tmp = new JsonObject();
            tmp.addProperty("source", "Derek");
            tmp.addProperty("message", "lol");
            arr.add(tmp);
        }
        obj.add("lines", arr);
        MessageList list = new MessageList(obj);
        assertEquals(10, list.getMessages().size());
    }
}
