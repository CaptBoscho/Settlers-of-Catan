
package client.facade;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import shared.dto.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;
import shared.model.game.IGame;
import shared.model.game.MessageList;
import shared.model.game.TurnTracker;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.player.Player;
import shared.definitions.*;
import shared.exceptions.*;
import shared.model.player.PlayerManager;

import java.util.*;

/**
 * The Facade class handles all the communication
 * between the UI and game model.
 *
 * @author Corbin Byers
 */
public class Facade {

    private IGame game;
    private List<Player> players = new ArrayList<>();
    private HashMap<String, ModelPlayerInfo> entries = new HashMap<>();
    private static Facade _instance;

    /**
     * Constructor initializes map and game values
     */
    private Facade() {
        this.game = new Game();
    }

    public static Facade getInstance(){
        if(_instance == null) {
            _instance = new Facade();
        }
        return _instance;
    }

    public void addObserver(Observer o){
        this.game.addObserver(o);
    }

    public CatanColor getPlayerColorByIndex(int id) throws PlayerExistsException {
        return this.game.getPlayerColorByIndex(id);
    }

    public boolean canInitiateRoad(int playerID, EdgeLocation edge){
        try{
            return this.game.canInitiateRoad(playerID,  edge);
        } catch(InvalidLocationException | InvalidPlayerException e) {
            return false;
        }
    }

