package client.join;

import client.base.*;
import client.catan.GameStatePanel;
import client.catan.TitlePanel;
import client.data.PlayerInfo;
import client.facade.Facade;
import client.map.*;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import client.turntracker.ITurnTrackerView;
import client.turntracker.TurnTrackerController;
import client.turntracker.TurnTrackerView;
import shared.model.game.Game;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
        if(Facade.getInstance().getPlayers().size() > 4) {
            getView().setPlayers(Facade.getInstance().getPlayers().toArray(new PlayerInfo[4]));
            getView().showModal();
        }

        // START GAMEEE
        try {
            ServerProxy.getInstance().getCurrentModel(0);
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }
        ITurnTrackerView view = new TurnTrackerView(new TitlePanel(), new GameStatePanel());
        TurnTrackerController turnTrackerController = new TurnTrackerController(view);
        view.setController(turnTrackerController);
        turnTrackerController.initFromModel();
	}

	@Override
	public void addAI() {

		// TODO --
		// TEMPORARY
		getView().closeModal();
	}

}
