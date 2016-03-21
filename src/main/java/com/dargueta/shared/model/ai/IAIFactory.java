package com.dargueta.shared.model.ai;

import com.dargueta.shared.exceptions.CreateAIException;

/**
 * Created by Kyle 'TMD' Cornelison on 3/19/2016.
 */
public interface IAIFactory {
    /**
     * Create a new AI Player based on the AI Type
     * @param type
     * @return
     */
    AIPlayer create(AIType type) throws CreateAIException;
}
