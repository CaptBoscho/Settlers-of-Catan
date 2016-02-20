package client.join;

import client.base.*;
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
        getView().setPlayers(Game.getInstance().getPlayerInfo());
		getView().showModal();
	}

	@Override
	public void addAI() {

		// TODO --
		// TEMPORARY
		getView().closeModal();
	}

}
