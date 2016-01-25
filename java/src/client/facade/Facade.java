<<<<<<< HEAD:java/src/client/facade/Facade.java
package client.facade;
import shared.*;
=======
package shared.facade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.game.Game;
import shared.model.map.Map;
>>>>>>> 42b6a704e063e1e04203883513c0916de678bcd8:java/src/shared/facade/Facade.java

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
     *
     * @param playerID The ID of the player asking this
     */
    public boolean myTurn(int playerID) {
        return true;
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
    public boolean canBuildRoad(int playerID, EdgeLocation edge) {
        return true;
    }

    /**
     * Builds a road
     * @param playerID
     * @param edge
     * @throws BuildException
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws BuildException{
        if(canBuildRoad(playerID, edge)){

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
    public boolean canBuildBuilding(int playerID, VertexLocation vertex) {
        return true;
    }

    /**
     * Builds a building
     * @param playerID
     * @param vertex
     * @throws BuildException
     */
    public void buildBuilding(int playerID, VertexLocation vertex) throws BuildException{
        if(canBuildBuilding(playerID,vertex)){

        } else {
            throw new BuikldException("Can't build the building");
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
        return true;
    }

    /**
     * player Buys a development card
     * @param playerID
     * @throws BuildException
     */
    public void buyDC(int playerID) throws BuildException {
        if(canBuyDC(playerID)){

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
        return true;
    }

    /**
     * Commits the trade
     * @param playerID
     * @throws BuildException
     */
    public void Trade(int playerID) throws BuildException {
        if(canTrade(playerID)){

        } else {
            throw new BuildException("Can't complete this trade");
        }
    }

    /**
     * Facade asks the game who then asks the turn tracker if the
     * player can play a Development Card
     *
     * @param playerID The ID of the player asking this
     * @return A boolean value indicating if a development card can be played
     */
    public boolean canPlayDC(int playerID) {
        return true;
    }
<<<<<<< HEAD:java/src/client/facade/Facade.java

    /**
     * plays the Development Card
     * @param playerID
     * @param dc
     * @throws BuildException
     */
    public void playDC(int playerID, DevelopmentCard dc) throws BuildException {
        if(canPlayDC(playerID)){

        } else {
            throw new BuildException("can't play this Develpment Card");
        }
    }


}
=======
}
>>>>>>> 42b6a704e063e1e04203883513c0916de678bcd8:java/src/shared/facade/Facade.java
