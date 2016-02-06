package shared.model.player;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.model.bank.*;
import shared.definitions.CatanColor;
import shared.model.cards.Card;
import shared.model.cards.devcards.DevelopmentCard;
import shared.model.game.trade.TradeType;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a player in the game
 *
 * @author Kyle Cornelison
 */
public class Player implements IPlayer,Comparable<Player>{ // TODO: 1/30/2016 Add exceptions when danny is done
    private int _id;
    private Name name;
    private int monuments;
    private int playerIndex;
    private CatanColor color;
    private int victoryPoints;
    private int soldiers;
    private boolean discarded;
    private boolean moveRobber;
    private boolean playedDevCard;
    private StructureBank structureBank;
    private IResourceCardBank resourceCardBank;
    private IDevelopmentCardBank developmentCardBank;

    /**
     * Default Constructor
     */
    public Player() {
        this.victoryPoints = 0;
        this.soldiers = 0;
        this.color = null;
        this.resourceCardBank = new ResourceCardBank(false);
        this.developmentCardBank = new DevelopmentCardBank(false);
        this.moveRobber = false;
        this.structureBank = new StructureBank();
        this.playedDevCard = false;
    }

    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Player(JsonObject json) {
        try {
            setColor(json.get("color").getAsString());
            this.name = new Name(json.get("name").getAsString());
            this.developmentCardBank = new DevelopmentCardBank(json.getAsJsonObject("oldDevCards"));
            this.developmentCardBank.addDevCards(json.getAsJsonObject("newDevCards"));
        } catch (InvalidNameException | InvalidColorException | BadCallerException e) {
            e.printStackTrace();
        }

        this._id = json.get("playerID").getAsInt();
        this.playerIndex = json.get("playerIndex").getAsInt();
        this.monuments = json.get("monuments").getAsInt();
        this.victoryPoints = json.get("victoryPoints").getAsInt();
        this.discarded = json.get("discarded").getAsBoolean();
        this.playedDevCard = json.get("playedDevCard").getAsBoolean();
        this.soldiers = json.get("soldiers").getAsInt();

        this.resourceCardBank = new ResourceCardBank(json.getAsJsonObject("resources"));
        this.structureBank = new StructureBank(json.get("roads").getAsInt(), json.get("settlements").getAsInt(), json.get("cities").getAsInt());
    }

    private void setColor(String color) throws InvalidColorException {
        switch(color) {
            case "red":
                this.color = CatanColor.RED;
                break;
            case "blue":
                this.color = CatanColor.BLUE;
                break;
            case "green":
                this.color = CatanColor.GREEN;
                break;
            case "brown":
                this.color = CatanColor.BROWN;
                break;
            case "orange":
                this.color = CatanColor.ORANGE;
                break;
            case "puce":
                this.color = CatanColor.PUCE;
                break;
            case "purple":
                this.color = CatanColor.PURPLE;
                break;
            case "white":
                this.color = CatanColor.WHITE;
                break;
            default:
                throw new InvalidColorException("The given color is invalid");
        }
    }

