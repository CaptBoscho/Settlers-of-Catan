package client.join;

import client.base.*;
import client.data.PlayerInfo;
import client.facade.Facade;
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
        getView().setPlayers(Facade.getInstance().getPlayers().toArray(new PlayerInfo[4]));
		getView().showModal();
	}

	@Override
	public void addAI() {

		// TODO --
		// TEMPORARY
		getView().closeModal();
	}

}
