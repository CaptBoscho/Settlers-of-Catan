package client.points;

import client.base.*;
import client.facade.Facade;
import client.facade.ModelPlayerInfo;
import client.points.states.GameFinishedState;
import client.points.states.GamePlayingState;
import client.turntracker.states.*;
import shared.model.game.Game;
import shared.model.game.TurnTracker;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private IGameFinishedView finishedView;
	private Facade facade;
    private PointsControllerState state;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		super(view);
		this.facade = Facade.getInstance();
		setFinishedView(finishedView);
        createState(facade.getPhase());
		initFromModel();
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
		for(ModelPlayerInfo playerInfo : facade.getPlayers()){
            getPointsView().setPoints(playerInfo.getVictoryPoints());
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
        state.update();
	}

    private void createState(TurnTracker.Phase phase){
        switch (phase) {
            case GAMEFINISHED:  state = new GameFinishedState(getFinishedView());
                break;
            default: state = new GamePlayingState(getPointsView());
                break;
        }
    }
}

