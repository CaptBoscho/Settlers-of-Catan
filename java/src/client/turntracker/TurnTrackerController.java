package client.turntracker;

import client.facade.Facade;
import client.services.UserCookie;
import client.turntracker.states.*;
import client.base.*;
import shared.model.game.Game;
import shared.model.game.TurnTracker;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	private Facade facade;
    private UserCookie userCookie;
    private TurnTrackerControllerState state;

	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
        createState(facade.getPhase());
		facade = Facade.getInstance();
        facade.addObserver(this);
        userCookie = UserCookie.getInstance();
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		state.endTurn();
	}
	
	private void initFromModel() {
        state.initFromModel();
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
		state.update((Game)o);
	}

    /**
     * Creates a new state to model the current game state
     * @param phase
     */
    private void createState(TurnTracker.Phase phase){
        switch (phase) {
            case SETUPONE:  state = new SetupOneState();
                break;
            case SETUPTWO:  state = new SetupTwoState();
                break;
            case ROLLING:  state = new RollingState();
                break;
            case PLAYING:  state = new PlayingState();
                break;
            case ROBBING:  state = new RobbingState();
                break;
            case DISCARDING:  state = new DiscardingState(getView());
                break;
            default: state = new PlayingState();
                break;
        }
    }
}
