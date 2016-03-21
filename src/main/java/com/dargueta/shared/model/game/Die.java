package com.dargueta.shared.model.game;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Representation of a game die
 */
public final class Die {
    private int min;
    private int max;

    /**
     * Default Constructor - sets die range to [1,6]
     */
    Die() {
        this.min = 1;
        this.max = 6;
    }

    /**
     * Overloaded Constructor - sets die range to [min,max]
     * @param min minimum value the dice can return
     * @param max maximum value the dice can return
     */
    Die(final int min, final int max) {
        assert max > min;

        this.min = min;
        this.max = max;
    }

    /**
     * Simulates rolling a die
     * @return Integer value between the min and max (inclusive)
     */
    public Integer roll() {
        return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
    }
}
