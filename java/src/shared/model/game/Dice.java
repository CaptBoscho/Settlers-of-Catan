package shared.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle Cornelison
 *
 * Representation of Game Dice
 */
public final class Dice implements Serializable {
    private List<Die> dice;

    /**
     * Default Constructor
     * @param diceCount Number of die to create
     */
    public Dice(int diceCount) {
        assert diceCount > 0;

        //Init List
        dice = new ArrayList<>();

        //Create diceCount die
        for(int i = 0; i < diceCount; i++){
            dice.add(new Die()); //Ranges will be [1,6] for each die
        }
    }

    /**
     * Simulates rolling one or more die
     * @return value of dice roll (all die added up)
     */
    public int roll() {
        //Initialize roll value
        int rollVal = 0;

        //Roll each die
        for (Die die : dice) {
            rollVal += die.roll();
        }

        //Return roll value
        return rollVal;
    }
}
