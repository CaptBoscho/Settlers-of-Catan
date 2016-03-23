package client.points;

import client.base.*;
import client.facade.Facade;
import client.services.UserCookie;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private IGameFinishedView finishedView;
	private Facade facade;
    private UserCookie userCookie;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		super(view);
		this.facade = Facade.getInstance();
        this.userCookie = UserCookie.getInstance();
        setFinishedView(finishedView);
        facade.addObserver(this);
	}
	
	private IPointsView getPointsView() {
		return (IPointsView)super.getView();
	}
	
	private IGameFinishedView getFinishedView() {
		return finishedView;
	}

	private void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		int points = facade.getPoints(userCookie.getPlayerIndex());
        assert points >= 0;
		if(points > 10) {
			points = 10;
		}
        getPointsView().setPoints(points);

        int winner = facade.getWinnerId();
        if(winner != -1) {
            boolean isWinner = winner == facade.getPlayerIdByIndex(userCookie.getPlayerIndex());
            getFinishedView().setWinner(facade.getPlayerNameByIndex(facade.getPlayerIndexByID(winner)), isWinner);
            getFinishedView().showModal();
        }
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
        initFromModel();
	}
}

