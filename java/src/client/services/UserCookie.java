package client.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Derek Argueta
 */
public final class UserCookie {

    private static UserCookie instance;
    private JsonObject userCookie;
    private JsonObject gameCookie;
    private static final String TEXT_ENCODING = "UTF-8";

    protected UserCookie() { }

    public static UserCookie getInstance() {
        if(instance == null) {
            instance = new UserCookie();
            instance.userCookie = new JsonObject();
            instance.gameCookie = new JsonObject();
        }

        return instance;
    }

    public String getCompleteCookieValue() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder("catan.user=" + URLEncoder.encode(this.userCookie.toString(), TEXT_ENCODING));
        if(this.gameCookie.entrySet().size() > 0) {
            builder.append("; catan.game=").append(URLEncoder.encode(this.gameCookie.toString(), TEXT_ENCODING));
        }
        return builder.toString();
    }

    public boolean hasContent() {
        return this.userCookie.entrySet().size() > 0 || this.gameCookie.entrySet().size() > 0;
    }

    public void clearCookies() {
        this.userCookie = new JsonObject();
        this.gameCookie = new JsonObject();
    }

    private void setCatanGameCookieValue(String catanGameCookieValue) throws UnsupportedEncodingException {
        assert catanGameCookieValue != null;
        assert catanGameCookieValue.length() > 0;

        final String decodedCookie = URLDecoder.decode(catanGameCookieValue, TEXT_ENCODING);
        this.gameCookie = new JsonParser().parse(decodedCookie).getAsJsonObject();
    }

    private void setCatanUserCookieValue(String catanUserCookieValue) throws UnsupportedEncodingException {
        assert catanUserCookieValue != null;
        assert catanUserCookieValue.length() > 0;

        final String decodedCookie = URLDecoder.decode(catanUserCookieValue, TEXT_ENCODING);
        this.userCookie = new JsonParser().parse(decodedCookie).getAsJsonObject();
    }

    public void setCookies(String cookieValue) throws UnsupportedEncodingException {
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
}
