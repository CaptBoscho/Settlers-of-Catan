package shared.model.game;

import client.facade.Facade;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

    public void addMessage(MessageLine m) {
        assert m != null;
        assert m.getMessage() != null;
        assert m.getMessage().length() > 0;

        chat.add(m);
    }

    private void makeMessageLog(JsonArray jarray) {
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
        return null;
    }

}
