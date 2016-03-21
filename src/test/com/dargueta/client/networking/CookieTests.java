package client.networking;

import client.services.UserCookie;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Derek Argueta
 */
public class CookieTests {

    private static final String TEST_COOKIE = "catan.user=%7B%22name%22%3A%22Sam%22%2C%22password%22%3A%22sam%22%2C%22playerID%22%3A0%7D";

    @Test
    public void testSetUserCookie() throws UnsupportedEncodingException {
        UserCookie.getInstance().setCookies(TEST_COOKIE + ";Path=/;");
        assertEquals("Sam", UserCookie.getInstance().getUsername());
        assertEquals(0, UserCookie.getInstance().getPlayerId());
    }

    @Test
    public void testEmptyCookie() {
        UserCookie.getInstance().clearCookies();
        assertFalse(UserCookie.getInstance().hasContent());
    }

    @Test
    public void testGetCookie() throws UnsupportedEncodingException {
        UserCookie.getInstance().setCookies(TEST_COOKIE + ";Path=/;");
        assertEquals(TEST_COOKIE, UserCookie.getInstance().getCompleteCookieValue());
    }
}
