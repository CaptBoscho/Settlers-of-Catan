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
 * Roll Controller - Rolling state
 */
public class RollingState extends RollControllerState {

    /**
     * Constructor
     * @param rollView
     * @param resultView
     */
    public RollingState(IRollView rollView, IRollResultView resultView){
        super(rollView, resultView);
    }

    @Override
    public void rollDice() throws PlayerExistsException, MissingUserCookieException, CommandExecutionFailed, InvalidStateActionException {
        super.rollDice();
    }

    @Override
    public void update() {
        try {
            super.update();
        } catch(PlayerExistsException e){}
    }
}
