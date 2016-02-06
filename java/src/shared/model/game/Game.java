package shared.model.game;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.PlayerExistsException;
import shared.model.JsonSerializable;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.definitions.DevCardType;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.InvalidTypeException;
import shared.model.bank.ResourceCardBank;
import shared.definitions.DevCardType;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;
import shared.model.cards.resources.ResourceCard;


import javax.naming.InsufficientResourcesException;
import java.util.*;

/**
 * game class representing a Catan game
 */
public class Game implements IGame, JsonSerializable {

    private static Game instance;

    private Dice dice;
    private Map map;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private int winner;
    private int version;

    /**
     * Constructor
     */
    protected Game() {
        this.dice = new Dice(2,12);
        this.map = new Map(false, false, false);
        this.turnTracker = null;//new TurnTracker(0,0);
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager(new ArrayList<Player>());
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);
    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }

        return instance;
    }

    public void updateGame(JsonObject json) {
        this.developmentCardBank = new DevelopmentCardBank(json.get("deck").getAsJsonObject(), true);
        this.map = new Map(json.get("map").getAsJsonObject());
        this.resourceCardBank = new ResourceCardBank(json.get("bank").getAsJsonObject(), true);

        JsonObject turnTracker = json.getAsJsonObject("turnTracker");
        this.longestRoadCard = new LongestRoad(turnTracker.get("longestRoad").getAsInt());
        this.largestArmyCard = new LargestArmy(turnTracker.get("largestArmy").getAsInt());
        this.version = json.get("version").getAsInt();
        this.winner = json.get("winner").getAsInt();
        try {
            this.turnTracker = new TurnTracker(json.get("turnTracker").getAsJsonObject());
        } catch (BadJsonException e) {
            e.printStackTrace();
        }
    }

    public int getVersion() {
        return this.version;
    }

    //IGame Methods
    //======================================================
    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @return Id of first player
     */
    public int initializeGame(List<Player> players, boolean randomhexes, boolean randomchits, boolean randomports) throws FailedToRandomizeException{
        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        this.map = new Map(randomhexes, randomchits, randomports);
        //List<Integer> order = this.playerManager.randomizePlayers();
        turnTracker = new TurnTracker(players.get(0).get_id());
        turnTracker.setNumPlayers(players.size());

        return turnTracker.getCurrentTurn();
    }

    public boolean canInitiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException{
        if(turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase()){
            return map.canInitiateSettlement(playerID, vertex);
        }
        return false;
    }

    public void initiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException{
        if(canInitiateSettlement(playerID, vertex)){
            map.initiateSettlement(playerID, vertex);
        }
    }

    public boolean canInitiateRoad(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException{
        if(turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase()){
            return map.canInitiateRoad(playerID, edge, vertex);
        }
        return false;
    }

    public void initiateRoad(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException{
        map.initiateRoad(playerID, edge, vertex);
    }

    /**
     * returns the playerID for whose turn it is
     *
     * @return
     */
    @Override
    public int getCurrentTurn() {
        return turnTracker.getCurrentTurn();
    }

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param playerID ID of Player performing action
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int playerID) throws PlayerExistsException {
        return playerManager.canDiscardCards(playerID);

    }

    /**
     * Action - Player discards cards
     *
     * @param playerID ID of Player performing action
     * @param cards    Cards to be discarded
     */
    @Override
    public void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException{
        if(canDiscardCards(playerID)){
            playerManager.discardResourceType(playerID, cards);
        }
    }

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     *
     * @param playerID ID of Player performing action
     * @return True if Player can roll the die
     */
    @Override
    public boolean canRollNumber(int playerID) {
        return turnTracker.isPlayersTurn(playerID) && turnTracker.canRoll();
    }

    /**
     * Action - Player rolls the dice
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public int rollNumber(int playerID) throws InvalidDiceRollException{
        int roll = dice.roll();
        if(roll == 7){
            turnTracker.updateRobber(true);
        } else{
            map.getResources(roll);
        }

        turnTracker.nextPhase();
        return roll;
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int playerID) {
        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
    }

    /**
     * for testing purposes
     * @param card
     * @param id
     * @throws PlayerExistsException
     */
    public void giveResource(ResourceCard card, int id) throws PlayerExistsException{
        playerManager.addResource(id, card);
    }

    /**
     * for testing purposes.
     * @param t
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    public ResourceCard getResourceCard(ResourceType t) throws InsufficientResourcesException, InvalidTypeException{
        return resourceCardBank.discard(t);
    }

    public Integer amountOwnedResource(int playerID, ResourceType t) throws PlayerExistsException, InvalidTypeException{
        return playerManager.getPlayerByID(playerID).howManyofThisCard(t);
    }

    /**
     * Action - Player offers trade
     *
     * @param playerIDOne   ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    @Override
    public void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException{
        if(canOfferTrade(playerIDOne)){
            TradePackage one = new TradePackage(playerIDOne,onecards);
            TradePackage two = new TradePackage(playerIDTwo, twocards);
            Trade trade = new Trade(one,two);

            System.out.println("trading");
            playerManager.offerTrade(playerIDOne,playerIDTwo,onecards,twocards);

        }
    }


    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     *
     * @param playerID ID of Player performing action
     * @return True if Player can finish their turn
     */
    @Override
    public boolean canFinishTurn(int playerID) {
        return turnTracker.canDiscard() && turnTracker.isPlayersTurn(playerID);
    }

    /**
     * Action - Player finishes their turn
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public Integer finishTurn(int playerID) throws Exception {
        return turnTracker.nextTurn();
    }

    public TurnTracker.Phase getCurrentPhase(){
        return turnTracker.getPhase();
    }

    public void nextPhase(){turnTracker.nextPhase();}

    public void setPhase(TurnTracker.Phase p){turnTracker.setPhase(p);}

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int playerID) throws PlayerExistsException{

        return playerManager.canBuyDevCard(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canDiscard();
    }

    /**
     * Action - Player buys a dev card
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void buyDevCard(int playerID) throws PlayerExistsException{
        playerManager.buyDevCard(playerID);
    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int playerID) throws PlayerExistsException{
        if(getCurrentTurn() == playerID){
            return playerManager.canUseYearOfPlenty(playerID) && turnTracker.canPlay();
        }
        return false;
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useYearOfPlenty(int playerID) throws PlayerExistsException, DevCardException{
        if(canUseYearOfPlenty(playerID)){
            playerManager.useYearOfPlenty(playerID);
        }
    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder(int playerID) throws PlayerExistsException{
        return playerManager.canUseRoadBuilder(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useRoadBuilder(int playerID) throws PlayerExistsException, DevCardException{
        if(canUseRoadBuilder(playerID)){
            playerManager.useRoadBuilder(playerID);
        }
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int playerID) throws PlayerExistsException{

        return playerManager.canUseSoldier(playerID)  && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useSoldier(int playerID) throws PlayerExistsException, DevCardException{
        if(canUseSoldier(playerID)){
            playerManager.useSoldier(playerID);
            int used = playerManager.getKnights(playerID);
            if(used >= 3 && used > largestArmyCard.getMostSoldiers()){
                int oldplayer = largestArmyCard.getOwner();
                largestArmyCard.setNewOwner(playerID, used);
                playerManager.changeLargestArmyPossession(oldplayer, playerID);
            }

            turnTracker.updateRobber(true);
        }
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(int playerID) throws PlayerExistsException{
        return playerManager.canUseMonopoly(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonopoly(int playerID) throws PlayerExistsException, DevCardException{
        if(canUseMonopoly(playerID)){
            playerManager.useMonopoly(playerID);
        }
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(int playerID) throws PlayerExistsException{
        return playerManager.canUseMonument(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(int playerID) throws PlayerExistsException, DevCardException{
        if(canUseMonument(playerID)){
            playerManager.useMonument(playerID);
        }
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerID ID of Player performing action
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int playerID) {
        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID) && turnTracker.canUseRobber();
    }

    /**
     * Action - Player places the Robber
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException{
        return map.moveRobber(hexloc);
    }

    public ResourceType rob(int playerrobber, int playerrobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException{
        Set<Integer> who = map.whoCanGetRobbed();
        if(canPlaceRobber(playerrobber) && who.contains(playerrobbed)){
            turnTracker.updateRobber(false);
            ResourceType treasure = playerManager.placeRobber(playerrobber, playerrobbed);
        }

        return null;
    }

    /**
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
       // if(turnTracker.canBuild(playerID)){
            return (map.canBuildRoad(playerID, edge) && playerManager.canBuildRoad(playerID) && turnTracker.canDiscard());
       // }
       // return false;
    }

    /**
     * builds a road for hte player
     *
     * @param playerID
     */
    @Override
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        if(canBuildRoad(playerID, edge)){
            map.buildRoad(playerID, edge);
            playerManager.buildRoad(playerID);
            //check to update longest road
            int roadlength = map.getLongestRoadSize(playerID);
            if(roadlength >= 5 && roadlength > longestRoadCard.getSize()){
                newLongestRoad(longestRoadCard.getOwner(), playerID, roadlength);
            }
        }
    }

    /**
     * checks if the player has the cards to build a settlement
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException{
        return map.canBuildSettlement(playerID, vertex) && playerManager.canBuildSettlement(playerID) && turnTracker.canDiscard(); //&& turnTracker.canBuild(playerID);

    }

    /**
     * builds a settlement for this player
     *
     * @param playerID
     */
    @Override
    public void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        if(canBuildSettlement(playerID, vertex)){
            map.buildSettlement(playerID, vertex);
            playerManager.buildSettlement(playerID);
        }
    }

    /**
     * checks if the player has the cards to ubild a city
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {

        return map.canBuildCity(playerID, vertex) && playerManager.canBuildCity(playerID) && turnTracker.canDiscard(); //&& turnTracker.canBuild(playerID);
    }

    /**
     * builds a city for this player
     *
     * @param playerID
     */
    @Override
    public void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        if(canBuildCity(playerID, vertex)){
            map.buildCity(playerID, vertex);
            playerManager.buildCity(playerID);
        }
    }

    /**
     * returns the value of how many roads is the LongestRoad
     *
     * @return
     */
    @Override
    public int currentLongestRoadSize() {
        return longestRoadCard.getSize();
    }

    /**
     * returns the playerID of who owns the current longest road
     *
     * @return
     */
    @Override
    public int currentLongestRoadPlayer() {
        return longestRoadCard.getOwner();
    }

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     *
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    @Override
    public void newLongestRoad(int playerIDOld, int playerIDNew, int roadSize) {
        longestRoadCard.setOwner(playerIDNew, roadSize);
        //playerManager.newLongestRoad(playerIDOld, playerIDNew)
    }

    /**
     * checks if the player has the cards to buy a DevelopmentCard
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuyDevelopmentCard(int playerID) throws PlayerExistsException {
        return playerManager.canBuyDevCard(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canDiscard();

    }

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerID
     */
    @Override
    public DevCardType buyDevelopmentCard(int playerID) throws PlayerExistsException {
        if(canBuyDevelopmentCard(playerID)){
            playerManager.buyDevCard(playerID);
        }
        return null;
    }

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canTrade(int playerID) {
        return turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();
    }

    /**
     * checks if that player has the card needed for that port's trade
     *
     * @param playerID
     * @param port
     * @return
     */
    @Override
    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException{
        if(canTrade(playerID)){
            Set<PortType> ports = getPortTypes(playerID);
            if(ports.contains(port)){
                return playerManager.canMaritimeTrade(playerID, port);
            }
        }
        return false;
    }

    /**
     * effectuates a trade based on the port type
     *
     * @param playerID
     * @param port
     */
    @Override
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException{

        if(canMaritimeTrade(playerID, port)){
            List<ResourceType> cards = new ArrayList<>();
            if(port == PortType.BRICK) {
                cards.add(ResourceType.BRICK);
                cards.add(ResourceType.BRICK);
            } else if(port == PortType.ORE) {
                cards.add(ResourceType.ORE);
                cards.add(ResourceType.ORE);
            } else if(port == PortType.SHEEP) {
                cards.add(ResourceType.SHEEP);
                cards.add(ResourceType.SHEEP);
            } else if(port == PortType.WHEAT) {
                cards.add(ResourceType.WHEAT);
                cards.add(ResourceType.WHEAT);
            } else if(port == PortType.WOOD) {
                cards.add(ResourceType.WOOD);
                cards.add(ResourceType.WOOD);
            }
            List<ResourceCard> discarded = playerManager.discardResourceType(playerID, cards);

            for(ResourceCard rc: discarded) {
                resourceCardBank.addResource(rc);
            }

            playerManager.addResource(playerID, resourceCardBank.discard(want));
        }
    }

    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException{
        if(canMaritimeTrade(playerID, port)){
            if(port == PortType.THREE) {
                List<ResourceType> cards = new ArrayList<>();
                cards.add(give);
                cards.add(give);
                cards.add(give);

                List<ResourceCard> discarded = playerManager.discardResourceType(playerID, cards);

                for(ResourceCard rc: discarded) {
                    resourceCardBank.addResource(rc);
                }

                playerManager.addResource(playerID, resourceCardBank.discard(want));

            } else {
                throw new InvalidTypeException("not 3:1 port types");
            }
        }
    }



    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException{
        return map.getPortTypes(playerID);
    }

    public TurnTracker getTurnTracker() {
        return turnTracker;
    }

    public PlayerManager getPlayerManager(){
        return playerManager;
    }


    /*======================================================
    * Private - Helper Methods
    * ======================================================*/
    /**
     * Randomize the players' turn order
     */
    private void randomizePlayers() {
        try{
            playerManager.randomizePlayers();
        } catch(Exception e) {
            //throw new
        }
    }

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
