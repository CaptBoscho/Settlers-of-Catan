package client.roll;

import client.base.*;
import client.facade.Facade;
import client.services.*;
import shared.dto.RollNumberDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Dice;
import shared.model.game.TurnTracker;

import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the roll controller
 */
public final class RollController extends Controller implements IRollController, Observer {

	private Dice roller;
    private Facade facade;
	private IServer server;
	private UserCookie userCookie;
	private IRollResultView resultView;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {
		super(view);
		setResultView(resultView);
		roller = new Dice(2);
        facade = Facade.getInstance();
		facade.addObserver(this);
		server = ServerProxy.getInstance();
		userCookie = UserCookie.getInstance();
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
		//Roll the dice
		int roll = roller.roll();


		//Tell the server
		try {
            int id = userCookie.getPlayerId();
            int index = facade.getPlayerIndexByID(id);
            RollNumberDTO rollDTO = new RollNumberDTO(index, roll);
			server.rollNumber(rollDTO);
		} catch (MissingUserCookieException e) {
			getRollView().setMessage("Error Rolling");
		} catch (CommandExecutionFailed commandExecutionFailed) {
			getRollView().setMessage("Error Rolling");
		} catch (PlayerExistsException e) {
            getRollView().setMessage("Error Rolling");
        }

        //Set the result view value - value of dice roll
		resultView.setRollValue(roll);
		//Show the modal
		getResultView().showModal();
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
        // TODO: 2/23/2016 Use state pattern here
        if(facade.getPhase() == TurnTracker.Phase.ROLLING){
            if(facade.getCurrentTurn() == userCookie.getPlayerId()) {
                getRollView().setMessage("Roll the dice");
                getRollView().showModal();
            }
        }
	}
}

