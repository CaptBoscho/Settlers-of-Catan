package shared.model.player;

import client.data.PlayerInfo;
import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.model.bank.*;
import shared.definitions.CatanColor;
import shared.model.cards.Card;
import shared.model.cards.devcards.DevelopmentCard;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a player in the game
 *
 * @author Kyle Cornelison
 */
public final class Player implements IPlayer, Comparable<Player> { // TODO: 1/30/2016 Add exceptions when danny is done
    private int monuments;
    private int soldiers;
    private boolean discarded;
    private boolean moveRobber;
    private boolean playedDevCard;
    private StructureBank structureBank;
    private IResourceCardBank resourceCardBank;
    private IDevelopmentCardBank developmentCardBank;
    private PlayerInfo playerInfo;

    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Player(final JsonObject json) {
        assert json != null;
        assert json.has("playerID");
        assert json.has("playerIndex");
        assert json.has("monuments");
        assert json.has("victoryPoints");
        assert json.has("discarded");
        assert json.has("playedDevCard");
        assert json.has("soldiers");
        assert json.has("resources");
        assert json.has("roads");
        assert json.has("settlements");
        assert json.has("cities");

        final String name = json.get("name").getAsString();
        final int playerId = json.get("playerID").getAsInt();
        final int playerIndex = json.get("playerIndex").getAsInt();
        final String color = json.get("color").getAsString();
        final int victoryPoints = json.get("victoryPoints").getAsInt();

        // TODO - last bools should come from JSON
        this.playerInfo = new PlayerInfo(name, victoryPoints, CatanColor.translateFromString(color), playerId, playerIndex, false, false);

        try {
            this.developmentCardBank = new DevelopmentCardBank(json.getAsJsonObject("oldDevCards"), false);
            this.developmentCardBank.addDevCards(json.getAsJsonObject("newDevCards"));
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        this.monuments = json.get("monuments").getAsInt();
        this.discarded = json.get("discarded").getAsBoolean();
        this.playedDevCard = json.get("playedDevCard").getAsBoolean();
        this.soldiers = json.get("soldiers").getAsInt();

        this.resourceCardBank = new ResourceCardBank(json.getAsJsonObject("resources"), false);
        this.structureBank = new StructureBank(json.get("roads").getAsInt(), json.get("settlements").getAsInt(), json.get("cities").getAsInt());
    }

    private void setColor(final String color) throws InvalidColorException {
        assert color != null;
        assert color.length() > 0;

        switch(color) {
            case "red":
                this.playerInfo.setColor(CatanColor.RED);
                break;
            case "blue":
                this.playerInfo.setColor(CatanColor.BLUE);
                break;
            case "green":
                this.playerInfo.setColor(CatanColor.GREEN);
                break;
            case "brown":
                this.playerInfo.setColor(CatanColor.BROWN);
                break;
            case "orange":
                this.playerInfo.setColor(CatanColor.ORANGE);
                break;
            case "puce":
                this.playerInfo.setColor(CatanColor.PUCE);
                break;
            case "purple":
                this.playerInfo.setColor(CatanColor.PURPLE);
                break;
            case "white":
                this.playerInfo.setColor(CatanColor.WHITE);
                break;
            default:
                throw new InvalidColorException("The given color is invalid");
        }
    }

    /**
     * New Player Constructor
     * @param points    Initial points
     * @param color     Player Color
     * @param name      Player Name
     * @param id        Player ID
     * @param playerIndex The index of the player in a particular game (0-3)
     */
    public Player(int points, CatanColor color, int id, int playerIndex, String name) throws InvalidPlayerException {
        assert points >= 0;
        assert name != null;
        assert color != null;

        this.soldiers = 0;
        this.resourceCardBank = new ResourceCardBank(false);
        this.developmentCardBank = new DevelopmentCardBank(false);
        this.structureBank = new StructureBank();
        this.moveRobber = false;
        this.playerInfo = new PlayerInfo(name, points, color, id, playerIndex, false, false);
    }

    //IPlayer Interface Methods - Can Do & Do
    //========================================================

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     *
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards() {
        return resourceCardBank.canDiscardCards(); // TODO: 1/30/2016 What is this one supposed to do??? Discard on 7???
    }

    /**
     * Action - Player discards cards
     *
     * @param cards Cards to be discarded
     */
    @Override
    public List<ResourceCard> discardCards(final List<Card> cards) throws InsufficientResourcesException, InvalidTypeException {
        assert cards != null;
        assert cards.size() > 0;
        assert this.resourceCardBank != null;
        assert this.developmentCardBank != null;

        try {
            final List<ResourceCard> discarded = new ArrayList<>();
            for (final Card card : cards) {
                if (card instanceof ResourceCard) {
                   final  ResourceCard resourceCard = (ResourceCard) card;
                    discarded.add(resourceCardBank.discard(resourceCard.getType()));
                } else if (card instanceof DevelopmentCard) {
                    final DevelopmentCard developmentCard = (DevelopmentCard) card;
                    developmentCardBank.discard(developmentCard.getType());
                }
            }
            setDiscarded(true);
            return discarded;
        } catch (InsufficientResourcesException | InvalidTypeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ResourceCard> discardResourceCards(ResourceType rt, int amount) {
        assert rt != null;
        try {
            return this.resourceCardBank.discard(rt, amount);
        } catch (InvalidTypeException | InsufficientResourcesException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ResourceCard> discardResourceCards(final List<ResourceType> cards) throws InsufficientResourcesException, InvalidTypeException {
        assert cards != null;
        assert cards.size() > 0;
        assert this.resourceCardBank != null;

        final List<ResourceCard> discarded = new ArrayList<>();
        for(final ResourceType rt: cards) {
            discarded.add(resourceCardBank.discard(rt));
        }

        setDiscarded(true);
        return discarded;
    }

    public Integer howManyOfThisCard(ResourceType t) throws InvalidTypeException {
        assert t != null;
        assert this.resourceCardBank != null;

        switch(t) {
            case SHEEP:
                return resourceCardBank.getNumberOfSheep();
            case ORE:
                return resourceCardBank.getNumberOfOre();
            case BRICK:
                return resourceCardBank.getNumberOfBrick();
            case WOOD:
                return resourceCardBank.getNumberOfWood();
            case WHEAT:
                return resourceCardBank.getNumberOfWheat();
        }
        throw new InvalidTypeException("not correct resourcetype");
    }

    public int getNumberOfType(ResourceType t) {
        assert t != null;
        assert this.resourceCardBank != null;

        return resourceCardBank.getNumberOfType(t);
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade() {
        return resourceCardBank.canOfferTrade();
    }

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     *
     * @return True if Player can perform a maritime trade
     */
    @Override
    public boolean canMaritimeTrade(PortType type) {
        assert type != null;
        assert this.resourceCardBank != null;

        try {
            return resourceCardBank.canMaritimeTrade(type);
        } catch (InsufficientResourcesException | InvalidTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer quantityOfDevCards(){
        return developmentCardBank.size();
    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard() {
        return resourceCardBank.canBuyDevCard();
    }

    /**
     * Action - Player buys a dev card
     */
    @Override
    public void buyDevCard() {
        resourceCardBank.buyDevCard();
    }


    public void moveNewToOld() throws BadCallerException{ developmentCardBank.moveNewToOld();}

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty() {
        assert this.developmentCardBank != null;

        return !hasPlayedDevCard() && developmentCardBank.canUseYearOfPlenty();
    }

    /**
     * Action - Player plays Year of Plenty
     */
    @Override
    public void useYearOfPlenty() throws DevCardException {
        assert this.developmentCardBank != null;

        if(canUseYearOfPlenty()) {
            developmentCardBank.useYearOfPlenty();
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder() {
        return (!hasPlayedDevCard() && developmentCardBank.canUseRoadBuild());
    }

    /**
     * Action - Player plays Road Builder
     */
    @Override
    public void useRoadBuilder() throws DevCardException {
        assert this.developmentCardBank != null;

        if(canUseRoadBuilder()) {
            developmentCardBank.useRoadBuild();
            // TODO: 1/30/2016 Add any additional functionality - does the map or the structure bank build the road 
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier() {
        return (!hasPlayedDevCard() && developmentCardBank.canUseSoldier());
    }

    /**
     * Action - Player plays Soldier
     */
    @Override
    public void useSoldier() throws DevCardException {
        assert this.developmentCardBank != null;

        if(canUseSoldier()) {
            developmentCardBank.useSoldier();
            setMoveRobber(true);
            this.soldiers++;
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    public void loseArmyCard() {
        incrementPoints(-2);
    }


    public void winArmyCard() {
        incrementPoints(2);
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly() {
        assert this.developmentCardBank != null;

        return !hasPlayedDevCard() && developmentCardBank.canUseMonopoly();
    }

    /**
     * Action - Player plays Monopoly
     */
    @Override
    public void discardMonopoly() throws DevCardException {
        if(canUseMonopoly()) {
            developmentCardBank.useMonopoly();
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument() {
        return (!hasPlayedDevCard() && developmentCardBank.canUseMonument());
    }

    /**
     * Action - Player plays Monument
     */
    @Override
    public void useMonument() throws DevCardException {
        if(canUseMonument()) {
            developmentCardBank.useMonument();
            incrementMonuments();
            incrementPoints();
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber() {
        return canMoveRobber();
    }

    /**
     * Action - Player places the Robber
     */
    @Override
    public void placeRobber() throws MoveRobberException {
        if(canMoveRobber()) {
            setMoveRobber(false);
        } else {
            throw new MoveRobberException("Player cannot move the Robber at this time!");
        }
    }

    public ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException{
        assert this.resourceCardBank != null;

        return resourceCardBank.robbed();
    }

    /**
     * Determine if Player can build a road
     * Checks resource cards
     *
     * @return True if Player can build a road
     */
    @Override
    public boolean canBuildRoad() {
        return resourceCardBank.canBuildRoad() && structureBank.canBuildRoad();
    }

    /**
     * Action - Player builds a road
     */
    @Override
    public void buildRoad() {
        assert this.resourceCardBank != null;
        assert this.structureBank != null;

        try {
            resourceCardBank.buildRoad();
            structureBank.buildRoad();
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     *
     * @return True if Player can build a settlement
     */
    @Override
    public boolean canBuildSettlement() {
        assert this.resourceCardBank != null;
        assert this.structureBank != null;

        return resourceCardBank.canBuildSettlement() && structureBank.canBuildSettlement();
    }

    /**
     * Action - Player builds a settlement
     */
    @Override
    public void buildSettlement() {
        assert this.resourceCardBank != null;
        assert this.structureBank != null;

        //Delegate action
        try {
            resourceCardBank.buildSettlement();
            structureBank.buildSettlement();
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        }
        //Increment points
        incrementPoints();
    }

    /**
     * Determine if Player can build a city
     * Checks resource cards
     *
     * @return True if Player can build a city
     */
    @Override
    public boolean canBuildCity() {
        assert this.resourceCardBank != null;
        assert this.structureBank != null;

        return resourceCardBank.canBuildCity() && structureBank.canBuildCity();
    }

    /**
     * Action - Player builds a city
     */
    @Override
    public void buildCity() {
        assert this.resourceCardBank != null;
        assert this.structureBank != null;

        //Delegate action
        try {
            resourceCardBank.buildCity();
            structureBank.buildCity();
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
        }
        //Increment points
        incrementPoints(); //Note: only have to increment by 1 since we are replacing a settlement
    }

    //Helper Methods
    //======================================================
    /**
     * Increments the player's points by 1
     */
    private void incrementPoints() {
        incrementPoints(1);
    }

    /**
     * Increments the player's points by value of increment
     *
     * @param increment Number of points to add to the player's score
     */
    private void incrementPoints(int increment) {
        this.playerInfo.setVictoryPoints(this.getVictoryPoints() + increment);
    }

    /**
     * Increments the player's monuments
     * - points are added when the player plays a monument card
     */
    private void incrementMonuments() {
        this.monuments++;
    }

    /**
     * Adds a dev card to developmentCardBank
     *
     * @param cardToAdd
     */
    public void addDevCard(DevelopmentCard cardToAdd) {
        try {
            developmentCardBank.addDevCard(cardToAdd);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    public void playKnight() {
        this.soldiers++;
    }


    public Integer getKnights() {
        return this.soldiers;
    }

    /**
     * Adds a resource card to resourceCardBank
     *
     * @param cardToAdd
     */
    public void addResourceCard(ResourceCard cardToAdd) {
        resourceCardBank.addResource(cardToAdd);
    }

     //Override Default Methods
     //========================================================
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Player))return false;

        Player otherPlayer = (Player)other;
        return otherPlayer.getId() == this.getId() && otherPlayer.getName().equals(this.getName()) && otherPlayer.getVictoryPoints() == this.getVictoryPoints() && otherPlayer.getColor() == this.getColor();
    }

    @Override
    public int compareTo(final Player otherPlayer) {
        if (this.getId() > otherPlayer.getId()) {
            return 1;
        } else if (this.getId() < otherPlayer.getId()) {
            return -1;
        } else {
            return 0;
        }
    }

    //Serialization/Deserialization
    //=================================================
    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty("cities", structureBank.getAvailableCities());
        json.addProperty("color", this.getColor().toString());
        json.addProperty("discarded", discarded);
        json.addProperty("monuments", monuments);
        json.addProperty("name", this.getName());
        json.add("oldDevCards", developmentCardBank.toJSON());
        json.add("newDevCards", developmentCardBank.newCardsToJSON());
        json.addProperty("playerIndex", this.getPlayerIndex());
        json.addProperty("playedDevCard", playedDevCard);
        json.addProperty("playerID", this.getId());
        json.add("resources", resourceCardBank.toJSON());
        json.addProperty("roads", structureBank.getAvailableRoads());
        json.addProperty("settlements", structureBank.getAvailableSettlements());
        json.addProperty("soldiers", soldiers);
        json.addProperty("victoryPoints", this.getVictoryPoints());

        return json;
    }

    //Getters/Setters
    //============================================

    public int getId() {
        return this.playerInfo.getId();
    }

    public String getName() {
        return this.playerInfo.getName();
    }

    @Override
    public PlayerInfo getInfo() {
        return this.playerInfo;
    }

    public IDevelopmentCardBank getDevelopmentCardBank(){return developmentCardBank;}

    public IResourceCardBank getResourceCardBank(){return resourceCardBank;}

    public int countResources(){return resourceCardBank.size();}

    public boolean hasDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public boolean canMoveRobber() {
        return moveRobber;
    }

    public void setMoveRobber(boolean canMoveRobber) {
        this.moveRobber = canMoveRobber;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public int getPlayerIndex() {
        return this.playerInfo.getPlayerIndex();
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerInfo.setPlayerIndex(playerIndex);
    }

    public boolean hasPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getVictoryPoints() {
        return this.playerInfo.getVictoryPoints();
    }

    public CatanColor getColor() {
        return this.playerInfo.getColor();
    }

    public Integer getAvailableRoads() {
        return structureBank.getAvailableRoads();
    }

    public Integer getAvailableSettlements() {
        return structureBank.getAvailableSettlements();
    }

    public Integer getAvailableCities() {
        return structureBank.getAvailableCities();
    }
}
