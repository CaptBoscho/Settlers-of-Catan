package shared.definitions;

/**
 * All API endpoints should be registered here. By using variables instead of
 * raw Strings, the odds of typos and bugs are dramatically reduced.
 *
 * @author Derek Argueta
 */
public class Endpoints {
    public static final String LOGIN_ENDPOINT = "/user/login";
    public static final String REGISTER_ENDPOINT = "/user/register";

    public static final String LIST_GAMES_ENDPOINT = "/games/list";
    public static final String CREATE_GAME_ENDPOINT = "/games/create";
    public static final String JOIN_GAME_ENDPOINT = "/games/join";

    public static final String GAME_MODEL_ENDPOINT = "/game/model";
    public static final String ADD_AI_ENDPOINT = "/game/addAI";
    public static final String LIST_AI_ENDPOINT = "/game/listAI";
    public static final String SAVE_GAME_ENDPOINT = "/games/save";
    public static final String LOAD_GAME_ENDPOINT = "/games/load";

    public static final String SEND_CHAT_ENDPOINT = "/moves/sendChat";
    public static final String ROLL_NUMBER_ENDPOINT = "/moves/rollNumber";
    public static final String ROB_PLAYER_ENDPOINT = "/moves/robPlayer";
    public static final String FINISH_TURN_ENDPOINT = "/moves/finishTurn";
    public static final String BUY_DEV_CARD_ENDPOINT = "/moves/buyDevCard";
    public static final String YEAR_OF_PLENTY_ENDPOINT = "/moves/Year_of_Plenty";
    public static final String ROAD_BUILDING_ENDPOINT = "/moves/Road_Building";
    public static final String SOLDIER_ENDPOINT = "/moves/Soldier";
    public static final String MONOPOLY_ENDPOINT = "/moves/Monopoly";
    public static final String MONUMENT_ENDPOINT = "/moves/Monument";
    public static final String BUILD_ROAD_ENDPOINT = "/moves/buildRoad";
    public static final String BUILD_SETTLEMENT_ENDPOINT = "/moves/buildSettlement";
    public static final String BUILD_CITY_ENDPOINT = "/moves/buildCity";
    public static final String OFFER_TRADE_ENDPOINT = "/moves/offerTrade";
    public static final String ACCEPT_TRADE_ENDPOINT = "/moves/acceptTrade";
    public static final String MARITIME_TRADE_ENDPOINT = "/moves/maritimeTrade";
    public static final String DISCARD_CARDS_ENDPOINT = "/moves/discardCards";

    public static final String RESET_ENDPOINT = "/test/reset";

}
