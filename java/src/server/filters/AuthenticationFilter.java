package server.filters;

import server.main.Config;
import static server.utils.Strings.CATAN_USER_COOKIE_KEY;
import shared.dto.CookieWrapperDTO;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Adds authentication when wrapped around an HTTP handler
 * TODO - instead of abusing the CookieWrapperDTO, should instead make a CookieExtractor
 *
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#filters
 */
public final class AuthenticationFilter implements Filter {
    @Override
    public void handle(final Request request, final Response response) throws Exception {
        if(!request.cookies().containsKey(CATAN_USER_COOKIE_KEY)) {
            halt(401, "YOU SHALL NOT PASS");
        }
        final CookieWrapperDTO dto = new CookieWrapperDTO(null);
        dto.extractCookieInfo(request.cookies());
        final String username = dto.getUsername();
        final String password = dto.getPassword();
        boolean cookieMissingUser = !request.cookies().containsKey(CATAN_USER_COOKIE_KEY);
        boolean userAuthenticated = Config.facade.login(username, password);
        if(cookieMissingUser || !userAuthenticated) {
            halt(401, "YOU SHALL NOT PASS");
        }
    }
}
