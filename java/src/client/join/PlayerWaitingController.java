package client.join;

import client.base.*;
import client.catan.GameStatePanel;
import client.catan.TitlePanel;
import client.data.PlayerInfo;
import client.facade.Facade;
import client.map.*;
import client.services.MissingUserCookieException;
import client.services.Poller;
import client.services.ServerProxy;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerController;
import client.turntracker.TurnTrackerView;
import shared.model.game.Game;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
        Facade.getInstance().addObserver(this);

        // START GAMEEE
        try {
            ServerProxy.getInstance().getCurrentModel(Game.getInstance().getVersion());
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }

        Poller p = Poller.getInstance();

        // show waiting screen if there are not 4 players joined
        if(Facade.getInstance().getPlayers().size() < 4) {
            PlayerInfo[] infoArr = new PlayerInfo[Facade.getInstance().getPlayers().size()];
            Facade.getInstance().getPlayers().toArray(infoArr);
            getView().setPlayers(Facade.getInstance().getPlayers().toArray(infoArr));
            getView().showModal();
            p.setPlayerWaitingPolling();
        } else {
            p.setModelPolling();
        }
        p.start();
	}

	@Override
	public void addAI() {

		// TODO --
		// TEMPORARY
		getView().closeModal();
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
        System.out.println("updated called");
        if(getView().isModalShowing()) {
            PlayerInfo[] infoArr = new PlayerInfo[Facade.getInstance().getPlayers().size()];
            Facade.getInstance().getPlayers().toArray(infoArr);
            getView().closeModal();
            if(infoArr.length < 4) {
                getView().setPlayers(Facade.getInstance().getPlayers().toArray(infoArr));
                getView().showModal();
            } else {
                Poller.getInstance().setModelPolling();
            }
        }
    }
}
