package shared.model.player;

import shared.exceptions.*;
import shared.model.cards.Card;
import shared.model.game.trade.TradeType;
import shared.model.cards.resources.ResourceCard;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for managing users
 *
 * @author Kyle Cornelison
 */
public class PlayerManager implements IPlayerManager {
    private List<Player> players;

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
            this.players.add(new Player());
        } else {
            throw new TooManyPlayersException("Max number of players reached!");
        }
    }

    /**
     * Randomize player order (turn order)
     * @throws FailedToRandomizeException
     */
    public void randomizePlayers() throws FailedToRandomizeException {
        if(!this.players.isEmpty())
            Collections.shuffle(this.players);
        else
            throw new FailedToRandomizeException("There are no players to shuffle");
    }

    /**
     * Authenticates a player
     *
     * @param id ID of the player
     * @return True if player authentication is successful
     * @throws AuthenticationException
     */
    @Override
    public boolean authenticatePlayer(int id) throws AuthenticationException {
        return false;
    }

    /**
     * Tests whether or not the max number of players has been reached
     * @return True if a new player can be added
     */
    private boolean canAddPlayer(){
        return this.players.size() < 4;
    }

    /**
     * Gets a player by id
     * @param id ID of the Player
     * @return The Player with the specified ID
     * @throws PlayerExistsException
     */
    public Player getPlayerByID(int id) throws PlayerExistsException {
        for (Player player : this.players) {
            if(player.get_id() == id)
                return player;
        }

        throw new PlayerExistsException("Player with id " + id + " doesn't exist!");
    }

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistsException
     */
    public Player getPlayerByIndex(int index) throws PlayerExistsException {
        if(index < this.players.size() && this.players.get(index) != null)
            return this.players.get(index);
        else
            throw new PlayerExistsException("The player at index " + index + " doesn't exist!");
    }

    //Can Do & Do
    //=============================================================
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param id ID of the player
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canDiscardCards();
    }

    /**
     * Action - Player discards cards
     *
     * @param id    ID of the player
     * @param cards Cards to be discarded
     */
    @Override
    public void discardCards(int id, List<Card> cards) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        player.discardCards(cards);
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param id ID of the player
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canOfferTrade();
    }

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     *
     * @param id   ID of the player
     * @param type Type of trade
     * @return True if Player can perform a maritime trade
     */
    @Override
    public boolean canMaritimeTrade(int id, TradeType type) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canMaritimeTrade(type);
    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param id ID of the player
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canBuyDevCard();
    }

    /**
     * Action - Player buys a dev card
     *
     * @param id ID of the player
     */
    @Override
    public void buyDevCard(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        player.buyDevCard();
    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canUseYearOfPlenty();
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param id ID of the player
     */
    @Override
    public void useYearOfPlenty(int id) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.useYearOfPlenty();
    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canUseRoadBuilder();
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param id ID of the player
     */
    @Override
    public void useRoadBuilder(int id) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.useRoadBuilder();
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canUseSoldier();
    }

    /**
     * Action - Player plays Soldier
     *
     * @param id ID of the player
     */
    @Override
    public void useSoldier(int id) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.useSoldier();
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canUseMonopoly();
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param id ID of the player
     */
    @Override
    public void useMonopoly(int id) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.useMonopoly();
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canUseMonument();
    }

    /**
     * Action - Player plays Monument
     *
     * @param id ID of the player
     */
    @Override
    public void useMonument(int id) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.useMonument();
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param id ID of the player
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canMoveRobber();
    }

    /**
     * Action - Player places the Robber
     *
     * @param id ID of the player
     */
    @Override
    public void placeRobber(int id) throws MoveRobberException, PlayerExistsException {
        Player player = getPlayerByID(id);
        player.placeRobber();
    }

    /**
     * Determine if Player can build a road
     * Checks resource cards
     *
     * @param id ID of the player
     * @return True if Player can build a road
     */
    @Override
    public boolean canBuildRoad(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canBuildRoad();
    }

    /**
     * Action - Player builds a road
     *
     * @param id ID of the player
     */
    @Override
    public void buildRoad(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        player.buildRoad();
    }

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     *
     * @param id ID of the player
     * @return True if Player can build a settlement
     */
    @Override
    public boolean canBuildSettlement(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canBuildSettlement();
    }

    /**
     * Action - Player builds a settlement
     *
     * @param id ID of the player
     */
    @Override
    public void buildSettlement(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        player.buildSettlement();
    }

    /**
     * Determine if Player can build a city
     * Checks resource cards
     *
     * @param id ID of the player
     * @return True if Player can build a city
     */
    @Override
    public boolean canBuildCity(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        return player.canBuildCity();
    }

    /**
     * Action - Player builds a city
     *
     * @param id ID of the player
     */
    @Override
    public void buildCity(int id) throws PlayerExistsException {
        Player player = getPlayerByID(id);
        player.buildCity();
    }

    //Getters/setters
    //========================================================
    /**
     * Get all players
     * @return a list of players
     */
    public List<Player> getPlayers(){
        return this.players;
    }
}
