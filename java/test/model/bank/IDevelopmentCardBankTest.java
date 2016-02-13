package model.bank;

import org.junit.*;
import shared.exceptions.BadCallerException;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.IDevelopmentCardBank;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.devcards.*;

import static org.junit.Assert.*;

/**
 * @author Danny Harding
 */
public class IDevelopmentCardBankTest {
    IDevelopmentCardBank developmentCardBank;

    @Before
    public void initializeDevelopmentCardBank() {
        developmentCardBank = new DevelopmentCardBank(false);
    }

    @Test
    public void testDraw() throws Exception {
        developmentCardBank = new DevelopmentCardBank(true);
        developmentCardBank.empty();

        addOneOfEach();

        final int previousSize = developmentCardBank.size();
        final DevelopmentCard developmentCard = developmentCardBank.draw();
        final int afterSize = developmentCardBank.size();

        // One card got taken out
        assertTrue(previousSize == afterSize + 1);
        for (int i = 0; i < afterSize; i++) {
            // This type of card was taken out
            assertFalse(developmentCardBank.draw().getType() == developmentCard.getType());
        }
    }

    @Test
    public void testCanUseYearOfPlenty() throws Exception {

        try {
            developmentCardBank.addDevCard(new YearOfPlentyCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        assertFalse(developmentCardBank.canUseYearOfPlenty());

        addOneOfEach();

        assertFalse(developmentCardBank.canUseYearOfPlenty());

        try {
            developmentCardBank.moveNewToOld();
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        assertTrue(developmentCardBank.canUseYearOfPlenty());
        developmentCardBank.useYearOfPlenty();
        assertTrue(developmentCardBank.canUseYearOfPlenty());
        developmentCardBank.useYearOfPlenty();
        assertFalse(developmentCardBank.canUseYearOfPlenty());

        developmentCardBank = new DevelopmentCardBank(true);
        try {
            developmentCardBank.addDevCard(new YearOfPlentyCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        // bank owned by game can't use cards
        assertFalse(developmentCardBank.canUseYearOfPlenty());
    }

    @Test
    public void testUseYearOfPlenty() throws Exception {

    }

    @Test
    public void testCanUseSoldier() throws Exception {

        try {
            developmentCardBank.addDevCard(new SoldierCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        assertFalse(developmentCardBank.canUseSoldier());

        addOneOfEach();

        assertFalse(developmentCardBank.canUseSoldier());

        try {
            developmentCardBank.moveNewToOld();
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        assertTrue(developmentCardBank.canUseSoldier());
        developmentCardBank.useSoldier();
        assertTrue(developmentCardBank.canUseSoldier());
        developmentCardBank.useSoldier();
        assertFalse(developmentCardBank.canUseSoldier());

        developmentCardBank = new DevelopmentCardBank(true);
        try {
            developmentCardBank.addDevCard(new SoldierCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        // bank owned by game can't use cards
        assertFalse(developmentCardBank.canUseSoldier());
    }

    @Test
    public void testCanUseMonopoly() throws Exception {

        try {
            developmentCardBank.addDevCard(new MonopolyCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        assertFalse(developmentCardBank.canUseMonopoly());

        addOneOfEach();

        assertFalse(developmentCardBank.canUseMonopoly());

        try {
            developmentCardBank.moveNewToOld();
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        assertTrue(developmentCardBank.canUseMonopoly());
        developmentCardBank.useMonopoly();
        assertTrue(developmentCardBank.canUseMonopoly());
        developmentCardBank.useMonopoly();
        assertFalse(developmentCardBank.canUseMonopoly());

        developmentCardBank = new DevelopmentCardBank(true);
        try {
            developmentCardBank.addDevCard(new MonopolyCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        // bank owned by game can't use cards
        assertFalse(developmentCardBank.canUseMonopoly());
    }

    @Test
    public void testCanUseMonument() throws Exception {

        try {
            developmentCardBank.addDevCard(new MonumentCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        assertFalse(developmentCardBank.canUseMonument());

        addOneOfEach();

        assertFalse(developmentCardBank.canUseMonument());

        try {
            developmentCardBank.moveNewToOld();
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        assertTrue(developmentCardBank.canUseMonument());
        developmentCardBank.useMonument();
        assertTrue(developmentCardBank.canUseMonument());
        developmentCardBank.useMonument();
        assertFalse(developmentCardBank.canUseMonument());

        developmentCardBank = new DevelopmentCardBank(true);
        try {
            developmentCardBank.addDevCard(new MonumentCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        // bank owned by game can't use cards
        assertFalse(developmentCardBank.canUseMonument());
    }

    @Test
    public void testCanUseRoadBuild() throws Exception {

        try {
            developmentCardBank.addDevCard(new RoadBuildCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        assertFalse(developmentCardBank.canUseRoadBuild());

        addOneOfEach();

        assertFalse(developmentCardBank.canUseRoadBuild());

        try {
            developmentCardBank.moveNewToOld();
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        assertTrue(developmentCardBank.canUseRoadBuild());
        developmentCardBank.useRoadBuild();
        assertTrue(developmentCardBank.canUseRoadBuild());
        developmentCardBank.useRoadBuild();
        assertFalse(developmentCardBank.canUseRoadBuild());

        developmentCardBank = new DevelopmentCardBank(true);
        try {
            developmentCardBank.addDevCard(new RoadBuildCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        // bank owned by game can't use cards
        assertFalse(developmentCardBank.canUseRoadBuild());
    }

    @Test
    public void testAddDevCard() throws Exception {
        int beforeSize = developmentCardBank.size();
        addOneOfEach();
        int afterSize = developmentCardBank.size();

        assertEquals(beforeSize, afterSize - 5);

        for (int i = 0; i < 3; i++) {
            beforeSize = developmentCardBank.size();
            try {
                developmentCardBank.addDevCard(new RoadBuildCard());
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
            afterSize = developmentCardBank.size();

            assertEquals(beforeSize, afterSize - 1);
        }
    }

    private void addOneOfEach() {
        try {
            developmentCardBank.addDevCard(new YearOfPlentyCard());
            developmentCardBank.addDevCard(new SoldierCard());
            developmentCardBank.addDevCard(new RoadBuildCard());
            developmentCardBank.addDevCard(new MonopolyCard());
            developmentCardBank.addDevCard(new MonumentCard());
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
    }
}