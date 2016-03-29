
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

import java.util.*;

/**
 * The Facade class handles all the communication
 * between the UI and game model.
 *
 * @author Corbin Byers
 */
public class Facade {

    // TODO - validate that all the "id" variables aren't actual "index"

    private IGame game;
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

    /**
     *
     * @param o
     */
    public void addObserver(Observer o){
        this.game.addObserver(o);
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public CatanColor getPlayerColorByIndex(final int id) throws PlayerExistsException {
        return this.game.getPlayerColorByIndex(id);
    }

    /**
     *
     * @param playerID
     * @param edge
     * @return
     */
    public boolean canInitiateRoad(final int playerID, final EdgeLocation edge) {
        try{
            return this.game.canInitiateRoad(playerID,  edge);
        } catch(InvalidLocationException | InvalidPlayerException e) {
            return false;
        }
    }

    //TODO talk to server
    public void initiateRoad(final int playerID, EdgeLocation edge) {
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
    private boolean myTurn(int playerID) {
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

    /**
     *
     * @param playerIndex
     * @param answer
     */
    public void answerTrade(final int playerIndex, final boolean answer) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final TradeOfferResponseDTO dto = new TradeOfferResponseDTO(playerIndex, answer);
        try {
            ServerProxy.getInstance().respondToTradeOffer(dto);
        }catch(MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public int getTradeReceiver() { return this.game.getTradeReceiver(); }

    /**
     *
     * @return
     */
    public boolean isTradeActive() {
        return this.game.isTradeActive();
    }

    /**
     *
     * @return
     */
    public int getTradeSender() { return this.game.getTradeSender(); }

    /**
     *
     * @return
     */
    public int getTradeBrick() {
        return this.game.getTradeBrick();
    }

    /**
     *
     * @return
     */
    public int getTradeWood() {
        return this.game.getTradeWood();
    }

    /**
     *
     * @return
     */
    public int getTradeSheep() {
        return this.game.getTradeSheep();
    }

    /**
     *
     * @return
     */
    public int getTradeWheat() {
        return this.game.getTradeWheat();
    }

    /**
     *
     * @return
     */
    public int getTradeOre() {
        return this.game.getTradeOre();
    }

    /**
     *
     * @param pIndex
     * @return
     */
    public boolean canMaritimeTrade(final int pIndex) {
        assert pIndex >= 0;
        assert pIndex < 4;

        return canTrade(pIndex);
    }

    /**
     *
     * @param playerID
     * @return
     * @throws InvalidPlayerException
     */
    public Set<PortType> maritimeTradeOptions(final int playerID) throws InvalidPlayerException {
        assert playerID >= 0;

        if (canTrade(playerID)) {
            return this.game.getPortTypes(playerID);
        }
        throw new InvalidPlayerException("can't trade");
    }

    /**
     *
     * @param pIndex
     * @param ratio
     * @param get
     * @param give
     */
    public void maritimeTrade(final int pIndex, final int ratio, final ResourceType get, final ResourceType give) {
        assert pIndex >= 0;
        assert ratio > 0;
        assert get != null;
        assert give != null;

        if(canMaritimeTrade(pIndex)){
            final MaritimeTradeDTO dto = new MaritimeTradeDTO(pIndex, ratio, give.toString(), get.toString());
            try {
                ServerProxy.getInstance().maritimeTrade(dto);
            } catch(MissingUserCookieException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return
     */
    public shared.model.map.Map getMap() {
        return this.game.getMap();
    }

    /**
     *
     * @return
     */
    public TurnTracker.Phase getPhase() {
        return this.game.getCurrentPhase();
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public boolean ableToBuildRoad(final int id) throws PlayerExistsException{
        return this.game.ableToBuildRoad(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public boolean ableToBuildSettlement(int id) throws PlayerExistsException{
        return this.game.ableToBuildSettlement(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public boolean ableToBuildCity(int id) throws PlayerExistsException{
        return this.game.ableToBuildCity(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean ableToBuyDevCard(int id){
        try {
            return this.game.canBuyDevelopmentCard(id);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public Integer getAvailableRoads(final int id) throws PlayerExistsException{
        return this.game.getAvailableRoads(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public int getAvailableSettlements(final int id) throws PlayerExistsException{
        return this.game.getAvailableSettlements(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    public int getAvailableCities(final int id) throws PlayerExistsException{
        return this.game.getAvailableCities(id);
    }

    /**
     *
     * @param id
     * @param hexloc
     * @return
     */
    public boolean canMoveRobber(final int id, final HexLocation hexloc) {
        return this.game.canPlaceRobber(id, hexloc);
    }

    /**
     *
     * @param id
     * @param hexloc
     * @return
     */
    public RobPlayerInfo[] moveRobber(final int id, final HexLocation hexloc){
        try {
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
        } catch(AlreadyRobbedException | InvalidLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param playerID
     * @param victim
     * @param hexLoc
     */
    public void rob(final int playerID, final RobPlayerInfo victim, final HexLocation hexLoc) {
        assert playerID >= 0;
        assert victim != null;
        assert hexLoc != null;

        RobPlayerDTO dto;
        if(getNumberResourceCards(victim.getPlayerIndex()) == 0) {
            dto = new RobPlayerDTO(playerID, playerID, hexLoc);
        } else {
            dto = new RobPlayerDTO(playerID, victim.getPlayerIndex(), hexLoc);
        }
        try {
            ServerProxy.getInstance().robPlayer(dto);
        } catch(MissingUserCookieException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param playerIndex
     * @param hexloc
     * @param robbed
     */
    public void playSoldier(final int playerIndex, final HexLocation hexloc, final int robbed) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert hexloc != null;

        try {
            if (this.game.canUseSoldier(playerIndex)) {
                final PlaySoldierCardDTO dto = new PlaySoldierCardDTO(playerIndex, robbed, hexloc);
                ServerProxy.getInstance().playSoldierCard(dto);
            }
        } catch(PlayerExistsException | MissingUserCookieException e){
            e.printStackTrace();
        }
    }

    //TODO flesh this puppy out
    public List<PlayerInfo> getPlayers() {
        final List<Player> players = this.game.getPlayers();
        final List<PlayerInfo> playerInfos = new ArrayList<>();

        int longestroad = this.game.getPlayerWithLongestRoad();
        int largestarmy = this.game.getPlayerWithLargestArmy();
        for(final Player p: players){
            boolean lr = longestroad == p.getPlayerIndex();
            boolean la = largestarmy == p.getPlayerIndex();
            final PlayerInfo pi = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
            playerInfos.add(pi);
        }

        return playerInfos;
    }

    /**
     *
     * @param id
     * @return
     */
    public PlayerInfo[] getOtherPlayers(final int id) {
        assert id >= 0;

        final List<Player> players = this.game.getPlayers();
        final PlayerInfo[] playerInfos = new PlayerInfo[players.size()-1];

        final int longestroad = this.game.getPlayerWithLongestRoad();
        final int largestarmy = this.game.getPlayerWithLargestArmy();
        int i = 0;
        for (Player p: players){
            if (p.getPlayerIndex() != id) {
                boolean lr = longestroad == p.getPlayerIndex();
                boolean la = largestarmy == p.getPlayerIndex();
                playerInfos[i] = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
                i++;
            }
        }

        return playerInfos;
    }

    public int getGameId() {
        return this.game.getId();
    }

    public void setGameInfo(GameInfo gameInfo) {
        game.setId(gameInfo.getId());
        if (game.getPlayerManager().getPlayers().size() > 0) {
            for (int i = 0; i < gameInfo.getPlayers().size(); i++) {
                final PlayerInfo info = gameInfo.getPlayers().get(i);
                if (i >= gameInfo.getPlayers().size()) {
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
            for (final PlayerInfo info : gameInfo.getPlayers()) {
                final int playerId = info.getId();
                final int playerIndex = info.getPlayerIndex();
                final String name = info.getName();
                final CatanColor color = info.getColor();
                final int points = info.getVictoryPoints();
                try {
                    game.getPlayerManager().addPlayer(new Player(points, color, playerId, playerIndex, name));
                } catch (InvalidPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param playerId
     * @return
     */
    public int getPlayerIndexByID(final int playerId) {
        Player p = null;
        try {
            p = game.getPlayerById(playerId);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        assert p != null;
        return p.getPlayerIndex();
    }

    /**
     *
     * @return
     * @throws GameOverException
     */
    public PlayerInfo getWinner() throws GameOverException {
        final Player p = this.game.getWinner();

        final int longestroad = this.game.getPlayerWithLongestRoad();
        final int largestarmy = this.game.getPlayerWithLargestArmy();

        final boolean lr = longestroad == p.getId();
        final boolean la = largestarmy == p.getId();

        return new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
    }

    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(final int playerID){
        assert playerID >= 0;
        try {
            return this.game.numberOfDevCard(playerID) > 0;
        } catch(PlayerExistsException e) {
            return false;
        }

    }

    /**
     *
     * @param playerID
     * @return
     */
    public boolean canUseMonopoly(final int playerID){
        try {
            return this.game.canUseMonopoly(playerID);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param playerIndex
     * @param edgeLoc
     * @return
     */
    public boolean canPlaceRoadBuildingCard(final int playerIndex, final EdgeLocation edgeLoc){
        try {
            return this.game.canPlaceRoadBuildingCard(playerIndex, edgeLoc);
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canUseRoadBuilder(final int playerID){
        try {
            return this.game.canUseRoadBuilding(playerID);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean canUseMonument(final int playerID) {
        assert playerID >= 0;
        try{
            return this.game.canUseMonument(playerID);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean canUseSoldier(final int playerID) {
        assert playerID >= 0;

        try{
            return this.game.canUseSoldier(playerID);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param playerID
     * @return
     */
    public boolean canUseYearOfPlenty(final int playerID) {
        assert playerID >= 0;

        try{
            return this.game.canUseYearOfPlenty(playerID);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param playerID
     * @param edgeloc
     */
    public void buildFirstRoad(final int playerID, final EdgeLocation edgeloc) {
        assert playerID >= 0;
        assert edgeloc != null;

        this.game.buildFirstRoad(playerID, edgeloc);
    }

    /**
     * DO NOT TALK TO SERVER ON THIS METHOD.
     * @param playerID
     * @param road
     */
    public void deleteRoad(final int playerID, final EdgeLocation road) {
        assert playerID >= 0;
        assert road != null;

        this.game.deleteRoad(playerID, road);
    }

    /**
     *
     * @param playerID
     * @param resource
     * @return
     */
    public int getAmountOfResource(final int playerID, final ResourceType resource) {
        assert playerID >= 0;
        assert resource != null;

        try {
            return this.game.amountOwnedResource(playerID, resource);
        } catch(PlayerExistsException | InvalidTypeException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //TODO to server
    public void playRoadBuildingCard(final int playerIndex, EdgeLocation one, EdgeLocation two) {
        assert playerIndex >= 0;
        assert  playerIndex < 4;
        assert one != null;
        assert two != null;

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
    public int getNumberDevCards(final DevCardType type, final int playerID) {
        assert type != null;
        assert playerID >= 0;
        return game.getNumberDevCards(type, playerID);
    }

    /**
     *
     * @param index
     * @param resource
     */
    public void playMonopolyCard(final int index, final ResourceType resource) {
        assert index >= 0;
        assert index < 4;
        assert resource != null;

        final PlayMonopolyDTO dto = new PlayMonopolyDTO(index, resource.toString());
        try {
            ServerProxy.getInstance().playMonopolyCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param index
     * @param resource1
     * @param resource2
     */
    public void playYearOfPlentyCard(final int index, final ResourceType resource1, final ResourceType resource2) {
        assert index >= 0;
        assert index < 4;
        assert resource1 != null;
        assert resource2 != null;

        final PlayYOPCardDTO dto = new PlayYOPCardDTO(index, resource1, resource2);
        try {
            ServerProxy.getInstance().playYearOfPlentyCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param index
     */
    public void playMonumentCard(final int index) {
        assert index >= 0;
        assert index < 4;

        final PlayMonumentDTO dto = new PlayMonumentDTO(index);
        try {
            ServerProxy.getInstance().playMonumentCard(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public boolean canDiscard(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            return this.game.canDiscardCards(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public int getNumberResourceCards(final int playerIndex) {
        try {
            return this.game.getNumberResourceCards(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param playerIndex
     * @param brick
     * @param ore
     * @param sheep
     * @param wheat
     * @param wood
     */
    public void discard(int playerIndex, int brick, int ore, int sheep, int wheat, int wood) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final DiscardCardsDTO dto = new DiscardCardsDTO(playerIndex, brick, ore, sheep, wheat, wood);
        try {
            ServerProxy.getInstance().discardCards(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public int getNumberOfSoldiers(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return game.getNumberOfSoldiers(playerIndex);
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public boolean hasDiscarded(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return this.game.hasDiscarded(playerIndex);
    }

    /**
     *
     * @return
     */
    public MessageList getLog() {
        return this.game.getLog();
    }

    /**
     *
     * @param player
     * @return
     */
    public CatanColor getPlayerColorByName(final String player) {
        assert player != null;

        return this.game.getPlayerColorByName(player);
    }

    /**
     * returns the Hashmap where the key is the resourcetype
     * and the Integer is how many of that resource the bank
     * has.
     * @return
     */
    public Map<ResourceType, Integer> getBankResources(){
        return this.game.getBankResources();
    }

    /**
     * returns the Hashmap where the key is the resourcetype
     * and the Integer is how many of that resource the Player
     * bank has.
     * @return
     */
    public Map<ResourceType, Integer> getPlayerResources(final int pIndex) throws PlayerExistsException {
        assert pIndex >= 0;
        assert pIndex < 4;

        return this.game.getPlayerResources(pIndex);
    }

    /**
     *
     * @param playerIndex
     * @param message
     */
    public void sendChat(final int playerIndex, final String message) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert message != null;

        final SendChatDTO dto = new SendChatDTO(playerIndex, message);
        try {
            ServerProxy.getInstance().sendChat(dto);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public MessageList getChat() {
        return this.game.getChat();
    }

    /**
     *
     * @param playerIndex
     * @return
     */
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

    /**
     *
     * @return
     */
    public int getWinnerId() {
        try {
            if (game.getWinner() != null) {
                return this.game.getWinner().getId();
            }
        } catch (GameOverException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public String getPlayerNameByIndex(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            return this.game.getPlayerNameByIndex(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param playerIndex
     * @return
     */
    public int getPlayerIdByIndex(final int playerIndex) {
        assert playerIndex >= 0;
        assert  playerIndex < 4;

        try {
            return game.getPlayerIdByIndex(playerIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public IGame getGame(){
        return this.game;
    }
}