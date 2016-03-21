package shared.dto;

import com.google.gson.JsonObject;

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

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
