package client.turntracker.states;

import client.facade.Facade;
import client.services.UserCookie;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerControllerState;
import shared.model.game.Game;

import java.util.Observable;

/**
 * Created by corne on 2/18/2016.
 */
public class PlayingState extends TurnTrackerControllerState{
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;

    /**
     * Constructor
     */
    public PlayingState(ITurnTrackerView view){
        super(view);
        view = view;
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
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
            view.updateGameState("Playing", true);
        }else{
            view.updateGameState("Waiting for other players...",false);
        }
    }

    @Override
    public void update(Game game) {
        super.update(game);
    }
}