    //TODO talk to server
    public void initiateRoad(int playerID, EdgeLocation edge){
        try {
            edge = getServerEdgeLocation(edge);
            final BuildRoadDTO road = new BuildRoadDTO(playerID, edge, true);
            ServerProxy.getInstance().buildRoad(road);
        } catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public boolean canInitiateSettlement(int playerIndex, VertexLocation vertex) {
        try {
            return this.game.canInitiateSettlement(playerIndex, vertex);
        } catch(InvalidLocationException | InvalidPlayerException e) {
            return false;
        }
    }

    //TODO talk to server
    public void initiateSettlement(int pID, VertexLocation vertex) {
        vertex = getServerVertexLocation(vertex);
        final BuildSettlementDTO set = new BuildSettlementDTO(pID, vertex, true);
        try {
            ServerProxy.getInstance().buildSettlement(set);
        } catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks the game who then asks the turn tracker
     * if it's the player's turn.
     *
     * @param playerID The ID of the player asking this
     */
    public boolean myTurn(int playerID) {
        assert playerID >= 0;

        return playerID == this.game.getCurrentTurn();
    }

    public Integer getCurrentTurn(){return this.game.getCurrentTurn();}

    public void finishTurn(int playerID){
        FinishTurnDTO finish = new FinishTurnDTO(playerID);
        try {
            ServerProxy.getInstance().finishTurn(finish);
        } catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public boolean canFinishTurn(int playerID){
        return this.game.canFinishTurn(playerID);
    }

    private HexLocation getServerHexLocation(HexLocation hexLoc){
        return new HexLocation(hexLoc.getX(), hexLoc.getY()-hexLoc.getX());
    }

    private EdgeLocation getServerEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getServerHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    private VertexLocation getServerVertexLocation(VertexLocation vertexLoc){
        return new VertexLocation(getServerHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }



    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources, then asks the map
     * class if that player can build a road at that location.
     *
     * @param playerID The ID of the player asking this
     * @param edge
     * @return A boolean indicating if the asking player can build a road
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, PlayerExistsException {
        assert playerID >= 0;
        assert edge != null;

        return myTurn(playerID) && this.game.canBuildRoad(playerID, edge);
    }

    /**
     * Builds a road
     *
     * @param playerID
     * @param edge
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws MissingUserCookieException {
        assert playerID >= 0;
        assert edge != null;

        edge = getServerEdgeLocation(edge);
        final BuildRoadDTO dto = new BuildRoadDTO(playerID, edge, false);
        ServerProxy.getInstance().buildRoad(dto);

    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources, then asks the map
     * class if that player can build a Building at that location.
     *
     * @param playerID The ID of the player asking this
     * @param vertex
     * @return A boolean indicating if the asking player can build a building
     */
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, PlayerExistsException {
        return this.game.canBuildSettlement(playerID, vertex);

    }

    /**
     * Builds a building
     *
     * @param playerID
     * @param vertex
     */
    public void buildSettlement(int playerID, VertexLocation vertex) throws MissingUserCookieException {
        vertex = getServerVertexLocation(vertex);
        final BuildSettlementDTO dto = new BuildSettlementDTO(playerID, vertex, false);
        ServerProxy.getInstance().buildSettlement(dto);
    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources, then asks the map
     * class if that player can build a Building at that location.
     *
     * @param playerID The ID of the player asking this
     * @param vertex
     * @return A boolean indicating if the asking player can build a building
     */
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, PlayerExistsException{
        return this.game.canBuildCity(playerID, vertex);

    }

    /**
     * Builds a building
     *
     * @param playerID
     * @param vertex
     */
    public void buildCity(int playerID, VertexLocation vertex) throws MissingUserCookieException {
        vertex = getServerVertexLocation(vertex);
        final BuildCityDTO dto = new BuildCityDTO(playerID, vertex);
        ServerProxy.getInstance().buildCity(dto);
    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources to buy a development
     * card.
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if the asking player can buy a development card
     */
    public boolean canBuyDC(int playerID) throws PlayerExistsException {
        assert playerID >= 0;

        return this.game.canBuyDevelopmentCard(playerID);
    }

    /**
     * player Buys a development card
     *
     * @param playerID
     */
    public void buyDC(int playerID) throws MissingUserCookieException {
        assert playerID >= 0;

        final BuyDevCardDTO dto = new BuyDevCardDTO(playerID);
        ServerProxy.getInstance().buyDevCard(dto);
    }

    /**
     * Facade asks the game who then asks the turn tracker if trading
     * is permitted for this player.
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if the asking player can trade
     */
    public boolean canTrade(int playerID) {
        return myTurn(playerID) && game.canTrade(playerID);
    }

    /**
     * Commits the trade
     *
     * @param playerOneID
     */
    //TODO talk with server
    public void tradeWithPlayer(int playerOneID, int playerTwoID, List<ResourceType> oneCards, List<ResourceType> twoCards) {
        assert playerOneID >= 0;
        assert playerTwoID >= 0;
        assert playerOneID != playerTwoID;
        assert oneCards != null;
        assert oneCards.size() > 0;
        assert twoCards != null;
        assert twoCards.size() > 0;
        assert !oneCards.equals(twoCards);


        final TradePackage one = new TradePackage(playerOneID, oneCards);
        final TradePackage two = new TradePackage(playerTwoID, twoCards);
        final Trade t = new Trade(one, two);
        final OfferTradeDTO trade = new OfferTradeDTO(playerOneID, t, playerTwoID);
        try {
            ServerProxy.getInstance().offerTrade(trade);
        } catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public void answerTrade(int playerIndex, boolean answer) {
        final TradeOfferResponseDTO dto = new TradeOfferResponseDTO(playerIndex, answer);
        try {
            ServerProxy.getInstance().respondToTradeOffer(dto);
        }catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public int getTradeReceiver() {
        return this.game.getTradeReceiver();
    }

    public boolean isTradeActive() {
        return this.game.isTradeActive();
    }

    public int getTradeSender() {
        return this.game.getTradeSender();
    }

    public int getTradeBrick() {
        return this.game.getTradeBrick();
    }

    public int getTradeWood() {
        return this.game.getTradeWood();
    }

    public int getTradeSheep() {
        return this.game.getTradeSheep();
    }

    public int getTradeWheat() {
        return this.game.getTradeWheat();
    }

    public int getTradeOre() {
        return this.game.getTradeOre();
    }

    public boolean canMaritimeTrade(int pIndex) {
        assert pIndex >= 0;
        return canTrade(pIndex);
    }

    public Set<PortType> maritimeTradeOptions(int playerID) throws InvalidPlayerException {
        assert playerID >= 0;

        if (canTrade(playerID)) {
            return this.game.getPortTypes(playerID);
        }
        throw new InvalidPlayerException("can't trade");
    }

    public void maritimeTrade(int pIndex, int ratio, ResourceType get, ResourceType give) {
        assert pIndex >= 0;
        assert ratio > 0;
        assert get != null;
        assert give != null;

        if(canMaritimeTrade(pIndex)){
            MaritimeTradeDTO dto = new MaritimeTradeDTO(pIndex, ratio, give.toString(), get.toString());
            try {
                ServerProxy.getInstance().maritimeTrade(dto);
            } catch(MissingUserCookieException e) {
                e.printStackTrace();
            }
        }
    }

    public shared.model.map.Map getMap(){return this.game.getMap();}

    public TurnTracker.Phase getPhase(){return this.game.getCurrentPhase();}

    public boolean ableToBuildRoad(int id) throws PlayerExistsException{
        return this.game.ableToBuildRoad(id);
    }

    public boolean ableToBuildSettlement(int id) throws PlayerExistsException{
        return this.game.ableToBuildSettlement(id);
    }

    public boolean ableToBuildCity(int id) throws PlayerExistsException{
        return this.game.ableToBuildCity(id);
    }

    public boolean ableToBuyDevCard(int id){
        try {
            return this.game.canBuyDevelopmentCard(id);
        }catch(PlayerExistsException e){ return false;}
    }

    public Integer getAvailableRoads(int id) throws PlayerExistsException{
        return this.game.getAvailableRoads(id);
    }

    public Integer getAvailableSettlements(int id) throws PlayerExistsException{
        return this.game.getAvailableSettlements(id);
    }

    public Integer getAvailableCities(int id) throws PlayerExistsException{
        return this.game.getAvailableCities(id);
    }

    public boolean canMoveRobber(int id, HexLocation hexloc) {
        return this.game.canPlaceRobber(id, hexloc);
    }

    public RobPlayerInfo[] moveRobber(int id, HexLocation hexloc){
        try{
            Set<Integer> ids = this.game.placeRobber(id, hexloc);
            List<Player> players = this.game.getPlayers();
            RobPlayerInfo[] robbed = new RobPlayerInfo[ids.size()];
            int i = 0;

            for(Player p: players){
                if(ids.contains(p.getPlayerIndex())){
                    RobPlayerInfo rbi = new RobPlayerInfo();
                    rbi.setColor(p.getColor());
                    rbi.setId(p.getId());
                    rbi.setName(p.getName());
                    rbi.setPlayerIndex(p.getPlayerIndex());
                    rbi.setNumCards(p.countResources());
                    robbed[i] = rbi;
                    i++;
                }
            }

            return robbed;
        } catch(AlreadyRobbedException | InvalidLocationException ignored) {

        }
        return null;
    }

    public void rob(int playerID, RobPlayerInfo victim, HexLocation hexLoc) {
        RobPlayerDTO dto;
        if(getNumberResourceCards(victim.getPlayerIndex()) == 0) {
            dto = new RobPlayerDTO(playerID, playerID, hexLoc);
        } else {
            dto = new RobPlayerDTO(playerID, victim.getPlayerIndex(), hexLoc);
        }
        try {
            ServerProxy.getInstance().robPlayer(dto);
        }
        catch(MissingUserCookieException e){
            e.printStackTrace();
        }
    }

    public void playSoldier(int playerIndex, HexLocation hexloc, int robbed){
        try{
            if (this.game.canUseSoldier(playerIndex)) {
                PlaySoldierCardDTO dto = new PlaySoldierCardDTO(playerIndex, robbed, hexloc);
                ServerProxy.getInstance().playSoldierCard(dto);
            }
        } catch(PlayerExistsException | MissingUserCookieException e){
            e.printStackTrace();
        }
    }

    //TODO flesh this puppy out
    public List<PlayerInfo> getPlayers() {
        List<Player> players = this.game.getPlayers();
        List<PlayerInfo> playerInfos = new ArrayList<>();

        int longestroad = this.game.getPlayerWithLongestRoad();
        int largestarmy = this.game.getPlayerWithLargestArmy();
        for(Player p: players){
            boolean lr = longestroad == p.getPlayerIndex();
            boolean la = largestarmy == p.getPlayerIndex();
            PlayerInfo pi = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
            playerInfos.add(pi);
        }

        return playerInfos;
    }

    public PlayerInfo[] getOtherPlayers(int id){
        List<Player> players = this.game.getPlayers();
        PlayerInfo[] playerInfos = new PlayerInfo[players.size()-1];

        int longestroad = this.game.getPlayerWithLongestRoad();
        int largestarmy = this.game.getPlayerWithLargestArmy();
        int i = 0;
        for(Player p: players){
            if(p.getPlayerIndex() != id) {
                boolean lr = longestroad == p.getPlayerIndex();
                boolean la = largestarmy == p.getPlayerIndex();
                PlayerInfo pi = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
                playerInfos[i] = pi;
                i++;
            }
        }

        return playerInfos;
    }

    public int getGameId() {
        return this.game.getId();
    }

    public void setGameInfo(GameInfo gameInfo) {
        gameInfo.setId(gameInfo.getId());
        if(game.getPlayerManager().getPlayers().size() > 0) {
            for (int i = 0; i < gameInfo.getPlayers().size(); i++) {
                PlayerInfo info = gameInfo.getPlayers().get(i);
                if(i >= gameInfo.getPlayers().size()) {
                    try {
                        game.getPlayerManager().addPlayer(new Player(info.getVictoryPoints(), info.getColor(), info.getId(), info.getPlayerIndex(), info.getName()));
                    } catch (InvalidPlayerException e) {
                        e.printStackTrace();
                    }
                } else {
                    game.getPlayerManager().getPlayers().get(i).setPlayerIndex(info.getPlayerIndex());
                }
                // TODO -- add rest
            }
        } else {
            for(PlayerInfo info : gameInfo.getPlayers()) {
                int playerId = info.getId();
                int playerIndex = info.getPlayerIndex();
                String name = info.getName();
                CatanColor color = info.getColor();
                int points = info.getVictoryPoints();
                try {
                    game.getPlayerManager().addPlayer(new Player(points, color, playerId, playerIndex, name));
                } catch (InvalidPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getPlayerIndexByID(int playerId) {
        Player p = null;
        try {
            p = game.getPlayerById(playerId);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        assert p != null;
        return p.getPlayerIndex();
    }

    public PlayerInfo getWinner() throws GameOverException {
        Player p = this.game.getWinner();

        int longestroad = this.game.getPlayerWithLongestRoad();
        int largestarmy = this.game.getPlayerWithLargestArmy();

        boolean lr = longestroad == p.getId();
        boolean la = largestarmy == p.getId();

        return new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
    }

    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(int playerID){
        assert playerID >= 0;
        try {
            int cards = this.game.numberOfDevCard(playerID);
            return cards > 0;
        } catch(PlayerExistsException e) {
            return false;
        }

    }

    public boolean canUseMonopoly(int playerID){
        try {
            return this.game.canUseMonopoly(playerID);
        }catch(PlayerExistsException e){return false;}
    }

    public boolean canPlaceRoadBuildingCard(int playerIndex, EdgeLocation edgeLoc){
        try {
            return this.game.canPlaceRoadBuildingCard(playerIndex, edgeLoc);
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canUseRoadBuilder(int playerID){
        try {
            return this.game.canUseRoadBuilding(playerID);
        }catch(PlayerExistsException e){return false;}
    }

    public boolean canUseMonument(int playerID){
        try{
            return this.game.canUseMonument(playerID);
        }catch(PlayerExistsException e){return false;}
    }

    public boolean canUseSoldier(int playerID){
        try{
            return this.game.canUseSoldier(playerID);
        }catch(PlayerExistsException e){return false;}
    }

    public boolean canUseYearOfPlenty(int playerID){
        try{
            return this.game.canUseYearOfPlenty(playerID);
        }catch(PlayerExistsException e){return false;}
    }


    public void buildFirstRoad(int playerID, EdgeLocation edgeloc){
        this.game.buildFirstRoad(playerID, edgeloc);
    }

    /**
     * DO NOT TALK TO SERVER ON THIS METHOD.
     * @param playerID
     * @param road
     */
    public void deleteRoad(int playerID, EdgeLocation road){
        this.game.deleteRoad(playerID, road);
    }


    public int getAmountOfResource(int playerID, ResourceType resource){
        try {
            return this.game.amountOwnedResource(playerID, resource);
        } catch(PlayerExistsException | InvalidTypeException e) {
            return 0;
        }
    }

    //TODO to server
    public void playRoadBuildingCard(int playerIndex, EdgeLocation one, EdgeLocation two){
        one = getServerEdgeLocation(one);
        two = getServerEdgeLocation(two);
        RoadBuildingDTO dto = new RoadBuildingDTO(playerIndex, one, two);
        try {
            ServerProxy.getInstance().playRoadBuildingCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the number of devCards of a given type owned by a given player
     * @param type The DevCardType to check
     * @param playerID the ID of the player to check
     * @return The number of DevelopmentCards of type 'type' owned by player of ID 'playerID'.
     */
    public int getNumberDevCards(DevCardType type, int playerID) {
        return game.getNumberDevCards(type, playerID);
    }

    public void playMonopolyCard(int index, ResourceType resource) {
        PlayMonopolyDTO dto = new PlayMonopolyDTO(index, resource.toString());
        try {
            ServerProxy.getInstance().playMonopolyCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public void playYearOfPlentyCard(int index, ResourceType resource1, ResourceType resource2) {
        PlayYOPCardDTO dto = new PlayYOPCardDTO(index, resource1, resource2);
        try {
            ServerProxy.getInstance().playYearOfPlentyCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public void playMonumentCard(int index) {
        PlayMonumentDTO dto = new PlayMonumentDTO(index);
        try {
            ServerProxy.getInstance().playMonumentCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public boolean canDiscard(int playerIndex) {
        try {
            return this.game.canDiscardCards(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getNumberResourceCards(int playerIndex) {
        try {
            return this.game.getNumberResourceCards(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void discard(int playerIndex, int brick, int ore, int sheep, int wheat, int wood) {
        DiscardCardsDTO dto = new DiscardCardsDTO(playerIndex, brick, ore, sheep, wheat, wood);
        try {
            ServerProxy.getInstance().discardCards(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfSoldiers(int playerIndex) {
        return game.getNumberOfSoldiers(playerIndex);
    }

    public boolean hasDiscarded(int playerIndex) {
        return this.game.hasDiscarded(playerIndex);
    }

    public MessageList getLog() {
        return this.game.getLog();
    }

    public CatanColor getPlayerColorByName(String player) {
        return this.game.getPlayerColorByName(player);
    }

    /**
     * returns the Hashmap where the key is the resourcetype
     * and the Integer is how many of that resource the bank
     * has.
     * @return
     */
    public HashMap<ResourceType, Integer> getBankResources(){
        return this.game.getBankResources();
    }

    /**
     * returns the Hashmap where the key is the resourcetype
     * and the Integer is how many of that resource the Player
     * bank has.
     * @return
     */
    public HashMap<ResourceType, Integer> getPlayerResources(int pIndex) throws PlayerExistsException {
        return this.game.getPlayerResources(pIndex);
    }

    public int getVictoryPoints(int pIndex) {
        PlayerManager pm = game.getPlayerManager();
        try {
            Player player = pm.getPlayerByIndex(pIndex);
            return player.getVictoryPoints();
        } catch (PlayerExistsException e) {
            return -1;
        }
    }

    public void sendChat(int playerIndex, String message) {
        SendChatDTO dto = new SendChatDTO(playerIndex, message);
        try {
            ServerProxy.getInstance().sendChat(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    public MessageList getChat() {
        return this.game.getChat();
    }

    public int getPoints(int playerIndex) {
        assert playerIndex >=0;
        assert playerIndex <=3;

        try {
            return this.game.getPoints(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getWinnerId() {
        try {
            return this.game.getWinner().getId();
        } catch (GameOverException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getPlayerNameByIndex(int playerIndex) {
        try {
            return this.game.getPlayerNameByIndex(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPlayerIdByIndex(int playerIndex) {
        try {
            return game.getPlayerIdByIndex(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int hasLargestRoad() {
        return this.game.getPlayerWithLargestArmy();
    }

    public int hasLongestRoad() {
        return this.game.getPlayerWithLongestRoad();
    }

    public IGame getGame(){
        return this.game;
    }
}