package model.Player;

import org.junit.Test;
import shared.exceptions.InvalidNameException;
import shared.model.player.Name;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class NameTests {

    @Test
    public void testEquals() throws InvalidNameException {
        Name one = new Name("Derek");
        assertFalse(one.equals(null));
        assertFalse(one.equals("A String"));
        assertFalse(one.equals("Derek"));

        Name copy = one;
        Name two = new Name("Derek");
        Name three = new Name("NotDerek");

        assertTrue(one.equals(copy));
        assertTrue(one.equals(two));
        assertFalse(one.equals(three));
    }

    @Test
    public void testInvalidNames() {
        try {
            new Name("ValidName");
            assertTrue(true);
        } catch (InvalidNameException e) {
            assertFalse(true);
        }

        try {
            new Name("Not a Name");
            assertTrue(false);
        } catch (InvalidNameException e) {
            assertTrue(true);
        }

        try {
            new Name("numb3rs");
            assertTrue(false);
        } catch (InvalidNameException e) {
            assertTrue(true);
        }

        try {
            new Name("Derek!!!!");
            assertTrue(false);
        } catch (InvalidNameException e) {
            assertTrue(true);
        }
    }
}
