package shared.bank;

import shared.resources.ResourceCard;

import java.util.ArrayList;

/**
 * Created by Danny on 1/18/16.
 */
public class ResourceCardBank {
    static final int NUMBER_BRICK = 15;
    static final int NUMBER_ORE = 15;
    static final int NUMBER_SHEEP = 15;
    static final int NUMBER_WHEAT = 15;
    static final int NUMBER_WOOD = 15;

    private ArrayList<ResourceCard> resourceCards;

    ResourceCardBank(String parent) {
        if (parent.equals("game")) {
            // create full bank
        } else if (parent.equals("user")) {
            // create empty bank
        }
    }
}
