
package client.facade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;
import shared.model.game.IGame;
import shared.model.game.TurnTracker;
import shared.model.map.*;
import shared.model.player.Player;
import shared.definitions.*;
import shared.model.player.Name;
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
    private HashMap<String, PlayerInfo> entries = new HashMap<>();
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

    public Set<CatanColor> canJoin() {
        if (this.entries.size() >= 4) {
            return null;
        } else {
            return this.available_colors;
        }
    }

    public void addObserver(Observer o){
        this.game.addObserver(o);
    }

    public CatanColor getPlayerColorByID(int id) throws PlayerExistsException{
        return this.game.getPlayerColorByID(id);}

    public void joinPlayer(PlayerInfo pi) throws BuildException {
        assert pi != null;

        if (this.entries.size() >= 4) {
            throw new BuildException("too many players");
        } else {
            this.entries.put(pi.getUserName(), pi);
            this.available_colors.remove(pi.getColor());
        }
    }

    public void leaveQueue(PlayerInfo pi) throws BuildException {
        assert pi != null;

        PlayerInfo removed = this.entries.remove(pi.getUserName());
        if (removed == null) {
            throw new BuildException("player didn't exist");
        } else {
            if (!this.available_colors.add(removed.getColor())) {
                throw new BuildException("couldn't re-add the color");
            }
        }
    }

    public boolean canStartGame() {
        return entries.size() == 4 || entries.size() == 3;
    }


    public void initializeGame(boolean randomhex, boolean randomchit, boolean randomport) throws BuildException, InvalidNameException, InvalidPlayerException, FailedToRandomizeException {
        if (this.entries.size() != 4 && this.entries.size() != 3) {
            throw new BuildException("need 3-4 players to play");
        } else {
            int id = 1;
            for (String currKey : this.entries.keySet()) {
                Name him = new Name(this.entries.get(currKey).getName());
                Player p = new Player(0, this.entries.get(currKey).getColor(), id, him);
                this.players.add(p);
                id++;
            }
            int firstPlayerID = this.game.initializeGame(this.players, randomhex, randomchit, randomport);


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

        int turn = this.game.getCurrentTurn();
        return playerID == turn;
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
    public void buildRoad(int playerID, EdgeLocation edge) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException, PlayerExistsException {
        assert playerID >= 0;
        assert edge != null;

        if (canBuildRoad(playerID, edge)) {
            this.game.buildRoad(playerID, edge);
        } else {
            throw new BuildException("Can't build the road");
        }
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
    public void buildSettlement(int playerID, VertexLocation vertex) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException, PlayerExistsException {
        if (canBuildSettlement(playerID, vertex)) {
            this.game.buildSettlement(playerID, vertex);
        } else {
            throw new BuildException("Can't build the building");
        }
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
    public void buildCity(int playerID, VertexLocation vertex) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException, PlayerExistsException {
        if (canBuildCity(playerID, vertex)) {
            this.game.buildCity(playerID, vertex);
        } else {
            throw new BuildException("Can't build the building");
        }
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
    public DevCardType buyDC(int playerID) throws Exception {
        assert playerID >= 0;

        if (canBuyDC(playerID)) {
            return this.game.buyDevelopmentCard(playerID);
        } else {
            throw new BuildException("Can't buy Develpment Card");
        }
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
    public void tradeWithPlayer(int playerOneID, int playerTwoID, List<ResourceType> oneCards, List<ResourceType> twoCards) throws BuildException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerOneID >= 0;
        assert playerTwoID >= 0;
        assert playerOneID != playerTwoID;
        assert oneCards != null;
        assert oneCards.size() > 0;
        assert twoCards != null;
        assert twoCards.size() > 0;
        assert !oneCards.equals(twoCards);

        if (canTrade(playerOneID)) {
            this.game.offerTrade(playerOneID, playerTwoID, oneCards,  twoCards);
        } else {
            throw new BuildException("Can't complete this trade");
        }
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

    public void maritimeTrade(int playerID, PortType port, ResourceType type) throws BuildException, InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerID >= 0;
        assert type != null;

        if (!canMaritimeTrade(playerID, port)) {
            throw new BuildException("invalid maritime trade");
        } else {
            this.game.maritimeTrade(playerID, port, type);
        }
    }


    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(int playerID, DevCardType dc) throws PlayerExistsException {
        assert playerID >= 0;
        assert dc != null;

        if(myTurn(playerID)){
            if(dc == DevCardType.SOLDIER){return this.game.canUseSoldier(playerID);}
            if(dc == DevCardType.MONUMENT){return this.game.canUseMonument(playerID);}
            if(dc == DevCardType.ROAD_BUILD){return this.game.canUseRoadBuilder(playerID);}
            if(dc == DevCardType.MONOPOLY){return this.game.canUseMonopoly(playerID);}
            if(dc == DevCardType.YEAR_OF_PLENTY){return this.game.canUseYearOfPlenty(playerID);}
        }
        return false;
    }

    public shared.model.map.Map getMap(){return this.game.getMap();}

    public TurnTracker.Phase getPhase(){return this.game.getCurrentPhase();}

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