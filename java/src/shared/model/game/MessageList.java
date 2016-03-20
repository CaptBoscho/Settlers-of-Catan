package shared.model.game;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Corbin Byers
 */
public final class MessageList implements JsonSerializable {

    private List<MessageLine> chat;

    public MessageList() {
        chat = new ArrayList<>();
    }

    public MessageList(final JsonObject js) {
        assert js != null;
        chat = new ArrayList<>();
        Gson gs = new Gson();
        makeMessageLog(gs.fromJson(js.getAsJsonArray("lines"), JsonArray.class));
    }

    public void addMessage(final MessageLine m) {
        assert m != null;
        assert m.getMessage() != null;
        assert m.getMessage().length() > 0;

        chat.add(m);
    }

    private void makeMessageLog(final JsonArray jarray) {
        assert jarray != null;

        for (JsonElement je: jarray){
            JsonObject json = je.getAsJsonObject();
            this.chat.add(new MessageLine(json));
        }
    }

    public List<MessageLine> getMessages() {
        return this.chat;
    }

    @Override
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        final JsonArray array = new JsonArray();
        for(MessageLine line : chat){
            final JsonObject item = new JsonObject();
            item.addProperty("message",line.getMessage());
            item.addProperty("source",line.getPlayer());
            array.add(item);
        }
        json.add("lines",array);
        return json;
    }

}
