package shared.model.player;

import com.google.gson.*;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;

import shared.model.bank.InvalidTypeException;

import shared.model.cards.Card;

import shared.model.cards.devcards.DevelopmentCard;
import shared.model.game.trade.TradeType;

import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
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
    public PlayerManager(List<Player> ps){
        this.players = ps;
    }

    public PlayerManager(JsonArray players) {
        this.players = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!(players.get(i) instanceof JsonNull)) {
                addPlayer(new Player((JsonObject) players.get(i)));
            }
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Randomize player order (turn order)
     * @throws FailedToRandomizeException
     */
    public List<Integer> randomizePlayers() throws FailedToRandomizeException {
        if (!this.players.isEmpty()){
            Collections.shuffle(this.players);
            List<Integer> id_order = new ArrayList<>();
            for (Player p : this.players) {
                id_order.add(p.get_id());
            }
            return id_order;
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
        if(index < this.players.size() && this.players.get(index) != null) {
            return this.players.get(index);
        } else {
            throw new PlayerExistsException("The player at index " + index + " doesn't exist!");
        }
    }


    public Integer getKnights(int playerID) throws PlayerExistsException{
        return getPlayerByID(playerID).getKnights();}


    public void playKnight(int playerID) throws PlayerExistsException{
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
    public List<ResourceCard> discardCards(int id, List<Card> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {

        Player player = getPlayerByID(id);
        return player.discardCards(cards);
    }

    public List<ResourceCard> discardResourceType(int id, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        Player player = getPlayerByID(id);
        return player.discardResourceCards(cards);
    }

    public void addResource(int id, ResourceCard rc) throws PlayerExistsException{
        Player player = getPlayerByID(id);
        player.addResourceCard(rc);
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

    public void offerTrade(int one, int two, List<ResourceType> ones, List<ResourceType> twos) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException{
        Player player1 = getPlayerByID(one);
        Player player2 = getPlayerByID(two);
        List<ResourceCard> discardedOnes = player1.discardResourceCards(ones);
        List<ResourceCard> discardedTwos = player2.discardResourceCards(twos);
        for(ResourceCard rc: discardedOnes){
            player2.addResourceCard(rc);
        }
        for(ResourceCard rc: discardedTwos){
            player1.addResourceCard(rc);
        }
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
        Player player = getPlayerByID(id);
        return player.canMaritimeTrade(type);
    }


    public void maritimeTrade(int playerID, PortType type, ResourceType want) throws InvalidTypeException, PlayerExistsException{
        Player player = getPlayerByID(playerID);



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

    public void addDevCard(int id, DevelopmentCard dc) throws PlayerExistsException{
        Player player = getPlayerByID(id);
        player.addDevCard(dc);
    }

    public void moveNewToOld(int id) throws PlayerExistsException, BadCallerException{
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


    public void changeLargestArmyPossession(int playerold, int playernew) throws PlayerExistsException{
        getPlayerByID(playerold).loseArmyCard();
        getPlayerByID(playernew).winArmyCard();
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
    public void useMonopoly(int id, int num, ResourceType type) throws DevCardException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        Player player = getPlayerByID(id);
        player.useMonopoly();
        System.out.println("mono1: " + getPlayerByID(id).getNumberOfType(type));
        for(int i=1; i<= num; i++){
            if(i!=id){
                System.out.println("I: " + i);
                int amount = getPlayerByID(i).getNumberOfType(type);
                System.out.println("amount: " + amount);
                List<ResourceType> rt = new ArrayList<ResourceType>();
                for(int k=0; k<amount; k++){
                    System.out.println("first for");
                    rt.add(type);
                }
                List<ResourceCard> returned = getPlayerByID(i).discardResourceCards(rt);
                for(int k=0; k< returned.size(); k++){
                    System.out.println("adding");
                    addResource(id, returned.get(k));
                }

            }
        }
        System.out.println("mono2: " + getPlayerByID(id).howManyofThisCard(type));
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
