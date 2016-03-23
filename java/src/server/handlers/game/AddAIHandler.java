package server.handlers.game;

import static server.utils.Strings.BAD_JSON_MESSAGE;
import shared.dto.AddAIDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public final class AddAIHandler implements Route {
    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!AddAIDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        // TODO
        response.status(200);
        response.type("application/json");
        return "{\"Hello\": \"World!\"}";
    }
}
