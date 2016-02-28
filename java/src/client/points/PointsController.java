package client.points;

import client.base.*;
import client.data.PlayerInfo;
import client.facade.Facade;
import client.services.UserCookie;
import shared.model.game.TurnTracker;

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
	
	public IPointsView getPointsView() {
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}

	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		int points = facade.getPoints(userCookie.getPlayerIndex());
        assert points <= 10;
        assert points >= 0;
        getPointsView().setPoints(points);

        int winner = facade.getWinnerIndex();
        if(winner != -1 && facade.getPhase() == TurnTracker.Phase.GAMEFINISHED) {
            boolean isWinner = winner == userCookie.getPlayerIndex();
            getFinishedView().setWinner(facade.getPlayerNameByIndex(winner), isWinner);
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

