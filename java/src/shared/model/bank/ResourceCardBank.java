package shared.model.bank;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.resources.ResourceCard;

import java.util.ArrayList;

/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public class ResourceCardBank implements JsonSerializable {
    static final int MAX_NUMBER_BRICK = 15;
    static final int MAX_NUMBER_ORE = 15;
    static final int MAX_NUMBER_SHEEP = 15;
    static final int MAX_NUMBER_WHEAT = 15;
    static final int MAX_NUMBER_WOOD = 15;

    private ArrayList<ResourceCard> resourceCards;

    /**
     * Creates a full ResourceCardBank
     * @param game The object that contains the DevelopmentCardBank
     */
    public ResourceCardBank(Game game) {

    }

    /**
     * Construct a ResourceCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public ResourceCardBank(JsonObject json) {

    }

    /**
     * Creates an empty ResourceCardBank
     * @param player The object that contains the ResourceCardBank
     */
    public ResourceCardBank (Player player) {

    }

    /**
     * Adds a ResourceCard to the ResourceCardBank
     * @param cardToAdd ResourceCard to add
     */
    public void addResource(ResourceCard cardToAdd) {
        resourceCards.add(cardToAdd);
    }

    /**
     * @return number of cards in the ResourceCardBank
     */
    public int size() {
        return resourceCards.size();
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
