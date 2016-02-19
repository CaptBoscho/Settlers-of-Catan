package client.turntracker.states;

import client.facade.Facade;
import client.services.UserCookie;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerControllerState;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;
import shared.model.player.Player;

import java.util.Observable;

/**
 * Created by corne on 2/18/2016.
 */
public class DiscardingState extends TurnTrackerControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;

    /**
     * Constructor
     */
    public DiscardingState(ITurnTrackerView view){
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
            view.updateGameState("Discard", true);
        }else{
            view.updateGameState("Waiting for other players to discard",false);
        }
    }

    @Override
    public void update(Game game) {
        super.update(game);
    }
}
