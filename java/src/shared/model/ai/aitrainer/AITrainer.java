package shared.model.ai.aitrainer;

import shared.model.ai.aimodel.AIType;
import shared.model.game.Game;

import java.util.Dictionary;

/**
 * Created by Kyle 'TMD' Cornelison on 3/25/2016.
 *
 * Class used to train a new AI
 */
public class AITrainer implements IAITrainer {
    private Game gameModel;
    private Object currentTraining; //TODO: figure out what the object should actually be
    private Dictionary<String, Boolean> moves; //Available moves the ai can use at the current time-step
    private Dictionary<String, Integer> params; //Weights...

    /**
     * Trains the specified AI type
     *
     * @param type
     */
    @Override
    public void train(AIType type) {

    }

    /**
     * Saves the current training state of the ai
     */
    @Override
    public void saveTraining() {
        //Todo: save training results somewhere to be loaded into the server
    }


}
