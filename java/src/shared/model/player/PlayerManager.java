package shared.model.player;

import shared.exceptions.FailedToRandomizeException;
import shared.exceptions.PlayerExistException;
import shared.exceptions.TooManyPlayersException;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for managing users
 *
 * @author Kyle Cornelison
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
        } else {
            throw new TooManyPlayersException("Max number of players reached!");
        }
    }

    /**
     * Randomize player order (turn order)
     * @throws FailedToRandomizeException
     */
    public void randomizePlayers() throws FailedToRandomizeException {
        Collections.shuffle(this.players);
    }

    /**
     * Validate
     * @return True if player authentication is successful
     * @throws AuthenticationException
     */
    public boolean authenticatePlayer() throws AuthenticationException{
        return true;
    }

    /**
     * Tests whether or not the max number of players has been reached
     * @return True if a new player can be added
     */
    private boolean canAddPlayer(){
        return this.players.size() < 4;
    }

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistException
     */
    public Player getPlayerByIndex(int index) throws PlayerExistException{
        return this.players.get(index);
    }

    /**
     * Get all players
     * @return a list of players
     */
    public List<Player> getPlayers(){
        return this.players;
    }
}
