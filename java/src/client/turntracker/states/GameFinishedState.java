package client.turntracker.states;

import client.facade.Facade;
import client.services.UserCookie;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerControllerState;

/**
 * Created by Kyle 'TMD' Cornelison on 2/20/2016.
 */
public class GameFinishedState extends TurnTrackerControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;

    /**
     * Constructor
     */
    public GameFinishedState(ITurnTrackerView view){
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
        view.updateGameState("The game has ended.", false);
    }

    @Override
    public void update() {
        super.update();
    }
}
