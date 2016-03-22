package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;
import shared.model.cards.resources.*;

import javax.naming.InsufficientResourcesException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A bank owned by either a Player or a game which holds all the owner
 * DevelopmentCards
 *
 * @author Danny Harding
 */
public final class ResourceCardBank implements JsonSerializable, IResourceCardBank {
    static final int MAX_NUMBER_BRICK = 15;
    static final int MAX_NUMBER_ORE = 15;
    static final int MAX_NUMBER_SHEEP = 15;
    static final int MAX_NUMBER_WHEAT = 15;
    static final int MAX_NUMBER_WOOD = 15;

    private List<Brick> bricks = new ArrayList<>();
    private List<Ore> ores = new ArrayList<>();
    private List<Sheep> sheeps = new ArrayList<>();
    private List<Wheat> wheats = new ArrayList<>();
    private List<Wood> woods = new ArrayList<>();

    private boolean ownedByGame;

    /**
     * Creates a full ResourceCardBank
     * @param ownedByGame A boolean telling if the ResourceCardBank is owned by the Game
     */
    public ResourceCardBank(final boolean ownedByGame) {
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
    public ResourceCardBank(final JsonObject json, final boolean ownedByGame) {
        this.ownedByGame = ownedByGame;

        for (int i = 0; i < json.get("brick").getAsInt(); i++) {
            addResource(new Brick());
        }
        for (int i = 0; i < json.get("wheat").getAsInt(); i++) {
            addResource(new Wheat());
        }
        for (int i = 0; i < json.get("wood").getAsInt(); i++) {
            addResource(new Wood());
        }
        for (int i = 0; i < json.get("sheep").getAsInt(); i++) {
            addResource(new Sheep());
        }
        for (int i = 0; i < json.get("ore").getAsInt(); i++) {
            addResource(new Ore());
        }
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
    public ResourceCard draw(final ResourceType type) throws InvalidTypeException, Exception {
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
            final List<ResourceCard> hand = bricks.stream().collect(Collectors.toList());
            hand.addAll(woods.stream().collect(Collectors.toList()));
            hand.addAll(sheeps.stream().collect(Collectors.toList()));
            hand.addAll(wheats.stream().collect(Collectors.toList()));
            hand.addAll(ores.stream().collect(Collectors.toList()));
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

    public Integer getNumberOfType(ResourceType type){
        int size = 0;
        switch (type) {
            case WOOD:
                size = getNumberOfWood();
                break;
            case BRICK:
                size = getNumberOfBrick();
                break;
            case SHEEP:
                size = getNumberOfSheep();
                break;
            case WHEAT:
                size = getNumberOfWheat();
                break;
            case ORE:
                size = getNumberOfOre();
                break;
        }
        return size;
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

    public List<ResourceCard> discard(ResourceType rt, int amount) throws InvalidTypeException, InsufficientResourcesException {
        List<ResourceCard> discardedCards = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            discardedCards.add(this.removeCard(rt));
        }
        return discardedCards;
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
        return (getNumberOfWheat() >= 2 && getNumberOfOre() >= 3);
    }

    @Override
    public void buildCity() throws InsufficientResourcesException {
        if (canBuildCity()) {
            try {
                discard(ResourceType.WHEAT);
                discard(ResourceType.WHEAT);
                discard(ResourceType.ORE);
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

    private ResourceCard removeCard(final ResourceType type) throws InsufficientResourcesException, InvalidTypeException {
        switch (type) {
            case BRICK:
                if (getNumberOfBrick() > 0) {
                    return bricks.remove(0);
                }
            case ORE:
                if (getNumberOfOre() > 0) {
                    return ores.remove(0);
                }
            case SHEEP:
                if (getNumberOfSheep() > 0) {
                    return sheeps.remove(0);
                }
            case WHEAT:
                if (getNumberOfWheat() > 0) {
                    return wheats.remove(0);
                }
            case WOOD:
                if (getNumberOfWood() > 0) {
                    return woods.remove(0);
                }
            default:
                throw new InvalidTypeException("The given type is invalid");
        }
    }


    public boolean canBeRobbed(){
        return size() > 0;
    }

    public ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException{
        if(canBeRobbed()){
            final List<ResourceType> content = new ArrayList<>();
            if(getNumberOfBrick() > 0){content.add(ResourceType.BRICK);}
            if(getNumberOfOre() > 0){content.add(ResourceType.ORE);}
            if(getNumberOfSheep() > 0){content.add(ResourceType.SHEEP);}
            if(getNumberOfWheat() > 0){content.add(ResourceType.WHEAT);}
            if(getNumberOfWood() > 0){content.add(ResourceType.WOOD);}

            int card = new Random().nextInt(content.size());

            return discard(content.get(card));
        } else {
            throw new InsufficientResourcesException("this player has no resources");
        }
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty("brick", getNumberOfBrick());
        json.addProperty("wood", getNumberOfWood());
        json.addProperty("sheep", getNumberOfSheep());
        json.addProperty("wheat", getNumberOfWheat());
        json.addProperty("ore", getNumberOfOre());
        return json;
    }
}
