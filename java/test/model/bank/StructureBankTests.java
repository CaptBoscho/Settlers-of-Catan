package model.bank;

import org.junit.Test;
import shared.model.bank.StructureBank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class StructureBankTests {

    @Test
    public void testDefaultConstructor() {
        StructureBank bank = new StructureBank();

        // bank should be able to build stuff right away
        assertTrue(bank.canBuildRoad());
        assertTrue(bank.canBuildCity());
        assertTrue(bank.canBuildSettlement());

        assertEquals(15, bank.getAvailableRoads());
        assertEquals(5, bank.getAvailableSettlements());
        assertEquals(4, bank.getAvailableCities());
    }

    @Test
    public void testOtherConstructor() {
        StructureBank bank = new StructureBank(1, 2, 1);

        // bank should still be able to build stuff right away
        assertTrue(bank.canBuildRoad());
        assertTrue(bank.canBuildCity());
        assertTrue(bank.canBuildSettlement());

        assertEquals(1, bank.getAvailableRoads());
        assertEquals(2, bank.getAvailableSettlements());
        assertEquals(1, bank.getAvailableCities());
    }

    @Test
    public void testBuildingRoad() {
        StructureBank bank = new StructureBank();

        for(int i = 0; i < 10; i++) bank.buildRoad();
        assertEquals(5, bank.getAvailableRoads());
        assertTrue(bank.canBuildRoad());

        for(int i = 0; i < 5; i++) bank.buildRoad();
        assertEquals(0, bank.getAvailableRoads());
        assertFalse(bank.canBuildRoad());

        try {
            bank.buildRoad();
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBuildingSettlement() {
        StructureBank bank = new StructureBank();

        bank.buildSettlement();
        assertEquals(4, bank.getAvailableSettlements());
        assertTrue(bank.canBuildSettlement());

        for(int i = 0; i < 4; i++) bank.buildSettlement();
        assertEquals(0, bank.getAvailableSettlements());
        assertFalse(bank.canBuildSettlement());

        try {
            bank.buildSettlement();
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBuildingCity() {
        StructureBank bank = new StructureBank();

        bank.buildCity();
        assertEquals(3, bank.getAvailableCities());
        assertTrue(bank.canBuildCity());

        for(int i = 0; i < 3; i++) bank.buildCity();
        assertEquals(0, bank.getAvailableCities());
        assertFalse(bank.canBuildCity());

        try {
            bank.buildCity();
            assertTrue(false);
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
}
