package client.turntracker.states;

import client.facade.Facade;
import client.turntracker.TurnTrackerControllerState;
import shared.model.game.Game;

import java.util.Observable;

/**
 * Created by corne on 2/18/2016.
 */
public class SetupTwoState extends TurnTrackerControllerState {
    private Facade facade;

    /**
     * Constructor
     */
    public SetupTwoState(){
        facade = Facade.getInstance();
    }

    @Override
    public void endTurn() {

    }

    @Override
    public void initFromModel() {

    }

    @Override
    public void update(Observable o, Object arg) {
        initFromModel();
    }
}
