package shared.bank;

import shared.model.Game;
import shared.model.Player;
import shared.resources.ResourceCard;

import java.util.ArrayList;

/**
 * A bank owned by either a Player or a Game which holds all the owners DevelopmentCards
 *
 * Created by Danny on 1/18/16.
 */
public class ResourceCardBank {
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
    ResourceCardBank(Game game) {

    }

    /**
     * Creates an empty ResourceCardBank
     * @param player The object that contains the ResourceCardBank
     */
    ResourceCardBank(Player player) {

    }
}
