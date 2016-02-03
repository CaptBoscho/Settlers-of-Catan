package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;
import shared.model.game.trade.TradeType;
import shared.model.resources.*;

import javax.naming.InsufficientResourcesException;
import java.util.*;

/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public class ResourceCardBank implements JsonSerializable, IResourceCardBank {
    static final int MAX_NUMBER_BRICK = 15;
    static final int MAX_NUMBER_ORE = 15;
    static final int MAX_NUMBER_SHEEP = 15;
    static final int MAX_NUMBER_WHEAT = 15;
    static final int MAX_NUMBER_WOOD = 15;

    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<Ore> ores = new ArrayList<>();
    private ArrayList<Sheep> sheeps = new ArrayList<>();
    private ArrayList<Wheat> wheats = new ArrayList<>();
    private ArrayList<Wood> woods = new ArrayList<>();

    private boolean ownedByGame;

    /**
     * Creates a full ResourceCardBank
     * @param ownedByGame A boolean telling if the ResourceCardBank is owned by the Game
     */
    public ResourceCardBank(boolean ownedByGame) {
        this.ownedByGame = ownedByGame;
        if (ownedByGame) {
            fillBrick();
            fillOre();
            fillSheep();
            fillWheat();
            fillWood();
        }
    }

    /**
     * Construct a ResourceCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public ResourceCardBank(JsonObject json) {
        //deserialize
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

    @Override
    public ResourceCard draw(ResourceType type) throws InvalidTypeException, Exception {
        if (ownedByGame) {
            return removeCard(type);
        } else {
            throw new Exception("Can't choose which resource to draw from player");
        }
    }

    @Override
    public ResourceCard draw() throws Exception {
        if (ownedByGame) {
            throw new Exception("Must specify Resource Type to draw from Game");
        } else {
            List<ResourceCard> hand = new ArrayList<>();
            for (ResourceCard brick : bricks) {
                hand.add(brick);
            }
            for (ResourceCard wood : woods) {
                hand.add(wood);
            }
            for (ResourceCard sheep : sheeps) {
                hand.add(sheep);
            }
            for (ResourceCard wheat : wheats) {
                hand.add(wheat);
            }
            for (ResourceCard ore : ores) {
                hand.add(ore);
            }
            return hand.get(new Random().nextInt(hand.size()));
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
    @Override
    public int size() {
        return getNumberOfBrick() + getNumberOfOre() + getNumberOfSheep() + getNumberOfWheat() + getNumberOfWood();
    }

    @Override
    public ResourceCard discard(ResourceType type) throws InsufficientResourcesException, InvalidTypeException {
        return removeCard(type);
    }

    @Override
    public boolean canOfferTrade() {
        return size() > 0;
    }

    @Override
    public boolean canMaritimeTrade(PortType type) throws InsufficientResourcesException, InvalidTypeException {
        switch (type) {
            case BRICK:
                return (getNumberOfBrick() >= 2);
            case ORE:
                return (getNumberOfOre() >= 2);
            case SHEEP:
                return (getNumberOfSheep() >= 2);
            case WHEAT:
                return (getNumberOfWheat() >= 2);
            case WOOD:
                return (getNumberOfWood() >= 2);
            case THREE:
                return (getNumberOfBrick() >= 3 || getNumberOfOre() >= 3 || getNumberOfWheat() >= 3 || getNumberOfWood() >= 3 || getNumberOfSheep() >= 3);
            default:
                throw new InvalidTypeException("The given type is invalid");
        }
    }

    @Override
    public boolean canBuyDevCard() {
        return (getNumberOfSheep() > 0 && getNumberOfWheat() > 0 && getNumberOfOre() > 0);
    }

    @Override
    public void buyDevCard() {
        try {
            discard(ResourceType.ORE);
            discard(ResourceType.SHEEP);
            discard(ResourceType.WHEAT);
        } catch (InsufficientResourcesException | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canBuildRoad() {
        return (getNumberOfBrick() > 0 && getNumberOfWood() > 0);
    }

    @Override
    public void buildRoad() throws InsufficientResourcesException {
        if (canBuildRoad()) {
            try {
                discard(ResourceType.WOOD);
                discard(ResourceType.BRICK);
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        } else {
            throw new InsufficientResourcesException("Not enough resources to build road");
        }
    }

    @Override
    public boolean canBuildSettlement() {
        return (getNumberOfWood() >= 1 && getNumberOfBrick() >= 1 && getNumberOfWheat() >= 1 && getNumberOfSheep() >= 1);
    }

    @Override
    public void buildSettlement() throws InsufficientResourcesException {
        if (canBuildSettlement()) {
            try {
                discard(ResourceType.BRICK);
                discard(ResourceType.SHEEP);
                discard(ResourceType.WHEAT);
                discard(ResourceType.WOOD);
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        } else {
            throw new InsufficientResourcesException("Not enough resources to build settlement");
        }
    }

    @Override
    public boolean canBuildCity() {
        return (getNumberOfWheat() >= 3 && getNumberOfOre() >= 2);
    }

    @Override
    public void buildCity() throws InsufficientResourcesException {
        if (canBuildCity()) {
            try {
                discard(ResourceType.WHEAT);
                discard(ResourceType.WHEAT);
                discard(ResourceType.WHEAT);
                discard(ResourceType.ORE);
                discard(ResourceType.ORE);
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        } else {
            throw new InsufficientResourcesException("Not enough resources to build city");
        }
    }

    @Override
    public boolean canDiscardCards() {
        return (size() > 7);
    }

    private ResourceCard removeCard(ResourceType type) throws InsufficientResourcesException, InvalidTypeException {
        switch (type) {
            case BRICK:
                if (getNumberOfBrick() > 0) {
                    return bricks.remove(0);
                } else {
                    throw new InsufficientResourcesException("There are no available bricks");
                }
            case ORE:
                if (getNumberOfOre() > 0) {
                    return ores.remove(0);
                } else {
                    throw new InsufficientResourcesException("There are no available ores");
                }
            case SHEEP:
                if (getNumberOfSheep() > 0) {
                    return sheeps.remove(0);
                } else {
                    throw new InsufficientResourcesException("There are no available sheep");
                }
            case WHEAT:
                if (getNumberOfWheat() > 0) {
                    return wheats.remove(0);
                } else {
                    throw new InsufficientResourcesException("There are no available wheats");
                }
            case WOOD:
                if (getNumberOfWood() > 0) {
                    return woods.remove(0);
                } else {
                    throw new InsufficientResourcesException("There are no available woods");
                }
            default:
                throw new InvalidTypeException("The given type is invalid");
        }
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
