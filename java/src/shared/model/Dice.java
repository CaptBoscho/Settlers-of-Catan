package shared.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @name Dice
 * @description Represents the dice in a game
 */
public class Dice {
    private Integer min;
    private Integer max;

    /**
     * @name Dice
     * @description Default Constructor
     */
    public Dice(){
        this.min = 1;
        this.max = 6;
    }

    /**
     * @name Dice
     * @param min
     * @param max
     * @Description Dice class constructor
     */
    public Dice(Integer min, Integer max){
        this.min = min;
        this.max = max;
    }

    /**
     * @name roll
     * @description simulates rolling one or more dice
     * @return
     */
    public Integer roll(){
        return new Integer(ThreadLocalRandom.current().nextInt(this.min, this.max + 1));
    }
}
