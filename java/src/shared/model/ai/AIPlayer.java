package shared.model.ai;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.exceptions.InvalidPlayerException;
import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.player.PlayerType;

/**
 * Created by Kyle 'TMD' Cornelison on 3/19/2016.
 */
abstract public class AIPlayer extends Player {

    private AIType aiType;

    public AIPlayer() {
        super();
    }
    
    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public AIPlayer(JsonObject json) {
        super(json);
        // TODO: 3/19/2016 Figure this out...
    }

    /**
     * New Player Constructor
     *
     * @param points      Initial points
     * @param color       Player Color
     * @param id          Player ID
     * @param playerIndex The index of the player in a particular game (0-3)
     * @param name        Player Name
     * @param type
     */
    public AIPlayer(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name);
        super.setPlayerType(PlayerType.AI);
        this.aiType = type;
    }

    public AIType getAIType() {
        return aiType;
    }

    abstract public void setUpOne(Game game);

    abstract public void setUpTwo(Game game);

    abstract public void rolling(Game game);

    abstract public void discarding(Game game);

    abstract public void robbing(Game game);

    abstract public void playing(Game game);

    abstract public void acceptTrade(Game game);

    abstract public void setTrading(boolean isTrading);

    abstract public boolean isTrading();
}
