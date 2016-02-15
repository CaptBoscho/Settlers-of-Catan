package shared.model.game;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Corbin Byers
 */
public final class MessageList {

    private List<MessageLine> chat = new ArrayList<>();

    public void addMessage(MessageLine m){
        assert m != null;
        assert m.getMessage() != null;
        assert m.getMessage().length() > 0;

        chat.add(m);
    }

    public void loadJSON(JsonObject js) {
        assert js != null;
       // HashMap<String,Object> result = new ObjectMapper().readValue(js, HashMap.class);
        chat = new ArrayList<>();

        Gson gs = new Gson();
        makeMessageLog(gs.fromJson(js.getAsJsonArray("lines"), JsonArray.class));
    }

    private void makeMessageLog(JsonArray jarray) {
        assert jarray != null;

        Gson gs = new Gson();
        for (JsonElement je: jarray){
            JsonObject json = je.getAsJsonObject();
            this.chat.add(new MessageLine(json));
        }
    }

    public List<MessageLine> getMessages() {
        return this.chat;
    }
}
