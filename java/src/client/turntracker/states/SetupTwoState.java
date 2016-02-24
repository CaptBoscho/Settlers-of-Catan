package client.turntracker.states;

import client.facade.Facade;
import client.services.UserCookie;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerControllerState;

/**
 * Created by Kyle 'TMD' Cornelison on 2/18/2016.
 */
public class SetupTwoState extends TurnTrackerControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;

    /**
     * Constructor
     */
    public SetupTwoState(ITurnTrackerView view){
        super(view);
        this.view = view;
        this.facade = Facade.getInstance();
        this.userCookie = UserCookie.getInstance();
    }

    @Override
    public void endTurn() {
        super.endTurn();
    }

    @Override
    public void initFromModel() {
        super.initFromModel();

        //Game State
        if(facade.getCurrentTurn() == userCookie.getPlayerId()){
            view.updateGameState("Setup 2 - end placement", true);
        }else{
            view.updateGameState("Waiting for other players to setup",false);
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
