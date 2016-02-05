package shared.model.game;

import org.omg.CORBA.DynAnyPackage.Invalid;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
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
import shared.model.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
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
    public int initializeGame(List<Player> players, boolean randomhexes, boolean randomchits, boolean randomports) throws FailedToRandomizeException{
        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        this.map = new Map(randomhexes, randomchits, randomports);
        //List<Integer> order = this.playerManager.randomizePlayers();
        turnTracker = new TurnTracker(players.get(0).getPlayerIndex());
        turnTracker.setNumPlayers(players.size());

        return players.get(0).getPlayerIndex();
    }



    public boolean canFirstTurn(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException{
        if(turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase()){
            return map.canInitiateSettlement(playerID, vertex) && map.canInitiateRoad(playerID, edge, vertex);
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
        if(turnTracker.getPhase() == TurnTracker.Phase.DISCARDING)
        {
            return playerManager.canDiscardCards(playerID);
        }
        return false;
    }

    /**
     * Action - Player discards cards
     *
     * @param playerID ID of Player performing action
     * @param cards    Cards to be discarded
     */
    @Override
    public void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistException, InsufficientResourcesException, InvalidTypeException{
        if(canDiscardCards(playerID)){
            playerManager.discardCards(playerID, cards);
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
        if(turnTracker.isPlayersTurn(playerID))
        {
            return turnTracker.canRoll();
        }

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
        map.getResources(roll);
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
     * Action - Player offers trade
     *
     * @param playerIDOne   ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    @Override
    public void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) {
        if(canOfferTrade(playerIDOne)){
            TradePackage one = new TradePackage(playerIDOne,onecards);
            TradePackage two = new TradePackage(playerIDTwo, twocards);
            Trade trade = new Trade(one,two);
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

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int playerID) throws PlayerExistException{

        return playerManager.canBuyDevCard(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canDiscard();
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
        return playerManager.canUseRoadBuilder(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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

        return playerManager.canUseSoldier(playerID)  && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
    public boolean canUseMonopoly(int playerID) throws PlayerExistException{
        return playerManager.canUseMonopoly(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
        return playerManager.canUseMonument(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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

    public ResourceType rob(int playerrobber, int playerrobbed) throws MoveRobberException, InvalidTypeException, PlayerExistException, InsufficientResourcesException{
        if(canPlaceRobber(playerrobber)){
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
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistException {
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
        return map.canBuildSettlement(playerID, vertex) && playerManager.canBuildSettlement(playerID) && turnTracker.canDiscard(); //&& turnTracker.canBuild(playerID);
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

        return map.canBuildCity(playerID, vertex) && playerManager.canBuildCity(playerID) && turnTracker.canDiscard(); //&& turnTracker.canBuild(playerID);
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
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistException, InvalidTypeException, InsufficientResourcesException{
        if(canMaritimeTrade(playerID, port)){
            List<ResourceType> cards = new ArrayList<ResourceType>();
            if(port == PortType.BRICK){
                cards.add(ResourceType.BRICK);
                cards.add(ResourceType.BRICK);
            }
            else if(port == PortType.ORE){
                cards.add(ResourceType.ORE);
                cards.add(ResourceType.ORE);
            }
            else if(port == PortType.SHEEP){
                cards.add(ResourceType.SHEEP);
                cards.add(ResourceType.SHEEP);
            }
            else if(port == PortType.WHEAT){
                cards.add(ResourceType.WHEAT);
                cards.add(ResourceType.WHEAT);
            }
            else if(port == PortType.WOOD){
                cards.add(ResourceType.WOOD);
                cards.add(ResourceType.WOOD);
            }
            List<ResourceCard> discarded = playerManager.discardCards(playerID, cards);

            for(ResourceCard rc: discarded)
            {
                resourceCardBank.addResource(rc);
            }

            playerManager.addResource(playerID, resourceCardBank.discard(want));
        }
    }

    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistException, InsufficientResourcesException, InvalidTypeException{
        if(canMaritimeTrade(playerID, port)){
            if(port == PortType.THREE){
                List<ResourceType> cards = new ArrayList<ResourceType>();
                cards.add(give);
                cards.add(give);
                cards.add(give);

                List<ResourceCard> discarded = playerManager.discardCards(playerID, cards);

                for(ResourceCard rc: discarded)
                {
                    resourceCardBank.addResource(rc);
                }

                playerManager.addResource(playerID, resourceCardBank.discard(want));

            } else{
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