package client.roll;

import client.base.*;
import client.facade.Facade;
import client.services.*;
import shared.dto.RollNumberDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Dice;

/**
 * Implementation for the roll controller
 */
public final class RollController extends Controller implements IRollController {

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
		//Display the roll view
		getRollView().setMessage("Roll the dice!");
		getRollView().showModal();

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
}

