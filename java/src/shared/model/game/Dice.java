package shared.model.game;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Representation of game dice
 */
public final class Dice {
    private int min;
    private int max;

    /**
     * Default Constructor - sets dice range to [1,6]
     */
    public Dice() {
        this.min = 1;
        this.max = 6;
    }

    /**
     * Overloaded Constructor - sets dice range to [min,max]
     * @param min minimum value the dice can return
     * @param max maximum value the dice can return
     */
    public Dice(final int min, final int max) {
        assert max > min;

        this.min = min;
        this.max = max;
    }

    /**
     * Simulates rolling one or more dice
     * @return Integer value between the min and max (inclusive)
     */
    public Integer roll() {
        return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
    }
}
