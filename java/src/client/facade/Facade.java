
package client.facade;
import org.omg.CORBA.DynAnyPackage.Invalid;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.game.Game;
import shared.model.game.IGame;
import shared.model.map.IMap;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.definitions.*;
import shared.model.player.Name;
import shared.exceptions.*;

import java.util.*;

/**
 * The Facade class handles all the communication
 * between the UI and game model.
 *
 * @author Corbin Byers
 */
public class Facade {

    private IMap map;
    private IGame game;
    private List<Player> players = new ArrayList<>();
    private HashMap<String, PlayerInfo> entries = new HashMap<>();
    private Set<CatanColor> available_colors = new HashSet<>();

    /**
     * Constructor initializes map and game values
     */
    public Facade() {
        //this.map = new Map();
        this.game = new Game();
    }

    public Set<CatanColor> canJoin() {
        if (entries.size() >= 4) {
            return null;
        } else {
            return available_colors;
        }
    }

    public void joinPlayer(PlayerInfo pi) throws BuildException {
        if (entries.size() >= 4) {
            throw new BuildException("too many players");
        } else {
            entries.put(pi.getUserName(), pi);
            available_colors.remove(pi.getColor());
        }
    }

    public void leaveQueue(PlayerInfo pi) throws BuildException {
        PlayerInfo removed = entries.remove(pi.getUserName());
        if (removed == null) {
            throw new BuildException("player didn't exist");
        } else {
            if (!available_colors.add(removed.getColor())) {
                throw new BuildException("couldn't re-add the color");
            }
        }
    }

    public boolean canStartGame() {
        return entries.size() == 4 || entries.size() == 3;
    }


    public void initializeGame() throws BuildException, InvalidNameException, InvalidPlayerException {
        if (entries.size() != 4 && entries.size() != 3) {
            throw new BuildException("need 3-4 players to play");
        } else {
            int id = 0;
            for (String currKey : entries.keySet()) {
                Name him = new Name(entries.get(currKey).getName());
                Player p = new Player(0, entries.get(currKey).getColor(), id, him);
                players.add(p);
                id++;
            }
            int firstPlayerID = this.game.initializeGame(players);
            //map stuff

        }
    }

    /**
     * Asks the game who then asks the turn tracker
     * if it's the player's turn.
     *
     * @param playerID The ID of the player asking this
     */
    public boolean myTurn(int playerID) {
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
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException {
        if (!myTurn(playerID)) {
            return false;
        } else {
            boolean cangame = game.canBuildRoad(playerID);
            boolean canmap = map.canBuildRoad(playerID, edge);

            return cangame && canmap;
        }
    }

    /**
     * Builds a road
     *
     * @param playerID
     * @param edge
     * @throws BuildException
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException {
        if (canBuildRoad(playerID, edge)) {
            game.buildRoad(playerID);
            map.buildRoad(playerID, edge);

            int player_road = map.getLongestRoadSize(playerID);
            //need to talk to longest road

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
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException {
        boolean cangame = game.canBuildSettlement(playerID);
        boolean canmap = map.canBuildSettlement(playerID, vertex);

        return cangame && canmap;
    }

    /**
     * Builds a building
     *
     * @param playerID
     * @param vertex
     * @throws BuildException
     */
    public void buildSettlement(int playerID, VertexLocation vertex) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException {
        if (canBuildSettlement(playerID, vertex)) {
            game.buildSettlement(playerID);
            map.buildSettlement(playerID, vertex);
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
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException {
        boolean cangame = game.canBuildCity(playerID);
        boolean canmap = map.canBuildCity(playerID, vertex);

        return cangame && canmap;
    }

    /**
     * Builds a building
     *
     * @param playerID
     * @param vertex
     * @throws BuildException
     */
    public void buildCity(int playerID, VertexLocation vertex) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException {
        if (canBuildCity(playerID, vertex)) {
            game.buildCity(playerID);
            map.buildCity(playerID, vertex);
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
    public boolean canBuyDC(int playerID) {
        return game.canBuyDevelopmentCard(playerID);
    }

    /**
     * player Buys a development card
     *
     * @param playerID
     * @throws BuildException
     */
    public DevCardType buyDC(int playerID) throws BuildException {
        if (canBuyDC(playerID)) {
            return game.buyDevelopmentCard(playerID);
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
    public void tradeWithPlayer(int playerOneID, int playerTwoID, List<ResourceType> oneCards, List<ResourceType> twoCards) throws BuildException {
        if (canTrade(playerOneID)) {
            game.tradePlayer(playerOneID, oneCards, playerTwoID, twoCards);
        } else {
            throw new BuildException("Can't complete this trade");
        }
    }

    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException {
        if (canTrade(playerID)) {
            Set<PortType> ports = map.getPortTypes(playerID);
            boolean cangame = game.canMaritimeTrade(playerID, port);
            return ports.contains(port) && cangame;
        }
        return false;
    }

    public Set<PortType> maritimeTradeOptions(int playerID) throws InvalidPlayerException {
        if (canTrade(playerID)) {
            return map.getPortTypes(playerID);
        }
        return null;
    }

    public void maritimeTrade(int playerID, PortType port) throws BuildException, InvalidPlayerException {
        if (!canMaritimeTrade(playerID, port)) {
            throw new BuildException("invalid maritime trade");
        } else {
            game.maritimeTrade(playerID, port);
        }
    }


    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(int playerID, DevCardType dc) {
        return myTurn(playerID) && game.canPlayDevelopmentCard(playerID, dc);
    }

    /**
     * plays the Development Card
     *
     * @param playerID
     * @param dc
     * @throws BuildException
     */
    public void playDC(int playerID, DevCardType dc) throws BuildException {
        if (canPlayDC(playerID, dc)) {
            game.playDevelopmentCard(playerID, dc);
        } else {
            throw new BuildException("can't play this Develpment Card");
        }
    }
}