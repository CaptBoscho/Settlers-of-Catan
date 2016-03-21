package server.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Adds authentication when wrapped around an HTTP handler
 *
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#filters
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        // -- TODO check user credentials
        halt(401, "YOU SHALL NOT PASS");
    }
}
