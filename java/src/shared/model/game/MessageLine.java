package shared.model.game;

/**
 * Created by boscho on 2/3/16.
 */
public class MessageLine {

    private String source;
    private String content;

    public MessageLine(String person, String c){
        source = person;
        content = c;
    }

    public String getPlayer(){
        return source;
    }

    public String getMessage(){
        return content;
    }
}
