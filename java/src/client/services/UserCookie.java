package client.services;

import client.data.PlayerInfo;
import client.facade.Facade;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shared.definitions.CatanColor;

import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Derek Argueta
 */
public final class UserCookie {

    private static UserCookie instance;

    private PlayerInfo playerInfo;
    private JsonObject userCookie;
    private String gameCookie;
    private static final String TEXT_ENCODING = "UTF-8";

    protected UserCookie() { }

    public static UserCookie getInstance() {
        if(instance == null) {
            instance = new UserCookie();
            instance.userCookie = new JsonObject();
            instance.gameCookie = "";
            instance.playerInfo = new PlayerInfo();
        }

        return instance;
    }

    public String getCompleteCookieValue() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder("catan.user=" + URLEncoder.encode(this.userCookie.toString(), TEXT_ENCODING));
        if(this.gameCookie.length() > 0) {
            builder.append("; catan.game=").append(URLEncoder.encode(this.gameCookie, TEXT_ENCODING));
        }
        return builder.toString();
    }

    public boolean hasContent() {
        return this.userCookie.entrySet().size() > 0 || this.gameCookie.length() > 0;
    }

    public void clearCookies() {
        this.userCookie = new JsonObject();
        this.gameCookie = "";
    }

    private void setCatanGameCookieValue(final String catanGameCookieValue) throws UnsupportedEncodingException {
        assert catanGameCookieValue != null;
        assert catanGameCookieValue.length() > 0;

        this.gameCookie = catanGameCookieValue;
    }

    private void setCatanUserCookieValue(final String catanUserCookieValue) throws UnsupportedEncodingException {
        assert catanUserCookieValue != null;
        assert catanUserCookieValue.length() > 0;

        final String decodedCookie = URLDecoder.decode(catanUserCookieValue, TEXT_ENCODING);
        this.userCookie = new JsonParser().parse(decodedCookie).getAsJsonObject();
    }

    public void setCookies(final String cookieValue) throws UnsupportedEncodingException {
        final String[] cookies = cookieValue.split(";");
        for(final String cookie : cookies) {
            if(cookie.startsWith("catan.user")) {
                this.setCatanUserCookieValue(cookie.substring(cookie.indexOf("=") + 1));
            } else if(cookie.startsWith("catan.game")) {
                this.setCatanGameCookieValue(cookie.substring(cookie.indexOf("=") + 1));
            }
        }
    }

    public String getUsername() {
        return this.userCookie.get("name").toString().replaceAll("\"", "");
    }

    public int getPlayerId() {
        return this.userCookie.get("playerID").getAsInt();
    }

    public int getPlayerIndex() {
        try {
            return Facade.getInstance().getGame().getPlayerById(this.getPlayerId()).getPlayerIndex();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void setName(String loginUsername) {
        this.playerInfo.setName(loginUsername);
    }

    public void setId(int id) {
        this.playerInfo.setId(id);
    }

    public CatanColor getColor() {
        return this.playerInfo.getColor();
    }

    public PlayerInfo getPlayerInfo() {
        return this.playerInfo;
    }
}
