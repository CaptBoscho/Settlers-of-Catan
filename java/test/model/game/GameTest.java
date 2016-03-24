package model.game;

import client.facade.Facade;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.locations.*;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.devcards.*;
import shared.model.cards.resources.ResourceCard;
import shared.model.game.IGame;
import shared.model.game.TurnTracker;
import shared.model.game.trade.TradePackage;
import shared.model.player.Player;
import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Corbin Byers
 */
public class GameTest {

    private IGame game;

    @Before
    public void testInitializeGame() throws InvalidNameException, InvalidPlayerException {
        game = Facade.getInstance().getGame();
        final List<Player> players = new ArrayList<>();

        final Player one = new Player(0, CatanColor.BLUE, 0, 0, "Hope");
        final Player two = new Player(0, CatanColor.BROWN, 1, 1, "Corbin");
        final Player three = new Player(0, CatanColor.GREEN, 2, 2, "Hanna");
        final Player four = new Player(0, CatanColor.ORANGE, 3, 3, "Becca");

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

        final int first = game.initializeGame(players, true, true, false);

        assertTrue(first >= 0 && first < 4);
    }

    @Test
    public void testInitialization() throws Exception{
        final int current_turn = game.getCurrentTurn();
        final HexLocation hloc = new HexLocation(0,0);
        final VertexLocation vloc = new VertexLocation(hloc, VertexDirection.East);
        final EdgeLocation eloc = new EdgeLocation(hloc, EdgeDirection.NorthEast);


        assertTrue(game.canInitiateSettlement(current_turn,vloc));
        game.initiateSettlement(current_turn,vloc);
        assertTrue(game.canInitiateRoad(current_turn,eloc));
        assertFalse(game.canInitiateRoad(current_turn,new EdgeLocation(hloc, EdgeDirection.SouthWest)));
        game.initiateRoad(current_turn,eloc);

        final int next = game.getTurnTracker().nextTurn();

        assertFalse(game.canInitiateSettlement(next, vloc));
    }

    @Test
    public void testCanRollNumber() {
        game.setPhase(TurnTracker.Phase.ROLLING);
        final int turn = game.getCurrentTurn();
        assertTrue(game.canRollNumber(turn));
        try {
            game.getTurnTracker().nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(game.canRollNumber(turn));
    }

    @Test
    public void testRollNumber() throws InvalidDiceRollException{
        final int turn = game.getCurrentTurn();
        try {
            game.getTurnTracker().nextPhase();
            game.getTurnTracker().nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int roll = game.rollDice(turn);
        assertTrue(roll > 1);
        assertTrue(roll <= 12);
    }

    @Test
    public void testCanOfferTrade() {
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);
        assertTrue(game.canOfferTrade(guy));
    }

    @Test
    public void testOfferTrade() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException {
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        int friend = 3;
        if(guy == 3) {
            friend = 4;
        }
        final ResourceCard one = game.getResourceCard(ResourceType.BRICK);
        final ResourceCard two = game.getResourceCard(ResourceType.ORE);
        final ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, friend);

        final List<ResourceType> ones = new ArrayList<>();
        final List<ResourceType> twos = new ArrayList<>();
        ones.add(ResourceType.BRICK);
        ones.add(ResourceType.ORE);
        twos.add(ResourceType.SHEEP);

        assertTrue(game.amountOwnedResource(guy, ResourceType.BRICK) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.ORE) == 1);
        assertTrue(game.amountOwnedResource(friend, ResourceType.SHEEP) == 1);
        TradePackage package1 = new TradePackage(guy,ones);
        TradePackage package2 = new TradePackage(friend, twos);

        game.offerTrade(package1, package2);

