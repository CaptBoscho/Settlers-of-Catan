package client.points.states;

import client.facade.Facade;
import client.facade.ModelPlayerInfo;
import client.points.IGameFinishedView;
import client.points.PointsControllerState;
import client.services.UserCookie;
import shared.exceptions.GameOverException;

/**
 * Created by Kyle 'TMD' Cornelison on 2/19/2016.
 *
 * Represents PointsController State of Game Finished
 */
public class GameFinishedState extends PointsControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private IGameFinishedView view;

    /**
     * Constructor
     * @param view Finished Game View
     */
    public GameFinishedState(IGameFinishedView view){
        this.view = view;
        this.facade = Facade.getInstance();
        this.userCookie = UserCookie.getInstance();
    }

    @Override
    public void initFromModel(){}

    @Override
    public void update(){
        try {
            //Get the winner
            ModelPlayerInfo winner = facade.getWinner();
            view.setWinner(winner.getName(), winner.getIndex() == userCookie.getPlayerId());

            //Show the modal
            if(!view.isModalShowing())
                view.showModal();
        } catch (GameOverException e) {
            return;
        }
    }
}
