package client.turntracker;

import client.facade.Facade;
import client.services.UserCookie;
import org.apache.http.cookie.Cookie;
import shared.definitions.CatanColor;
import client.base.*;
import shared.exceptions.PlayerExistsException;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	private Facade facade;
	private int currentPlayerID;

	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
		facade = Facade.getInstance();
        facade.addObserver(this);
		currentPlayerID = facade.getCurrentTurn();
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
//		facade.endTurn(); // TODO: 2/16/2016 Have Corbin implement in facade
//        getView().updatePlayer();
	}
	
	private void initFromModel() {
		try {
			CatanColor currentPlayerColor = facade.getPlayerColorByID(currentPlayerID);
			getView().setLocalPlayerColor(currentPlayerColor);
		} catch (PlayerExistsException e) {
//			getView().;
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
