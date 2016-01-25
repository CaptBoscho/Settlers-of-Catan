package shared.model.bank;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.devcards.DevelopmentCard;
import shared.model.game.Game;
import shared.model.player.Player;

import java.util.ArrayList;


/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public class DevelopmentCardBank implements JsonSerializable {
    static final int SOLDIER_CARDS = 14;
    static final int MONUMENT_CARDS = 5;
    static final int MONOPOLY_CARDS = 2;
    static final int YEAR_OF_PLENTY_CARDS = 2;
    static final int ROAD_BUILD_CARDS = 2;

    private int numberSoldierCards;

    private ArrayList<DevelopmentCard> developmentCards;

    /**
     * Creates a full DevelopmentCardBank
     * @param game The object that contains the DevelopmentCardBank
     */
    public DevelopmentCardBank(Game game) {

    }

    /**
     * Construct a DevelopmentCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public DevelopmentCardBank(JsonObject json) {

    }

    /**
     * Creates an empty DevelopmentCardBank
     * @param player The object that contains the DevelopmentCardBank
     */
    public DevelopmentCardBank(Player player) {

    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