    private String getColorString() throws InvalidColorException {
        switch(color) {
            case RED:
                return "red";
            case BLUE:
                return "blue";
            case GREEN:
                return "green";
            case BROWN:
                return "brown";
            case ORANGE:
                return "orange";
            case PUCE:
                return "puce";
            case PURPLE:
                return "purple";
            case WHITE:
                return "white";
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
     */
    public Player(int points, CatanColor color, int id, Name name) throws InvalidPlayerException {
        assert points >= 0;
        assert name != null;

        this.victoryPoints = points;
        this.color = color;
        this.soldiers = 0;
        this.resourceCardBank = new ResourceCardBank(false);
        this.developmentCardBank = new DevelopmentCardBank(false);
        this.structureBank = new StructureBank();
        this.name = name;
        this._id = id;
        this.moveRobber = false;
    }

    /**
     * Load Player Constructor
     * @param points    Player Point Counter
     * @param color     Player Color
     * @param rCrdBnk   Player Resources
     * @param devCrdBnk Player Development Cards
     * @param sBnk      Player Structures
     * @param name      Player Name
     * @param index     Player Index
     * @throws InvalidPlayerException
     */
    public Player(int points, CatanColor color, ResourceCardBank rCrdBnk,
                  DevelopmentCardBank devCrdBnk, StructureBank sBnk,
                  int index, Name name, boolean canMoveRobber, int soldiers) throws InvalidPlayerException {
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = rCrdBnk;
        this.developmentCardBank = devCrdBnk;
        this.structureBank = sBnk;
        this.name = name;
        this.playerIndex = index;
        this.moveRobber = canMoveRobber;
        this.soldiers = soldiers;
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
    public List<ResourceCard> discardCards(List<Card> cards) throws InsufficientResourcesException, InvalidTypeException {
        try {
            List<ResourceCard> discarded = new ArrayList<ResourceCard>();
            for (Card card : cards) {
                if (card instanceof ResourceCard) {
                    ResourceCard resourceCard = (ResourceCard) card;
                    discarded.add(resourceCardBank.discard(resourceCard.getType()));
                } else if (card instanceof DevelopmentCard) {
                    DevelopmentCard developmentCard = (DevelopmentCard) card;
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

    public List<ResourceCard> discardResourceCards(List<ResourceType> cards) throws InsufficientResourcesException, InvalidTypeException {
        List<ResourceCard> discarded = new ArrayList<ResourceCard>();
        for(ResourceType rt: cards) {
            discarded.add(resourceCardBank.discard(rt));
        }

        setDiscarded(true);
        return discarded;
    }

    public Integer howManyofThisCard(ResourceType t) throws InvalidTypeException{
        if(t == ResourceType.SHEEP){return resourceCardBank.getNumberOfSheep();}
        if(t == ResourceType.ORE){return resourceCardBank.getNumberOfOre();}
        if(t == ResourceType.BRICK){return resourceCardBank.getNumberOfBrick();}
        if(t == ResourceType.WOOD){return resourceCardBank.getNumberOfWood();}
        if(t == ResourceType.WHEAT){return resourceCardBank.getNumberOfWheat();}
        throw new InvalidTypeException("not correct resourcetype");
    }

    public Integer getNumberOfType(ResourceType t){
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
        return !hasPlayedDevCard() && developmentCardBank.canUseYearOfPlenty();
    }

    /**
     * Action - Player plays Year of Plenty
     */
    @Override
    public void useYearOfPlenty() throws DevCardException {
        if(canUseYearOfPlenty())
            developmentCardBank.useYearOfPlenty();
        else
            throw new DevCardException("Player has already played a Development card this turn!");
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
        if(canUseRoadBuilder())
            developmentCardBank.useRoadBuild();
            // TODO: 1/30/2016 Add any additional functionality - does the map or the structure bank build the road 
        else
            throw new DevCardException("Player has already played a Development card this turn!");
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
        if(canUseSoldier()) {
            developmentCardBank.useSoldier();
            setMoveRobber(true);
            this.soldiers++;
        }else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    public void loseArmyCard(){
        victoryPoints -=2;
    }


    public void winArmyCard(){
        victoryPoints +=2;
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly() {
        return (!hasPlayedDevCard() && developmentCardBank.canUseMonopoly());
    }

    /**
     * Action - Player plays Monopoly
     */
    @Override
    public void useMonopoly() throws DevCardException {
        if(canUseMonopoly())
            developmentCardBank.useMonopoly();
        else
            throw new DevCardException("Player has already played a Development card this turn!");
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
        }else {
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
        if(canMoveRobber())
            setMoveRobber(false);
        else
            throw new MoveRobberException("Player cannot move the Robber at this time!");
    }

    public ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException{

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
        return resourceCardBank.canBuildSettlement() && structureBank.canBuildSettlement();
    }

    /**
     * Action - Player builds a settlement
     */
    @Override
    public void buildSettlement() {
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
        return resourceCardBank.canBuildCity() && structureBank.canBuildCity();
    }

    /**
     * Action - Player builds a city
     */
    @Override
    public void buildCity() {
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
        this.victoryPoints += increment;
    }

    /**
     * Increments the player's monuments
     * - points are added when the player plays a monument card
     */
    private void incrementMonuments(){
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

    public void playKnight(){this.soldiers++;}


    public Integer getKnights(){return this.soldiers;}

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
        return otherPlayer._id == this._id;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        if (this._id > otherPlayer._id) {
            return 1;
        } else if (this._id < otherPlayer._id) {
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
        JsonObject json = new JsonObject();
        json.addProperty("cities", structureBank.getAvailableCities());
        try {
            json.addProperty("color", getColorString());
        } catch (InvalidColorException e) {
            e.printStackTrace();
        }
        json.addProperty("discarded", discarded);
        json.addProperty("monuments", monuments);
        json.addProperty("name", name.toString());
        json.add("oldDevCards", developmentCardBank.toJSON());
        json.add("newDevCards", developmentCardBank.newCardsToJSON());
        json.addProperty("playerIndex", playerIndex);
        json.addProperty("playedDevCard", playedDevCard);
        json.addProperty("playerID", _id);
        json.add("resources", resourceCardBank.toJSON());
        json.addProperty("roads", structureBank.getAvailableRoads());
        json.addProperty("settlements", structureBank.getAvailableSettlements());
        json.addProperty("soldiers", soldiers);
        json.addProperty("victoryPoints", victoryPoints);

        return json;
    }

    //Getters/Setters
    //============================================

    public int get_id() {
        return _id;
    }

    public Name getName() {
        return name;
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
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean hasPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public CatanColor getColor() {
        return color;
    }
}
