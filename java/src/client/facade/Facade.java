
package client.facade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;
import shared.model.game.IGame;
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

    /**
     * Constructor initializes map and game values
     */
    public Facade() {
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


    public void initializeGame(boolean randomhex, boolean randomchit, boolean randomport) throws BuildException, InvalidNameException, InvalidPlayerException, FailedToRandomizeException {
        if (entries.size() != 4 && entries.size() != 3) {
            throw new BuildException("need 3-4 players to play");
        } else {
            int id = 1;
            for (String currKey : entries.keySet()) {
                Name him = new Name(entries.get(currKey).getName());
                Player p = new Player(0, entries.get(currKey).getColor(), id, him);
                players.add(p);
                id++;
            }
            int firstPlayerID = this.game.initializeGame(players, randomhex, randomchit, randomport);


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
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, PlayerExistsException {
        return myTurn(playerID) && game.canBuildRoad(playerID, edge);
    }

    /**
     * Builds a road
     *
     * @param playerID
     * @param edge
     * @throws BuildException
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws BuildException, InvalidLocationException, StructureException, InvalidPlayerException, PlayerExistsException {
        if (canBuildRoad(playerID, edge)) {
            game.buildRoad(playerID, edge);

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
        return game.canBuildSettlement(playerID, vertex);

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
            game.buildSettlement(playerID, vertex);
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
        return game.canBuildCity(playerID, vertex);

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
            game.buildCity(playerID, vertex);
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
        return game.canBuyDevelopmentCard(playerID);
    }

    /**
     * player Buys a development card
     *
     * @param playerID
     * @throws BuildException
     */
    public DevCardType buyDC(int playerID) throws BuildException, PlayerExistsException, Exception {
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
    public void tradeWithPlayer(int playerOneID, int playerTwoID, List<ResourceType> oneCards, List<ResourceType> twoCards) throws BuildException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        if (canTrade(playerOneID)) {
            game.offerTrade(playerOneID, playerTwoID, oneCards,  twoCards);
        } else {
            throw new BuildException("Can't complete this trade");
        }
    }

    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException{
        if (canTrade(playerID)) {
            Set<PortType> ports = game.getPortTypes(playerID);
            boolean cangame = game.canMaritimeTrade(playerID, port);
            return ports.contains(port) && cangame;
        }
        return false;
    }

    public Set<PortType> maritimeTradeOptions(int playerID) throws InvalidPlayerException {
        if (canTrade(playerID)) {
            return game.getPortTypes(playerID);
        }
        throw new InvalidPlayerException("can't trade");
    }

    public void maritimeTrade(int playerID, PortType port, ResourceType type) throws BuildException, InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        if (!canMaritimeTrade(playerID, port)) {
            throw new BuildException("invalid maritime trade");
        } else {
            game.maritimeTrade(playerID, port, type);
        }
    }


    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(int playerID, DevCardType dc) throws PlayerExistsException{
        if(myTurn(playerID)){
            if(dc == DevCardType.SOLDIER){return game.canUseSoldier(playerID);}
            if(dc == DevCardType.MONUMENT){return game.canUseMonument(playerID);}
            if(dc == DevCardType.ROAD_BUILD){return game.canUseRoadBuilder(playerID);}
            if(dc == DevCardType.MONOPOLY){return game.canUseMonopoly(playerID);}
            if(dc == DevCardType.YEAR_OF_PLENTY){return game.canUseYearOfPlenty(playerID);}
        }
        return false;
    }

    /**
     * plays the Development Card
     *
     * @param playerID
     * @param dc
     * @throws BuildException
     */
    public void playDC(int playerID, DevCardType dc) throws BuildException, PlayerExistsException, DevCardException {
        if (canPlayDC(playerID, dc)) {
            if(dc == DevCardType.SOLDIER){game.useSoldier(playerID);}
            else if(dc == DevCardType.MONUMENT){game.useMonument(playerID);}
            else if(dc == DevCardType.ROAD_BUILD){game.useRoadBuilder(playerID);}
            else if(dc == DevCardType.MONOPOLY){game.useMonopoly(playerID);}
            else if(dc == DevCardType.YEAR_OF_PLENTY){game.useYearOfPlenty(playerID);}
        } else {
            throw new BuildException("can't play this Develpment Card");
        }
    }
}