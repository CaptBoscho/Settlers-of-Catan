package server.filters;

import server.main.Config;
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
public class AuthenticationFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        CookieWrapperDTO dto = new CookieWrapperDTO(null);
        dto.extractCookieInfo(request.cookies());
        if(!request.cookies().containsKey("catan.user") || !Config.facade.login(dto.getUsername(), dto.getPassword())) {
            halt(401, "YOU SHALL NOT PASS");
        }
    }
}
