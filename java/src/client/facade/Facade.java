
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
import shared.model.game.TurnTracker;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.player.Player;
import shared.definitions.*;
import shared.exceptions.*;

import javax.naming.InsufficientResourcesException;
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
    private Set<CatanColor> available_colors = new HashSet<>();
    private static Facade _instance;

    /**
     * Constructor initializes map and game values
     */
    private Facade() {
        this.game = Game.getInstance();
    }


    public static Facade getInstance(){
        if(_instance == null){
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
        }catch(InvalidLocationException e){
            return false;
        }catch(InvalidPlayerException e){
            return false;
        }
    }

    //TODO talk to server
    public void initiateRoad(int playerID, EdgeLocation edge){
        try {
            edge = getServerEdgeLocation(edge);
            final BuildRoadDTO road = new BuildRoadDTO(playerID, edge, true);
            ServerProxy.getInstance().buildRoad(road);
        } catch(MissingUserCookieException e){}
    }

    public boolean canInitiateSettlement(int pID, VertexLocation vertex){
        try{
            return this.game.canInitiateSettlement(pID, vertex);
        }catch(InvalidLocationException e){
            return false;
        }catch(InvalidPlayerException e){
            return false;
        }
    }

    //TODO talk to server
    public void initiateSettlement(int pID, VertexLocation vertex){
        try {
            vertex = getServerVertexLocation(vertex);
            final BuildSettlementDTO set = new BuildSettlementDTO(pID, vertex, true);
            ServerProxy.getInstance().buildSettlement(set);
        } catch(MissingUserCookieException e){}
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

    //TODO talk to server
    public void finishTurn(int playerID){
        try {
            FinishTurnDTO finish = new FinishTurnDTO(playerID);
            ServerProxy.getInstance().finishTurn(finish);
        } catch(MissingUserCookieException e){}
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
     * @throws BuildException
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
     * @throws BuildException
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
     * @throws BuildException
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
     * @throws BuildException
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
     * @throws BuildException
     */
    //TODO talk with server
    public void tradeWithPlayer(int playerOneID, int playerTwoID, List<ResourceType> oneCards, List<ResourceType> twoCards) throws BuildException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerOneID >= 0;
        assert playerTwoID >= 0;
        assert playerOneID != playerTwoID;
        assert oneCards != null;
        assert oneCards.size() > 0;
        assert twoCards != null;
        assert twoCards.size() > 0;
        assert !oneCards.equals(twoCards);

        TradePackage one = new TradePackage(playerOneID, oneCards);
        TradePackage two = new TradePackage(playerTwoID, twoCards);
        Trade t = new Trade(one, two);
        try {
            final OfferTradeDTO trade = new OfferTradeDTO(playerOneID, t, playerTwoID);
            ServerProxy.getInstance().offerTrade(trade);
        } catch(MissingUserCookieException e) {}
    }

    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException {
        assert playerID >= 0;
        assert port != null;

        if (canTrade(playerID)) {
            Set<PortType> ports = this.game.getPortTypes(playerID);
            boolean cangame = this.game.canMaritimeTrade(playerID, port);
            return ports.contains(port) && cangame;
        }
        return false;
    }

    public Set<PortType> maritimeTradeOptions(int playerID) throws InvalidPlayerException {
        assert playerID >= 0;

        if (canTrade(playerID)) {
            return this.game.getPortTypes(playerID);
        }
        throw new InvalidPlayerException("can't trade");
    }

    //TODO talk to server
    public void maritimeTrade(int playerID, PortType port, ResourceType want, ResourceType give) throws BuildException, InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerID >= 0;
        assert want != null;
        int ratio = 2;

        if(port == PortType.THREE){ratio = 3;}
        else if(port == PortType.BRICK){assert give == ResourceType.BRICK;}
        else if(port == PortType.ORE){assert give == ResourceType.ORE;}
        else if(port == PortType.WHEAT){assert give == ResourceType.WHEAT;}
        else if(port == PortType.WOOD){assert give == ResourceType.WOOD;}
        else if(port == PortType.SHEEP){assert give == ResourceType.SHEEP;}

        try {
            MaritimeTradeDTO dto = new MaritimeTradeDTO(playerID, ratio, give.toString(), want.toString());
            ServerProxy.getInstance().maritimeTrade(dto);
        }catch(MissingUserCookieException e){}


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
        try {
            RobPlayerDTO dto = new RobPlayerDTO(playerID, victim.getPlayerIndex(), hexLoc);
            ServerProxy.getInstance().robPlayer(dto);
        }
        catch(MissingUserCookieException e){
            e.printStackTrace();
        }
    }

    public Set<Integer> playSoldier(int playerID, HexLocation hexloc, int robbed){
        try{

            if (this.game.canUseSoldier(playerID)) {
                RobPlayerDTO dto = new RobPlayerDTO(playerID, robbed, hexloc);
                ServerProxy.getInstance().robPlayer(dto);
            }
            return null;
        } catch(PlayerExistsException e){
            return null;
        } catch(MissingUserCookieException e){
            return null;
        }
    }

    //TODO flesh this puppy out
    public List<PlayerInfo> getPlayers() {
        List<Player> players = this.game.getPlayers();
        List<PlayerInfo> playerInfos = new ArrayList<>();

        int longestroad = this.game.currentLongestRoadPlayer();
        int largestarmy = this.game.currentLargestArmyPlayer();
        for(Player p: players){
            boolean lr = false;
            boolean la = false;
            if(longestroad == p.getId()){lr = true;}
            if(largestarmy == p.getId()){la = true;}
            PlayerInfo pi = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
            playerInfos.add(pi);
        }

        return playerInfos;
    }

    public PlayerInfo[] getOtherPlayers(int id){
        List<Player> players = this.game.getPlayers();
        PlayerInfo[] playerInfos = new PlayerInfo[3];

        int longestroad = this.game.currentLongestRoadPlayer();
        int largestarmy = this.game.currentLargestArmyPlayer();
        int i =0;
        for(Player p: players){
            if(p.getPlayerIndex() != id) {
                boolean lr = false;
                boolean la = false;
                if (longestroad == p.getPlayerIndex()) {
                    lr = true;
                }
                if (largestarmy == p.getPlayerIndex()) {
                    la = true;
                }
                PlayerInfo pi = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);
                playerInfos[i] =pi;
                i++;
            }
        }

        return playerInfos;
    }

    public int getGameId() {
        return this.game.getId();
    }

    public void setGameInfo(GameInfo game) {
        Game.getInstance().setId(game.getId());
        if(Game.getInstance().getPlayerManager().getPlayers().size() > 0) {
            for (int i = 0; i < 4; i++) {
                PlayerInfo info = game.getPlayers().get(i);
                Game.getInstance().getPlayerManager().getPlayers().get(i).setPlayerIndex(info.getPlayerIndex());
                // TODO -- add rest
            }
        } else {
            for(PlayerInfo info : game.getPlayers()) {
                int playerId = info.getId();
                int playerIndex = info.getPlayerIndex();
                String name = info.getName();
                CatanColor color = info.getColor();
                int points = info.getVictoryPoints();
                try {
                    Game.getInstance().getPlayerManager().addPlayer(new Player(points, color, playerId, playerIndex, name));
                } catch (InvalidPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getPlayerIndexByID(int playerId) throws PlayerExistsException {
        Player p = game.getPlayerById(playerId);
        return p.getPlayerIndex();
    }

    public PlayerInfo getWinner() throws GameOverException{
        Player p = this.game.getWinner();

        int longestroad = this.game.currentLongestRoadPlayer();
        int largestarmy = this.game.currentLargestArmyPlayer();

        boolean lr = false;
        boolean la = false;
        if(longestroad == p.getId()){lr = true;}
        if(largestarmy == p.getId()){la = true;}
        PlayerInfo pi = new PlayerInfo(p.getName().toString(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), lr, la);

        return pi;
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
        }catch(PlayerExistsException e){return false;}

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
            return this.game.canUseRoadBuilder(playerID);
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
        } catch(PlayerExistsException e){return 0;}
        catch(InvalidTypeException e) {return 0;}
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

    public int getVictoryPoints(int playerIndex) {
        try {
            return game.getPlayerByIndex(playerIndex).getVictoryPoints();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * plays the Development Card
     *
     * @param playerID
     * @param dc
     * @throws BuildException
     */
    /*public void playDC(int playerID, DevCardType dc, EdgeLocation edge1, EdgeLocation edge2) throws BuildException, PlayerExistsException, DevCardException {
        if (canPlayDC(playerID, dc)) {
            if(dc == DevCardType.SOLDIER){game.useSoldier(playerID);}
            else if(dc == DevCardType.MONUMENT){game.useMonument(playerID);}
            else if(dc == DevCardType.ROAD_BUILD){game.useRoadBuilder(playerID);}
            else if(dc == DevCardType.MONOPOLY){game.useMonopoly(playerID);}
            else if(dc == DevCardType.YEAR_OF_PLENTY){game.useYearOfPlenty(playerID);}
        } else {
            throw new BuildException("can't play this Develpment Card");
        }
    }*/
}