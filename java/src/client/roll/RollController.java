package client.roll;

import client.base.*;
import client.facade.Facade;
import client.services.*;
import client.roll.states.*;
import shared.exceptions.InvalidStateActionException;
import shared.exceptions.PlayerExistsException;
import shared.model.game.TurnTracker;

import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the roll controller
 */
public final class RollController extends Controller implements IRollController, Observer {
    private Facade facade;
	private IRollResultView resultView;
    private RollControllerState state;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {
		super(view);
		setResultView(resultView);
        facade = Facade.getInstance();
		facade.addObserver(this);
        createState(facade.getPhase());
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {
        try {
            state.rollDice();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        } catch (CommandExecutionFailed commandExecutionFailed) {
            commandExecutionFailed.printStackTrace();
        } catch (InvalidStateActionException e) {
            e.printStackTrace();
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
        //Update the state
        createState(facade.getPhase());

        //Call state.update
        try {
            state.update();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create controller state
     * @param phase
     */
    public void createState(TurnTracker.Phase phase){
        switch (phase) {
            case ROLLING:  state = new RollingState(getRollView(), getResultView());
                break;
            default: state = new OtherStates(getRollView(), getResultView());
                break;
        }
    }
}

