package server.utils;

/**
 * @author Derek Argueta
 */
public class Strings {

    // -- cookie keys
    public static final String CATAN_USER_COOKIE_KEY = "catan.user";

    // -- command object names
    public static final String GAME_MODEL_COMMAND = "model";
    public static final String GAME_LIST_AI_COMMAND = "listAI";
    public static final String GAME_ADD_AI_COMMAND = "addAI";
    public static final String GAMES_LIST_COMMAND = "list";
    public static final String GAMES_JOIN_COMMAND = "join";
    public static final String GAMES_CREATE_COMMAND = "create";

    // -- misc error messages
    public static final String BAD_COMMAND_NAME_MSG = "no matching command found";

    public static final String BAD_JSON_MESSAGE = "Invalid request.";
}
