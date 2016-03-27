package shared.model.ai.aitrainer;

import shared.model.ai.aimodel.AIType;

/**
 * Created by Kyle 'TMD' Cornelison on 3/25/2016.
 */
public interface IAITrainer {

    /**
     * Trains the specified AI type
     * @param type
     */
    void train(AIType type);

    /**
     * Saves the current training state of the ai
     */
    void saveTraining();
}
