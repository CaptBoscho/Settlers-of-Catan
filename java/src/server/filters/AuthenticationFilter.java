package server.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * @author Derek Argueta
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        // -- TODO check user credentials
        halt(401, "YOU SHALL NOT PASS");
    }
}
