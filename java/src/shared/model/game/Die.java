package shared.model.game;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representation of a game die
 */
public final class Die implements Serializable {
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
     * Simulates rolling a die
     * @return Integer value between the min and max (inclusive)
     */
    public Integer roll() {
        return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
    }
}
