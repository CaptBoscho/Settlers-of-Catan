package shared.model.game;

import com.google.gson.JsonObject;

/**
 * @author Corbin Byers
 */
public class MessageLine {

    private String source;
    private String content;

    public MessageLine(String person, String c) {
        assert person != null;
        assert person.length() > 0;
        assert c != null;
        assert c.length() > 0;

        source = person;
        content = c;
    }

    public MessageLine(JsonObject jo){
        assert jo != null;
        assert jo.has("message");
        assert jo.has("source");

        content = jo.get("message").getAsString();
        source = jo.get("source").getAsString();
    }

    public String getPlayer(){
        return source;
    }

    public String getMessage(){
        return content;
    }
}
