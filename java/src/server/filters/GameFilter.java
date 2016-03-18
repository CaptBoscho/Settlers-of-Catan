package server.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * @author Derek Argueta
 */
public class GameFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        // -- TODO check that the game cookie is valid and user actually belongs to game
    }
}
