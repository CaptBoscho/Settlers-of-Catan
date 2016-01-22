package shared.model.player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing users
 */
public class PlayerManager {
    List<Player> players;

    /**
     * Default Constructor
     */
    public PlayerManager(){
        this.players = new ArrayList<Player>(4);
    }

    /**
     * Creates a new player and adds it to the list of players
     * @throws TooManyPlayersException
     */
    public void addNewPlayer() throws TooManyPlayersException{
        if(canAddPlayer()){
            this.players.add(new Player()); //// TODO: 1/19/2016 Decide to do this way or add a method including the player color 
        }else{
            throw new TooManyPlayersException("Max number of players reached!");
        }
    }

    /**
     * Validate
     * @return True if player authentication is successful
     */
    public boolean authenticatePlayer(){
        return true;
    }

    /**
     * Tests whether or not the max number of players has been reached
     * @return True if a new player can be added
     */
    private boolean canAddPlayer(){
        if(this.players.size() >= 4){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     */
    public Player getPlayerByIndex(int index){
        return this.players.get(index);
    }
}
