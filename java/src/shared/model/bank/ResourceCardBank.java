package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;
import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.resources.*;

import java.util.ArrayList;
import java.util.Objects;

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

    private final Object OWNER;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<Ore> ores = new ArrayList<>();
    private ArrayList<Sheep> sheeps = new ArrayList<>();
    private ArrayList<Wheat> wheats = new ArrayList<>();
    private ArrayList<Wood> woods = new ArrayList<>();

    /**
     * Creates a full ResourceCardBank
     * @param game The object that contains the DevelopmentCardBank
     */
    public ResourceCardBank(Game game) {
        OWNER = game;
        fillBrick();
        fillOre();
        fillSheep();
        fillWheat();
        fillWood();
    }

    /**
     * Creates an empty ResourceCardBank
     * @param player The object that contains the ResourceCardBank
     */
    public ResourceCardBank (Player player) {

        OWNER = player;
    }

    /**
     * Construct a ResourceCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public ResourceCardBank(JsonObject json) {
        //deserialize
        OWNER = null;
    }

    private void fillBrick() {
        for (int i = 0; i < MAX_NUMBER_BRICK; i++) {
            addResource(new Brick());
        }
    }

    private void fillOre() {
        for (int i = 0; i < MAX_NUMBER_ORE; i++) {
            addResource(new Ore());
        }
    }

    private void fillSheep() {
        for (int i = 0; i < MAX_NUMBER_SHEEP; i++) {
            addResource(new Sheep());
        }
    }

    private void fillWheat() {
        for (int i = 0; i < MAX_NUMBER_WHEAT; i++) {
            addResource(new Wheat());
        }
    }

    private void fillWood() {
        for (int i = 0; i < MAX_NUMBER_WOOD; i++) {
            addResource(new Wood());
        }
    }

    /**
     * Adds a ResourceCard to the ResourceCardBank
     * @param cardToAdd ResourceCard to add
     */
    public void addResource(ResourceCard cardToAdd) {
        switch (cardToAdd.getType()) {
            case WOOD:
                woods.add((Wood) cardToAdd);
                break;
            case BRICK:
                bricks.add((Brick) cardToAdd);
                break;
            case SHEEP:
                sheeps.add((Sheep) cardToAdd);
                break;
            case WHEAT:
                wheats.add((Wheat) cardToAdd);
                break;
            case ORE:
                ores.add((Ore) cardToAdd);
                break;
        }
    }

    public ResourceCard draw(ResourceType type) throws Exception {
        if (!(OWNER instanceof Game)) {
            throw new Exception("Can't call draw on ResourceCardBank owned by Player!");
        } else {
            switch (type) {
                case BRICK:
                    if (bricks.size() > 0) {
                        return bricks.remove(0);
                    } else {
                        return null;
                    }
                    break;
                case ORE:
                    if (ores.size() > 0) {
                        return ores.remove(0);
                    } else {
                        return null;
                    }
                    break;
                case SHEEP:
                    if (sheeps.size() > 0) {
                        return sheeps.remove(0);
                    } else {
                        return null;
                    }
                    break;
                case WHEAT:
                    if (wheats.size() > 0) {
                        return wheats.remove(0);
                    } else {
                        return null;
                    }
                    break;
                case WOOD:
                    if (woods.size() > 0) {
                        return woods.remove(0);
                    } else {
                        return null;
                    }
                    break;
                default:
                    throw new Exception("Invalid ResourceType");
            }
        }
    }

    public int getNumberOfBrick() {
        return bricks.size();
    }

    public int getNumberOfOre() {
        return ores.size();
    }

    public int getNumberOfSheep() {
        return sheeps.size();
    }

    public int getNumberOfWheat() {
        return wheats.size();
    }

    public int getNumberOfWood() {
        return woods.size();
    }

    /**
     * @return number of cards in the ResourceCardBank
     */
    public int size() {
        return bricks.size() + ores.size() + sheeps.size() + wheats.size() + woods.size();
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
