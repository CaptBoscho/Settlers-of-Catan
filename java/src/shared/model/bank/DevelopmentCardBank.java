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
     * Creates an empty DevelopmentCardBank
     * @param player The object that contains the DevelopmentCardBank
     */
    public DevelopmentCardBank(Player player) {

    }

    /**
     * Construct a DevelopmentCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public DevelopmentCardBank(JsonObject json) {

    }

    /**
     * Adds a DevelopmentCard to the bank.
     *
     * @pre none
     * @post developmentCards.length() == old.length() + 1
     * @post cardToAdd is now in developmentCards
     *
     * @param cardToAdd Development card to add to the bank
     */
    public void addDevCard(DevelopmentCard cardToAdd) {
        developmentCards.add(cardToAdd);
    }

    /**
     * @return the number of developmentCards in the bank
     */
    public int size() {
        return developmentCards.size();
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
