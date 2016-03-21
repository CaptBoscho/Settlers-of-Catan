package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * This class contains another IDTO, but has additional fields for containing cookie values
 *
 * @author Derek Argueta
 */
public class CookieWrapperDTO implements IDTO {

    private IDTO dto;
    private String username;
    private String password;
    private int playerId;
    private int gameId;

    public CookieWrapperDTO(final IDTO dto) {
        this.dto = dto;
    }

    //// literally THE dumbest way to do cookies. terribad design 340 peeps
    // like seriously, who stores a password as PLAIN TEXT in a cookie. cmon.
    public void extractCookieInfo(Map<String, String> cookies) {
        if(cookies.containsKey("catan.user")) {
            try {
                final String decodedCookie = URLDecoder.decode(cookies.get("catan.user"), "UTF-8");
                JsonObject crappyCookieDesign = new JsonParser().parse(decodedCookie).getAsJsonObject();
                this.username = crappyCookieDesign.get("name").getAsString();
                this.password = crappyCookieDesign.get("password").getAsString();
                this.playerId = crappyCookieDesign.get("playerID").getAsInt();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(cookies.containsKey("catan.game")) {
            this.gameId = Integer.parseInt(cookies.get("catan.game"));
        }
    }

    public IDTO getDto() {
        return this.dto;
    }

    public void setPlayerId(final int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public int getGameId() {
        return this.gameId;
    }

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
