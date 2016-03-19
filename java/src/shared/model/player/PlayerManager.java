package shared.model.player;

import com.google.gson.*;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
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
import java.util.List;

/**
 * Class for managing users
 *
 * @author Kyle Cornelison
 */
public final class PlayerManager implements IPlayerManager {
    //region Member variables
    private static int WINNING_POINTS = 10;
    private List<Player> players;
    //endregion

    //region Constructors
    /**
     * Default Constructor
     */
    public PlayerManager(List<Player> players){
        this.players = players;
    }

    /**
     * Json Constructor
     * @param players
     */
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
    //endregion

    //region Manager methods
    /**
     * Authenticates a player
     *
     * @param playerIndex index of the player
     * @return True if player authentication is successful
     * @throws AuthenticationException
     */
    @Override
    public boolean authenticatePlayer(int playerIndex) throws AuthenticationException {
        assert playerIndex >= 0;

        // TODO --
        return false;
    }

    /**
     * Changes control of the largest army card
     *
     * @param playerOld
     * @param playerNew
     * @throws PlayerExistsException
     */
    @Override
    public void changeLargestArmyPossession(int playerOld, int playerNew) throws PlayerExistsException {
        assert playerNew >= 0;
        assert playerOld >= 0;
        assert playerNew != playerOld;

        getPlayerByIndex(playerOld).loseArmyCard();
        getPlayerByIndex(playerNew).winArmyCard();
    }

