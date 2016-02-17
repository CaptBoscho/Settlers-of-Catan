package client.turntracker;

import client.facade.Facade;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;

import java.util.Observable;

/**
 * Created by corne on 2/16/2016.
 *
 * Base class for TurnTracker Controller States
 */
public class TurnTrackerControllerState {
    private Game gModel;
    private Facade facade;
    private int currentPlayerID;

    /**
     * Constructor
     */
    public TurnTrackerControllerState(Game model){
        gModel = model;
        facade = Facade.getInstance();
        currentPlayerID = facade.getCurrentTurn();
        initFromModel();
    }

    public void endTurn() {
//		facade.endTurn(); // TODO: 2/16/2016 Have Corbin implement in facade
    }

    private void initFromModel() {
        try {
            CatanColor currentPlayerColor = facade.getPlayerColorByID(currentPlayerID);
            //getView().setLocalPlayerColor(currentPlayerColor);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    public void update(Observable o, Object arg) {
        initFromModel();
    }
}
