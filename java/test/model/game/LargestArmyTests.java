package model.game;

import org.junit.Test;
import shared.model.game.LargestArmy;

import static org.junit.Assert.assertEquals;

/**
 * @author Derek Argueta
 */
public class LargestArmyTests {

    @Test
    public void testDefaultConstructor() {
        LargestArmy la = new LargestArmy();
        assertEquals(la.getOwner(), -1);
        assertEquals(la.getMostSoldiers(), 0);
        assertEquals(la.getPointWorth(), 2);
    }

    // TODO - number of soldiers should be computed from player!
    @Test
    public void testNormalConstructor() {
        LargestArmy la = new LargestArmy(4);
        assertEquals(la.getOwner(), 4);
        assertEquals(la.getMostSoldiers(), 0);
        assertEquals(la.getPointWorth(), 2);
    }

    @Test
    public void testSetOwner() {
        LargestArmy la = new LargestArmy(4);
        la.setNewOwner(3, 6);
        assertEquals(la.getOwner(), 3);
        assertEquals(la.getMostSoldiers(), 6);
        assertEquals(la.getPointWorth(), 2);
    }
}
