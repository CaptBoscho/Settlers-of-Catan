package shared.model.ai.aimodel;

import shared.exceptions.CreateAIException;
import shared.exceptions.InvalidPlayerException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 3/19/2016.
 */
public class AIFactory implements IAIFactory {
    private static AIFactory _instance;

    private AIFactory(){

    }

    /**
     * Returns the instance of the AIFactory
     * @return
     */
    public static IAIFactory getInstance(){
        if(_instance == null)
            _instance = new AIFactory();

        return _instance;
    }

    /**
     * Get the types of AIs allowed
     *
     * @return
     */
    public static List<AIType> listAITypes(){
        List<AIType> aiTypes = Arrays.asList(AIType.values());
        return aiTypes;
    }

    /**
     * Create a new AI Player based on the AI Type
     *
     * @param type
     * @return
     */
    @Override
    public AIPlayer create(AIType type) throws CreateAIException {

        try {
            if (type == AIType.LARGEST_ARMY) {
                return new AIPlayer(0, null, -1, -1, "Temp", AIType.LARGEST_ARMY);
            } else if (type == AIType.RODHAMMER){
                return new AIPlayer(0, null, -1, -1, "Temp", AIType.RODHAMMER);
            }else{
                throw new CreateAIException("Failed to create the AI type specified");
            }
        } catch (InvalidPlayerException e) {
            e.printStackTrace();
            throw new CreateAIException("Failed to create the AI Player");
        }
    }
}
