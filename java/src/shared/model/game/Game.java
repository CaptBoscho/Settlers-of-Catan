package shared.model.game;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.definitions.DevCardType;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;
import shared.model.resources.ResourceCard;

import java.util.List;
import java.util.Set;

/**
 * game class representing a Catan game
 */
public class Game implements IGame {
    private Dice dice;
    private Map map;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;

    /**
     * Constructor
     */
    public Game() {
        this.dice = new Dice();
        this.map = new Map();
        this.turnTracker = null;//new TurnTracker(0,0);
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);
    }

    //IGame Methods
    //======================================================
    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @return Id of first player
     */
    public int initializeGame(List<Player> players) throws FailedToRandomizeException{
        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        List<Integer> order = this.playerManager.randomizePlayers();
        turnTracker = new TurnTracker(order.get(0));

        return 0;
    }


    public boolean canFirstTurn(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException{
        if(getCurrentTurn()==playerID){
            //return map.canInitiateSettlement(playerID, vertex) && map.canInitiateRoad(playerID, edge, vertex);
        }
        return false;
    }

    public void firstTurn(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException{
        map.initiateSettlement(playerID, vertex);
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
    public boolean canDiscardCards(int playerID) throws PlayerExistException{

        return playerManager.canDiscardCards(playerID);
    }

    /**
     * Action - Player discards cards
     *
     * @param playerID ID of Player performing action
     * @param cards    Cards to be discarded
     */
    @Override
    public void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistException{
        playerManager.discardCards(playerID, cards);
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
        //check turntracker
        return false;
    }

    /**
     * Action - Player rolls the dice
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public int rollNumber(int playerID) throws InvalidDiceRollException{
        int roll = dice.roll();
        map.giveResources(roll);
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
        return false;
    }

    /**
     * Action - Player offers trade
     *
     * @param playerIDOne   ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    @Override
    public void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) {
        TradePackage one = new TradePackage(playerIDOne,onecards);
        TradePackage two = new TradePackage(playerIDTwo, twocards);
        Trade trade = new Trade(one,two);
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
        return false;
    }

    /**
     * Action - Player finishes their turn
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void finishTurn(int playerID) {

    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int playerID) throws PlayerExistException{

        return playerManager.canBuyDevCard(playerID);
    }

    /**
     * Action - Player buys a dev card
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void buyDevCard(int playerID) throws PlayerExistException{
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
    public boolean canUseYearOfPlenty(int playerID) throws PlayerExistException{
        if(getCurrentTurn() == playerID){
            //can use DC
            return playerManager.canUseYearOfPlenty(playerID);
        }
        return false;
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useYearOfPlenty(int playerID) throws PlayerExistException, DevCardException{
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
    public boolean canUseRoadBuilder(int playerID) throws PlayerExistException{
        //turnTracker.canUseDC(playerID);
        return playerManager.canUseRoadBuilder(playerID) ;
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useRoadBuilder(int playerID) throws PlayerExistException, DevCardException{
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
    public boolean canUseSoldier(int playerID) throws PlayerExistException{
        //turnTracker.canUseDC(playerID);
        return playerManager.canUseSoldier(playerID);
    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useSoldier(int playerID) throws PlayerExistException, DevCardException{
        if(canUseSoldier(playerID)){
            playerManager.useSoldier(playerID);
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
    public boolean canUseMonopoly(int playerID) throws PlayerExistException{
        //turnTracker.canUseDC(playerID);
        return playerManager.canUseMonopoly(playerID);
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonopoly(int playerID) throws PlayerExistException, DevCardException{
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
    public boolean canUseMonument(int playerID) throws PlayerExistException{
        //turnTracker.canUseDC(playerID);
        return playerManager.canUseMonument(playerID);
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(int playerID) throws PlayerExistException, DevCardException{
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
        //turnTracker.canPlaceRobber
        return false;
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

    public ResourceType rob(int playerrobber, int playerrobbed){
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
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistException {
       // if(turnTracker.canBuild(playerID)){
            return (map.canBuildRoad(playerID, edge) && playerManager.canBuildRoad(playerID));
       // }
       // return false;
    }

    /**
     * builds a road for hte player
     *
     * @param playerID
     */
    @Override
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistException {
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
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistException{
        return map.canBuildSettlement(playerID, vertex) && playerManager.canBuildSettlement(playerID); //&& turnTracker.canBuild(playerID);
    }

    /**
     * builds a settlement for this player
     *
     * @param playerID
     */
    @Override
    public void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistException {
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
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistException {

        return map.canBuildCity(playerID, vertex) && playerManager.canBuildCity(playerID); //&& turnTracker.canBuild(playerID);
    }

    /**
     * builds a city for this player
     *
     * @param playerID
     */
    @Override
    public void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistException {
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
    public boolean canBuyDevelopmentCard(int playerID) throws PlayerExistException {
        return playerManager.canBuyDevCard(playerID);
    }

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerID
     */
    @Override
    public DevCardType buyDevelopmentCard(int playerID) throws PlayerExistException {
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
        //turnTracker.canTrade(playerID);
        return false;
    }

    /**
     * checks if that player has the card needed for that port's trade
     *
     * @param playerID
     * @param port
     * @return
     */
    @Override
    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistException{
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
    public void maritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistException{
        if(canMaritimeTrade(playerID, port)){
            //playerManager.maritimeTrade(playerID, port));
        }
    }



    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException{
        return map.getPortTypes(playerID);
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
}