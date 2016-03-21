package com.dargueta.shared.model.player;

import com.google.gson.JsonObject;
import com.dargueta.shared.definitions.DevCardType;
import com.dargueta.shared.definitions.PortType;
import com.dargueta.shared.definitions.ResourceType;
import com.dargueta.shared.definitions.CatanColor;
import com.dargueta.shared.model.cards.Card;
import com.dargueta.shared.model.cards.devcards.DevelopmentCard;
import com.dargueta.shared.model.cards.resources.ResourceCard;
import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a player in the game - Super class for User Players and AI Players
 *
 * @author Kyle Cornelison
 */
public class Player implements IPlayer, Comparable<Player> {
    //region Member variables
    String name;
    int playerId;
    CatanColor color;
    int playerIndex;
    int victoryPoints;
    private int monuments;
    private int soldiers;
    private boolean discarded;
    private boolean moveRobber;
    private boolean playedDevCard;
    private StructureBank structureBank;
    private IResourceCardBank resourceCardBank;
    private IDevelopmentCardBank developmentCardBank;
    private PlayerType playerType = PlayerType.USER;
    //endregion

    //region Constructors
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

        this.name = json.get("name").getAsString();
        this.playerId = json.get("playerID").getAsInt();
        this.playerIndex = json.get("playerIndex").getAsInt();
        this.color = CatanColor.translateFromString(json.get("color").getAsString());
        this.victoryPoints = json.get("victoryPoints").getAsInt();

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
        //assert color != null;

        this.soldiers = 0;
        this.resourceCardBank = new ResourceCardBank(false);
        this.developmentCardBank = new DevelopmentCardBank(false);
        this.structureBank = new StructureBank();
        this.moveRobber = false;
        this.name = name;
        this.victoryPoints = points;
        this.color = color;
        this.playerId = id;
        this.playerIndex = playerIndex;
    }
    //endregion

    //region Can do methods
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards() {
        return resourceCardBank.canDiscardCards();
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
     * @param type Type of trade
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
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder() {
        return (!hasPlayedDevCard() && developmentCardBank.canUseRoadBuild()) && structureBank.getAvailableRoads() > 1;
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
    //endregion

    //region Do methods
    /**
     * Action - Player discards cards
     *
     * @param cards Cards to be discarded
     */
    @Override
    public List<ResourceCard> discardCards(List<Card> cards) throws InsufficientResourcesException, InvalidTypeException {
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

    /**
     * Action - Player discards resource cards
     * @param cards
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    @Override
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

    /**
     * Action - Player buys a dev card
     */
    @Override
    public void buyDevCard() {
        resourceCardBank.buyDevCard();
    }

    /**
     * Action - Player plays Year of Plenty
     */
    @Override
    public void useYearOfPlenty() throws DevCardException {
        assert this.developmentCardBank != null;
        assert developmentCardBank.getNumberOfDevCardsByType(DevCardType.YEAR_OF_PLENTY) > 0;

        if(canUseYearOfPlenty()) {
            developmentCardBank.useYearOfPlenty();
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
    }

    /**
     * Action - Player plays Road Builder
     */
    @Override
    public void useRoadBuilder() throws DevCardException {
        assert this.developmentCardBank != null;

        if(canUseRoadBuilder()) {
            developmentCardBank.useRoadBuild();
        } else {
            throw new DevCardException("Player has already played a Development card this turn!");
        }
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

    /**
     * Action - Player loses the largest army card to another player
     */
    @Override
    public void loseArmyCard() {
        incrementPoints(-2);
    }

    /**
     * Action - Player wins the largest army card from another player
     */
    @Override
    public void winArmyCard() {
        incrementPoints(2);
    }

    /**
     * Action - Player robbed by another player
     *
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    @Override
    public ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException {
        assert this.resourceCardBank != null;

        return resourceCardBank.robbed();
    }

    /**
     * Moves new development cards to old pile making them playable
     *
     * @throws BadCallerException
     */
    @Override
    public void moveNewToOld() throws BadCallerException {
        developmentCardBank.moveNewToOld();
    }
    //endregion

    //region Getters
    /**
     * Get the number of resource cards the player has
     *
     * @return
     */
    @Override
    public int getNumberResourceCards() {
        return resourceCardBank.size();
    }

    /**
     * Get the number of dev cards the player has of the specified type
     *
     * @param type
     * @return
     */
    @Override
    public int getNumberOfDevCardsByType(DevCardType type) {
        return developmentCardBank.getNumberOfDevCardsByType(type);
    }

    /**
     * Get the number of resource cards the player has matching the specified type
     * @param resourceType
     * @return
     */
    @Override
    public int getNumberOfType(ResourceType resourceType) {
        assert resourceType != null;
        assert this.resourceCardBank != null;

        return resourceCardBank.getNumberOfType(resourceType);
    }

    /**
     * Determine how many resources the player has of the specified type
     *
     * @param resourceType
     * @return
     * @throws InvalidTypeException
     */
    @Override
    public Integer howManyOfThisCard(ResourceType resourceType) throws InvalidTypeException {
        assert resourceType != null;
        assert this.resourceCardBank != null;

        switch(resourceType) {
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
        throw new InvalidTypeException("not correct resource type");
    }

    /**
     * Get the number of total dev cards the player has
     *
     * @return
     */
    @Override
    public Integer quantityOfDevCards() {
        return developmentCardBank.size();
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public CatanColor getColor() {
        return this.color;
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

    public int getSoldiers() {
        return soldiers;
    }

    public boolean hasPlayedDevCard() {
        return playedDevCard;
    }

    public int getPlayerIndex() {
        return this.playerIndex;
    }

    public IDevelopmentCardBank getDevelopmentCardBank(){return developmentCardBank;}

    public IResourceCardBank getResourceCardBank(){return resourceCardBank;}

    public int countResources(){return resourceCardBank.size();}

    public boolean hasDiscarded() {
        return discarded;
    }

    public boolean canMoveRobber() {
        return moveRobber;
    }

    public int getId() {
        return this.playerId;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Get the type of Player [User,AI]
     *
     * @return
     */
    public PlayerType getPlayerType(){
        return this.playerType;
    }
    //endregion

    //region Setters
    public void setMoveRobber(boolean canMoveRobber) {
        this.moveRobber = canMoveRobber;
    }

    public void setPlayerIndex(final int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    /**
     * Set the player's type
     *
     * @param type
     */
    public void setPlayerType(PlayerType type){
        this.playerType = type;
    }
    //endregion

    //region Helper methods
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
    private void incrementMonuments() {
        this.monuments++;
    }

    /**
     * Adds a dev card to developmentCardBank - Testing
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

    /**
     * Adds a resource card to resourceCardBank - Testing
     *
     * @param cardToAdd
     */
    public void addResourceCard(ResourceCard cardToAdd) {
        resourceCardBank.addResource(cardToAdd);
    }
    //endregion

    //region Override default methods
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
    //endregion

    //region Serialization
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
    //endregion
}
