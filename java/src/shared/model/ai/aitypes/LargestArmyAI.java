package shared.model.ai.aitypes;

import shared.definitions.CatanColor;
import shared.exceptions.InvalidPlayerException;
import shared.model.ai.AIPlayer;
import shared.model.ai.AIType;
import shared.model.game.Game;

/**
 * @author Joel Bradley
 */
public class LargestArmyAI extends AIPlayer {

    public LargestArmyAI(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {

    }

    @Override
    public void acceptTrade(Game game) {

    }

    @Override
    public void setUpOne(Game game) {

    }

    @Override
    public void setUpTwo(Game game) {

    }

    @Override
    public void rolling(Game game) {

    }

    @Override
    public void discarding(Game game) {

    }

    @Override
    public void robbing(Game game) {

    }

    @Override
    public void playing(Game game) {

    }

    @Override
    public void setTrading(boolean isTrading) {

    }

    @Override
    public boolean isTrading() {
        return false;
    }
}
