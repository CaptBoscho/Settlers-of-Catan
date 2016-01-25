package shared.model.bank;

import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.resources.ResourceCard;

import java.util.ArrayList;

/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
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
    public ResourceCardBank(Game game) {

    }

    /**
     * Creates an empty ResourceCardBank
     * @param player The object that contains the ResourceCardBank
     */
    public ResourceCardBank (Player player) {

    }
}
