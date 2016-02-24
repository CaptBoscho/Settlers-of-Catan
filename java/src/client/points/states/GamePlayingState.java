package client.points.states;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.facade.ModelPlayerInfo;
import client.points.IPointsView;
import client.points.PointsControllerState;
import client.services.UserCookie;

/**
 * Created by Kyle 'TMD' Cornelison on 2/19/2016.
 *
 * Represents PointsController State of Game Playing
 */
public class GamePlayingState extends PointsControllerState {
    private IPointsView view;
    private UserCookie userCookie;

    /**
     * Constructor
     * @param view Finished Game View
     */
    public GamePlayingState(IPointsView view){
        this.view = view;
        this.userCookie = UserCookie.getInstance();
    }

    @Override
    public void initFromModel(){}

    @Override
    public void update(){
        view.setPoints(userCookie.getPlayerInfo().getVictoryPoints());
    }
}
