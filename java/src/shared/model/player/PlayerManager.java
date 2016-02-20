package shared.model.player;

import client.data.PlayerInfo;
import com.google.gson.*;
import shared.definitions.CatanColor;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;

import shared.model.bank.InvalidTypeException;

import shared.model.cards.Card;

import shared.model.cards.devcards.DevelopmentCard;

import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class for managing users
 *
 * @author Kyle Cornelison
 */
public final class PlayerManager implements IPlayerManager {
    private static int WINNING_POINTS = 10;
    private List<Player> players;

    /**
     * Default Constructor
     */
    public PlayerManager(List<Player> ps){
        this.players = ps;
    }

    public PlayerManager(JsonArray players) {
        assert players != null;
        assert players.size() > 0;
        assert players.size() < 5;

        this.players = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!(players.get(i) instanceof JsonNull)) {
                addPlayer(new Player((JsonObject) players.get(i)));
            }
        }
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Randomize player order (turn order)
     * @throws FailedToRandomizeException
     */
    public List<Integer> randomizePlayers() throws FailedToRandomizeException {
        if (!this.players.isEmpty()){
            Collections.shuffle(this.players);
            return this.players.stream().map(Player::getId).collect(Collectors.toList());
        } else {
            throw new FailedToRandomizeException("There are no players to shuffle");
        }
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
        assert id >= 0;

        // TODO --
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
    public Player getPlayerByID(final int id) throws PlayerExistsException {
        assert id >= 0;

        for (final Player player : this.players) {
            if(player.getId() == id) {
                return player;
            }
        }

        throw new PlayerExistsException("Player with id " + id + " doesn't exist!");
    }

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistsException
     */
    public Player getPlayerByIndex(final int index) throws PlayerExistsException {
        assert index >= 0;

        if(index < this.players.size() && this.players.get(index) != null) {
            return this.players.get(index);
        } else {
            throw new PlayerExistsException("The player at index " + index + " doesn't exist!");
        }
    }

    public void setPlayersFromInfo(final List<PlayerInfo> info) {
        if(players.size() > 0) {
            for (int i = 0; i < 4; i++) {
                players.get(i).setPlayerInfo(info.get(i));
            }
        } else {
            for(final PlayerInfo i : info) {
                players.add(new Player(i));
            }
        }
    }

    public int getKnights(final int playerID) throws PlayerExistsException {
        return getPlayerByID(playerID).getKnights();
    }


    public void playKnight(int playerID) throws PlayerExistsException {
        assert playerID >= 0;

        getPlayerByID(playerID).playKnight();
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
    public boolean canDiscardCards(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canDiscardCards();
    }

    /**
     * Action - Player discards cards
     *
     * @param id    ID of the player
     * @param cards Cards to be discarded
     */
    @Override
    public List<ResourceCard> discardCards(final int id, final List<Card> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert id >= 0;
        assert cards != null;
        assert cards.size() > 0;

        return getPlayerByID(id).discardCards(cards);
    }

    public List<ResourceCard> discardResourceType(final int id, final List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert id >= 0;
        assert cards != null;
        assert cards.size() > 0;

        return getPlayerByID(id).discardResourceCards(cards);
    }

    public void addResource(final int id, final ResourceCard rc) throws PlayerExistsException {
        assert id >= 0;
        assert rc != null;

        getPlayerByID(id).addResourceCard(rc);
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param id ID of the player
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canOfferTrade();
    }

    public void offerTrade(final int one, final int two, final List<ResourceType> ones, final List<ResourceType> twos) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException{
        assert one >= 0;
        assert two >= 0;
        assert one != two;
        assert ones != null;
        assert ones.size() > 0;
        assert twos != null;
        assert twos.size() > 0;

        final Player player1 = getPlayerByID(one);
        final Player player2 = getPlayerByID(two);
        final List<ResourceCard> discardedOnes = player1.discardResourceCards(ones);
        final List<ResourceCard> discardedTwos = player2.discardResourceCards(twos);
        discardedOnes.forEach(player2::addResourceCard);
        discardedTwos.forEach(player1::addResourceCard);
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
    public boolean canMaritimeTrade(int id, PortType type) throws PlayerExistsException {
        assert id >= 0;
        assert type != null;

        return getPlayerByID(id).canMaritimeTrade(type);
    }


    public void maritimeTrade(int playerID, PortType type, ResourceType want) throws InvalidTypeException, PlayerExistsException {
        assert playerID >= 0;
        assert type != null;
        assert want != null;

        Player player = getPlayerByID(playerID);
        // TODO -


    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param id ID of the player
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canBuyDevCard();
    }

    /**
     * Action - Player buys a dev card
     *
     * @param id ID of the player
     */
    @Override
    public void buyDevCard(final int id) throws PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).buyDevCard();
    }

    public void addDevCard(final int id, final DevelopmentCard dc) throws PlayerExistsException {
        assert id >= 0;
        assert dc != null;

        getPlayerByID(id).addDevCard(dc);
    }

    public void moveNewToOld(final int id) throws PlayerExistsException, BadCallerException {
        assert id >= 0;

        getPlayerByID(id).moveNewToOld();
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
    public void useYearOfPlenty(final int id) throws DevCardException, PlayerExistsException {
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
    public boolean canUseRoadBuilder(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canUseRoadBuilder();
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param id ID of the player
     */
    @Override
    public void useRoadBuilder(final int id) throws DevCardException, PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).useRoadBuilder();
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canUseSoldier();
    }

    /**
     * Action - Player plays Soldier
     *
     * @param id ID of the player
     */
    @Override
    public void useSoldier(final int id) throws DevCardException, PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).useSoldier();
    }


    public void changeLargestArmyPossession(final int playerOld, final int playerNew) throws PlayerExistsException {
        assert playerNew >= 0;
        assert playerOld >= 0;
        assert playerNew != playerOld;

        getPlayerByID(playerOld).loseArmyCard();
        getPlayerByID(playerNew).winArmyCard();
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canUseMonopoly();
    }

    /**
     * Action - Player plays Monopoly
     * By playing the Monopoly card, all other players must give this player all their resource cards that match the
     * given ResourceType
     *
     * @param id ID of the player
     * @param type The type of the resource that will be used with the Monopoly card
     */
    @Override
    public void useMonopoly(final int id, final ResourceType type) throws DevCardException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert id >= 0;
        assert type != null;

        Player monopolyUser = getPlayerByID(id);
        monopolyUser.discardMonopoly();
        for(final Player player : this.getPlayers()) {

            final int amount = player.getNumberOfType(type);

            // if this player is *not* the player who is playing the Monopoly card
            if (!monopolyUser.equals(player) && amount > 0) {

                // collect all resources from that player and give it to the calling player
                for (final ResourceCard aReturned : player.discardResourceCards(type, amount)) {
                    addResource(id, aReturned);
                }
            }
        }
    }


    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param id ID of the player
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(final int id) throws PlayerExistsException {
        assert id >= 0;

        return getPlayerByID(id).canUseMonument();
    }

    /**
     * Action - Player plays Monument
     *
     * @param id ID of the player
     */
    @Override
    public void useMonument(final int id) throws DevCardException, PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).useMonument();
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
        assert id >= 0;

        return getPlayerByID(id).canMoveRobber();
    }

    /**
     * Action - Player places the Robber
     *
     * @param robber ID of the player
     */
    @Override
    public ResourceType placeRobber(int robber, int robbed) throws MoveRobberException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        Player player = getPlayerByID(robber);
        Player two = getPlayerByID(robbed);

        player.placeRobber();
        ResourceCard treasure = two.robbed();
        player.addResourceCard(treasure);
        return treasure.getType();
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
        assert id >= 0;

        return getPlayerByID(id).canBuildRoad();
    }

    /**
     * Action - Player builds a road
     *
     * @param id ID of the player
     */
    @Override
    public void buildRoad(int id) throws PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).buildRoad();
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
        assert id >= 0;

        return getPlayerByID(id).canBuildSettlement();
    }

    /**
     * Action - Player builds a settlement
     *
     * @param id ID of the player
     */
    @Override
    public void buildSettlement(int id) throws PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).buildSettlement();
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
        assert id >= 0;

        return getPlayerByID(id).canBuildCity();
    }

    /**
     * Action - Player builds a city
     *
     * @param id ID of the player
     */
    @Override
    public void buildCity(final int id) throws PlayerExistsException {
        assert id >= 0;

        getPlayerByID(id).buildCity();
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

    @Override
    public CatanColor getPlayerColorByID(int id) throws PlayerExistsException {
        return getPlayerByID(id).getColor();
    }

    public Integer getAvailableRoads(int id) throws PlayerExistsException {
        return getPlayerByID(id).getAvailableRoads();
    }

    public Integer getAvailableSettlements(int id) throws PlayerExistsException {
        return getPlayerByID(id).getAvailableSettlements();
    }

    public Integer getAvailableCities(int id) throws PlayerExistsException {
        return getPlayerByID(id).getAvailableCities();
    }

    public Player getWinner() throws GameOverException{
        for(Player player : players){
            if(player.getVictoryPoints() == WINNING_POINTS){
                return player;
            }
        }

        throw new GameOverException("The game is still in progress.");
    }
}
