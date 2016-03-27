package shared.model.ai.main;

import shared.model.ai.aimodel.AIType;
import shared.model.ai.aitrainer.AITrainer;
import shared.model.ai.aitrainer.IAITrainer;

/**
 * Created by Kyle 'TMD' Cornelison on 3/26/2016.
 *
 * Entry for training an AI
 */
public class Main {

    public static void main(String[] args) {
        IAITrainer trainer = new AITrainer();

        trainer.train(AIType.valueOf(args[0]));

        trainer.saveTraining();
    }
}