    /**
     * Moves new development cards to old pile - making them playable
     *
     * @param playerIndex
     * @throws PlayerExistsException
     * @throws BadCallerException
     */
    @Override
    public void moveNewToOld(int playerIndex) throws PlayerExistsException, BadCallerException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).moveNewToOld();
    }
    //endregion

    //region Can do methods
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param playerIndex index of the player
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canDiscardCards();
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param playerIndex index of the player
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canOfferTrade();
    }

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     *
     * @param playerIndex index of the player
     * @param type        Type of trade
     * @return True if Player can perform a maritime trade
     */
    @Override
    public boolean canMaritimeTrade(int playerIndex, PortType type) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert type != null;

        return getPlayerByIndex(playerIndex).canMaritimeTrade(type);
    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param playerIndex index of the player
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canBuyDevCard();
    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param playerIndex index of the player
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int playerIndex) throws PlayerExistsException {
        Player player = getPlayerByIndex(playerIndex);

        return player.canUseYearOfPlenty();
    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param playerIndex index of the player
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canUseRoadBuilder();
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param playerIndex index of the player
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canUseSoldier();
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param playerIndex index of the player
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canUseMonopoly();
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param playerIndex index of the player
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canUseMonument();
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerIndex index of the player
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canMoveRobber();
    }

    /**
     * Determine if Player can build a road
     * Checks resource cards
     *
     * @param playerIndex index of the player
     * @return True if Player can build a road
     */
    @Override
    public boolean canBuildRoad(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canBuildRoad();
    }

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     *
     * @param playerIndex index of the player
     * @return True if Player can build a settlement
     */
    @Override
    public boolean canBuildSettlement(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canBuildSettlement();
    }

    /**
     * Determine if Player can build a city
     * Checks resource cards
     *
     * @param playerIndex index of the player
     * @return True if Player can build a city
     */
    @Override
    public boolean canBuildCity(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).canBuildCity();
    }
    //endregion

    //region Do methods
    /**
     * Action - Player discards cards
     *
     * @param playerIndex index of the player
     * @param cards       Cards to be discarded
     */
    @Override
    public List<ResourceCard> discardCards(int playerIndex, List<Card> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert cards != null;
        assert cards.size() > 0;

        return getPlayerByID(playerIndex).discardCards(cards);
    }

    /**
     * Removes the cards from the player's bank
     *
     * @param playerIndex
     * @param cards
     * @return
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    @Override
    public List<ResourceCard> discardResourceType(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert cards != null;
        assert cards.size() > 0;

        return getPlayerByIndex(playerIndex).discardResourceCards(cards);
    }

    /**
     * Offer domestic trade between players
     *
     * @param playerIndexOne
     * @param playerIndexTwo
     * @param playerOneCards
     * @param playerTwoCards
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    @Override
    public void offerTrade(int playerIndexOne, int playerIndexTwo, List<ResourceType> playerOneCards, List<ResourceType> playerTwoCards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndexOne >= 0;
        assert playerIndexTwo >= 0;
        assert playerIndexOne != playerIndexTwo;
        assert playerOneCards != null;
        assert playerOneCards.size() > 0;
        assert playerTwoCards != null;
        assert playerTwoCards.size() > 0;

        final Player player1 = getPlayerByIndex(playerIndexOne);
        final Player player2 = getPlayerByIndex(playerIndexTwo);
        final List<ResourceCard> discardedOnes = player1.discardResourceCards(playerOneCards);
        final List<ResourceCard> discardedTwos = player2.discardResourceCards(playerTwoCards);
        discardedOnes.forEach(player2::addResourceCard);
        discardedTwos.forEach(player1::addResourceCard);
    }

    /**
     * Action - Player buys a dev card
     *
     * @param playerIndex index of the player
     */
    @Override
    public void buyDevCard(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).buyDevCard();
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerIndex index of the player
     */
    @Override
    public void useYearOfPlenty(int playerIndex) throws DevCardException, PlayerExistsException {
        Player player = getPlayerByIndex(playerIndex);

        player.useYearOfPlenty();
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerIndex index of the player
     */
    @Override
    public void useRoadBuilder(int playerIndex) throws DevCardException, PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).useRoadBuilder();
    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerIndex index of the player
     */
    @Override
    public void useSoldier(int playerIndex) throws DevCardException, PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).useSoldier();
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerIndex index of the player
     * @param type
     */
    @Override
    public void useMonopoly(int playerIndex, ResourceType type) throws DevCardException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerIndex >= 0;
        assert type != null;

        Player monopolyUser = getPlayerByIndex(playerIndex);
        monopolyUser.discardMonopoly();
        for(final Player player : this.getPlayers()) {

            final int amount = player.getNumberOfType(type);

            // if this player is *not* the player who is playing the Monopoly card
            if (!monopolyUser.equals(player) && amount > 0) {

                List<ResourceType> cards = new ArrayList<>();
                for(int i=0; i<amount; i++){
                    cards.add(type);
                }
                // collect all resources from that player and give it to the calling player
                for (final ResourceCard aReturned : player.discardResourceCards(cards)) {
                    addResource(playerIndex, aReturned);
                }
            }
        }
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerIndex index of the player
     */
    @Override
    public void useMonument(int playerIndex) throws DevCardException, PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).useMonument();
    }

    /**
     * Action - Player places the Robber
     *
     * @param playerRobbing index of the player robbing
     * @param playerRobbed  index of the player being robbed
     */
    @Override
    public void placeRobber(int playerRobbing, int playerRobbed) throws MoveRobberException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        Player robber = getPlayerByIndex(playerRobbing);
        Player robbed = getPlayerByIndex(playerRobbed);

        robber.placeRobber();
        ResourceCard treasure = robbed.robbed();
        robber.addResourceCard(treasure);
    }

    /**
     * Action - Player builds a road
     *
     * @param playerIndex index of the player
     */
    @Override
    public void buildRoad(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).buildRoad();
    }

    /**
     * Action - Player builds a settlement
     *
     * @param playerIndex index of the player
     */
    @Override
    public void buildSettlement(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).buildSettlement();
    }

    /**
     * Action - Player builds a city
     *
     * @param playerIndex index of the player
     */
    @Override
    public void buildCity(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        getPlayerByIndex(playerIndex).buildCity();
    }
    //endregion

    //region Add card methods
    /**
     * Add the resource card to the player
     *
     * @param playerIndex
     * @param rc
     * @throws PlayerExistsException
     */
    @Override
    public void addResource(int playerIndex, ResourceCard rc) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert rc != null;

        getPlayerByIndex(playerIndex).addResourceCard(rc);
    }

    /**
     * Add the dev card to the player
     *
     * @param playerIndex
     * @param dc
     * @throws PlayerExistsException
     */
    @Override
    public void addDevCard(int playerIndex, DevelopmentCard dc) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert dc != null;

        getPlayerByIndex(playerIndex).addDevCard(dc);
    }
    //endregion

    //region Getters
    /**
     * Get all players
     * @return a list of players
     */
    public List<Player> getPlayers(){
        return this.players;
    }

    /**
     * Get the winning player
     *
     * @return
     * @throws GameOverException
     */
    @Override
    public Player getWinner() throws GameOverException {
        for(Player player : players){
            if(player.getVictoryPoints() == WINNING_POINTS){
                return player;
            }
        }
        return null;
    }

    /**
     * Gets a player by id
     *
     * @param id ID of the Player
     * @return The Player with the specified ID
     * @throws PlayerExistsException
     */
    @Override
    public Player getPlayerByID(int id) throws PlayerExistsException {
        assert id >= 0;

        for (final Player player : this.players) {
            if(player.getId() == id) {
                return player;
            }
        }

        throw new PlayerExistsException("Player with id " + id + " doesn't exist!");
    }

    /**
     * Gets the specified player's color
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public CatanColor getPlayerColorByIndex(int playerIndex) throws PlayerExistsException {
        return getPlayerByIndex(playerIndex).getColor();
    }

    /**
     * Gets a player by index
     *
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistsException
     */
    @Override
    public Player getPlayerByIndex(int index) throws PlayerExistsException {
        assert index >= 0 && index < 4;

        for (final Player player : this.players) {
            if(player.getPlayerIndex() == index) {
                return player;
            }
        }

        throw new PlayerExistsException("The player at index " + index + " doesn't exist!");
    }

    /**
     * Get the number of resource cards the player has
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public int getNumberResourceCards(int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;

        return getPlayerByIndex(playerIndex).getNumberResourceCards();
    }

    /**
     * Get number of roads the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableRoads(int playerIndex) throws PlayerExistsException {
        return getPlayerByIndex(playerIndex).getAvailableRoads();
    }

    /**
     * Get the number of settlements the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableSettlements(int playerIndex) throws PlayerExistsException {
        return getPlayerByIndex(playerIndex).getAvailableSettlements();
    }

    /**
     * Get the number of cities the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableCities(int playerIndex) throws PlayerExistsException {
        return getPlayerByIndex(playerIndex).getAvailableCities();
    }

    /**
     * Get number of dev cards the player has
     *
     * @param type
     * @param playerIndex
     * @return
     */
    @Override
    public int getNumberDevCards(DevCardType type, int playerIndex) {
        try {
            return getPlayerByIndex(playerIndex).getNumberOfDevCardsByType(type);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get the number of soldier cards the player has played
     *
     * @param playerIndex
     * @return
     */
    @Override
    public int getNumberOfSoldiers(int playerIndex) {
        try {
            return getPlayerByIndex(playerIndex).getSoldiers();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Determine if the user has discarded
     *
     * @param playerIndex
     * @return
     */
    @Override
    public boolean hasDiscarded(int playerIndex) {
        try {
            return getPlayerByIndex(playerIndex).hasDiscarded();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the player by name
     *
     * @param name
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Player getPlayerByName(String name) throws PlayerExistsException {
        assert name != null;

        for (final Player player : this.players) {
            if(player.getName().equals(name)) {
                return player;
            }
        }

        throw new PlayerExistsException("Player with name " + name + " doesn't exist!");
    }

    /**
     * Get the player's color
     *
     * @param name
     * @return
     */
    @Override
    public CatanColor getPlayerColorByName(String name) {
        assert name != null;
        try {
            return getPlayerByName(name).getColor();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Private helper methods
    public void addPlayer(final Player player) {
        this.players.add(player);
    }
    //endregion

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) {
            return false;
        } else {
            PlayerManager other = (PlayerManager)o;
            if(this.players.size() != other.getPlayers().size()) {
                return false;
            } else {
                for(int i = 0; i < this.players.size(); i++) {
                    if(!this.players.get(i).equals(other.getPlayers().get(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public JsonArray toJSON() {
        JsonArray array = new JsonArray();
        for(Player p: players){
            array.add(p.toJSON());
        }
        JsonObject json = new JsonObject();

        return array;
    }

    //endregion
}
