package shared.facade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.map.Map;

/**
 * The Facade class handles all the communication
 * between the UI and game model.
 *
 * @author Corbin Byers
 */
public class Facade {

    private Map map;
    private Game game;

    /**
     * Asks the game who then asks the turn tracker
     * if it's the player's turn.
     * @param playerID
     */
    public boolean myTurn(int playerID){
        return true;
    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources, then asks the map
     * class if that player can build a road at that location.
     * @param playerID
     * @param edge
     * @return
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edge){
        return true;
    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources, then asks the map
     * class if that player can build a Building at that location.
     * @param playerID
     * @param vertex
     * @return
     */
    public boolean canBuildBuilding(int playerID, VertexLocation vertex){
        return true;
    }

    /**
     * Facade asks if it's the player's turn, then checks the players
     * hand to see if they have enough resources to buy a development
     * card.
     * @param playerID
     * @return
     */
    public boolean canBuyDC(int playerID){
        return true;
    }

    /**
     * Facade asks the game who then asks the turn tracker if trading
     * is permitted for this player.
     * @param playerID
     * @return
     */
    public boolean canTrade(int playerID){
        return true;
    }

    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development CArd
     * @param playerID
     * @return
     */
    public boolean canPlayDC(int playerID){
        return true;
    }
}
