package client.roll;

import client.base.*;
import shared.model.game.Dice;

/**
 * Implementation for the roll controller
 */
public final class RollController extends Controller implements IRollController {

	private Dice roller;
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
		//Set the result view value - value of dice roll
		resultView.setRollValue(roll);
		//Show the modal
		getResultView().showModal();
	}
}