        assertTrue(game.amountOwnedResource(friend, ResourceType.BRICK) == 1);
        assertTrue(game.amountOwnedResource(friend, ResourceType.ORE) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.SHEEP) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.BRICK) == 0);
    }

    @Test
    public void testCanFinishTurn() {
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);
        assertTrue(game.canFinishTurn(guy));
    }

    @Test
    public void testFinishTurn() {
        final int guy = game.getCurrentTurn();
        game.nextPhase();
        final TurnTracker.Phase p = game.getCurrentPhase();

        if(p == TurnTracker.Phase.PLAYING || p == TurnTracker.Phase.SETUPONE || p == TurnTracker.Phase.SETUPTWO){
            assertTrue(game.canFinishTurn(guy));
        } else{
            assertFalse(game.canFinishTurn(guy));
        }

    }

    @Test
    public void testCanBuyDevCard() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);
        assertFalse(game.canBuyDevelopmentCard(guy));

        final ResourceCard one = game.getResourceCard(ResourceType.WHEAT);
        final ResourceCard two = game.getResourceCard(ResourceType.ORE);
        final ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, guy);

        assertTrue(game.canBuyDevelopmentCard(guy));
    }

    @Test
    public void testCanUseYearOfPlenty() throws PlayerExistsException, BadCallerException {
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        if(game.numberOfDevCard(guy) == 0){
            assertFalse(game.canUseYearOfPlenty(guy));
        }
        final YearOfPlentyCard card = new YearOfPlentyCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);
        assertTrue(game.canUseYearOfPlenty(guy));

    }

    void testUseYearOfPlenty() {

    }

    @Test
    public void testCanUseRoadBuilder() throws PlayerExistsException, BadCallerException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        if(game.numberOfDevCard(guy) == 0){
            assertFalse(game.canUseRoadBuilding(guy));
        }
        final RoadBuildCard card = new RoadBuildCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);
        assertTrue(game.canUseRoadBuilding(guy));
    }

    void testUseRoadBuilder() {

    }

    @Test
    public void testCanUseSoldier() throws PlayerExistsException, BadCallerException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        if(game.numberOfDevCard(guy) == 0){
            //assertFalse(game.canUseSoldier(guy));
        }
        final SoldierCard card = new SoldierCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);
        //assertTrue(game.canUseSoldier(guy));
    }

    void testUseSoldier() {

    }

    @Test
    public void testCanUseMonopoly() throws PlayerExistsException, BadCallerException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        if(game.numberOfDevCard(guy) == 0){
            assertFalse(game.canUseMonopoly(guy));
        }
        final MonopolyCard card = new MonopolyCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);
        assertTrue(game.canUseMonopoly(guy));
    }

    @Test
    public void testUseMonopoly() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException, DevCardException, BadCallerException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);
        final ResourceCard one = game.getResourceCard(ResourceType.ORE);
        final ResourceCard two = game.getResourceCard(ResourceType.ORE);
        final ResourceCard three = game.getResourceCard(ResourceType.ORE);
        final ResourceCard four = game.getResourceCard(ResourceType.ORE);

        game.giveResource(one, 1);
        game.giveResource(two, 2);
        game.giveResource(three, 3);
        game.giveResource(four, 3);

        // give the user the monopoly card
        final MonopolyCard card = new MonopolyCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);

        game.useMonopoly(guy, ResourceType.ORE);
    }

    @Test
    public void testCanUseMonument() throws PlayerExistsException, BadCallerException{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.PLAYING);

        if(game.numberOfDevCard(guy) == 0){
            assertFalse(game.canUseMonument(guy));
        }
        final MonumentCard card = new MonumentCard();
        game.addDevCard(card, guy);
        game.getPlayerManager().moveNewToOld(guy);
        game.getPlayerManager().getPlayerByIndex(guy).incrementPoints(10);
        assertTrue(game.canUseMonument(guy));
    }

    void testUseMonument() {

    }

    @Test
    public void testBuyDevCard() throws InvalidTypeException, Exception{
        final int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.DISCARDING);

        final ResourceCard one = game.getResourceCard(ResourceType.WHEAT);
        final ResourceCard two = game.getResourceCard(ResourceType.ORE);
        final ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, guy);

        Player p = game.getPlayerManager().getPlayerByID(guy);
        final int sizeold = p.quantityOfDevCards();

        game.buyDevelopmentCard(guy);
        p = game.getPlayerManager().getPlayerByID(guy);
        final int sizenew = p.quantityOfDevCards();

        assertTrue(sizenew == sizeold + 1);
    }
}
