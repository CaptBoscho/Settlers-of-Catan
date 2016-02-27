package client.points.states;

import client.facade.Facade;
import client.points.IPointsView;
import client.points.PointsControllerState;
import client.services.UserCookie;

/**
 * Created by Kyle 'TMD' Cornelison on 2/19/2016.
 *
 * Represents PointsController State of Game Playing
 */
public class GamePlayingState extends PointsControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private IPointsView view;

    /**
     * Constructor
     * @param view Finished Game View
     */
    public GamePlayingState(IPointsView view){
        this.view = view;
        this.facade = Facade.getInstance();
        this.userCookie = UserCookie.getInstance();
    }

    @Override
    public void initFromModel(){}

    @Override
    public void update(){
        int vicPoints = userCookie.getVictoryPoints();
        view.setPoints(vicPoints);
    }
}
