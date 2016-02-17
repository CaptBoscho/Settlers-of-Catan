package shared.model.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corne on 2/17/2016.
 *
 * Representation of Game Dice
 */
public class Dice {
    private List<Die> dice;

    /**
     * Default Constructor
     * @param diceCount Number of die to create
     */
    public Dice(int diceCount){
        assert diceCount > 0;

        //Init List
        dice = new ArrayList<Die>();

        //Create diceCount die
        for(int i = 0; i < diceCount; i++){
            dice.add(new Die()); //Ranges will be [1,6] for each die
        }
    }

    /**
     * Overloaded Constructor - Allows for specifying die range
     * @param diceCount Number of die to create
     * @param dieMin Min value for each die
     * @param dieMax Max value for each die
     */
    public Dice(int diceCount, int dieMin, int dieMax){
        assert diceCount > 0;
        assert dieMax > dieMin;

        //Init List
        dice = new ArrayList<Die>();

        //Create diceCount die
        for(int i = 0; i < diceCount; i++){
            dice.add(new Die(dieMin, dieMax));
        }
    }

    /**
     * Simulates rolling one or more die
     * @return value of dice roll (all die added up)
     */
    public int roll(){
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
