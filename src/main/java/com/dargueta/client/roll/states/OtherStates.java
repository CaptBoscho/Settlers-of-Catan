package client.roll.states;

import client.roll.IRollResultView;
import client.roll.IRollView;
import client.roll.RollControllerState;
import client.services.CommandExecutionFailed;
import client.services.MissingUserCookieException;
import shared.exceptions.InvalidStateActionException;
import shared.exceptions.PlayerExistsException;

/**
 * Created by Kyle 'TMD' Cornelison on 2/24/2016.
 *
 * Represents all other states - Roll controller doesn't need to do anything unless
 * in the rolling state
 */
public class OtherStates extends RollControllerState{

    /**
     * Constructor
     *
     * @param rollView
     * @param resultView
     */
    public OtherStates(IRollView rollView, IRollResultView resultView) {
        super(rollView, resultView);
    }

    @Override
    public void rollDice() throws PlayerExistsException, MissingUserCookieException, CommandExecutionFailed, InvalidStateActionException {
        throw new InvalidStateActionException("Player can't roll the dice right now!");
    }

    @Override
    public void update() {
        //Do nothing: Don't need this here but keeping for completeness
    }
}


