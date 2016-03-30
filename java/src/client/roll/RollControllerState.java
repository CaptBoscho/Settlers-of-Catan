package client.roll;

import client.facade.Facade;
import client.services.*;
import shared.dto.RollNumberDTO;
import shared.exceptions.InvalidStateActionException;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Dice;
import shared.model.game.TurnTracker;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kyle 'TMD' Cornelison on 2/24/2016.
 *
 * Base class for Roll Controller states
 */
public class RollControllerState {
    private boolean rolled;

    private Facade facade;
    private IServer server;
    private UserCookie userCookie;
    private IRollView rollView;
    private IRollResultView rollResultView;

    /**
     * Constructor
     */
    public RollControllerState(IRollView rollView, IRollResultView resultView){
        this.rollView = rollView;
        this.rollResultView = resultView;
        this.facade = Facade.getInstance();
        this.server = ServerProxy.getInstance();
        this.userCookie = UserCookie.getInstance();
    }

    public void rollDice() throws PlayerExistsException, MissingUserCookieException, CommandExecutionFailed, InvalidStateActionException {
        if (!rolled) {
            rolled = true;
            //Create a dice object
            Dice roller = new Dice(2);
            //Roll the dice
            int roll = roller.roll();

            //Tell the server
            int index = userCookie.getPlayerIndex();
            RollNumberDTO rollDTO = new RollNumberDTO(index, roll);
            server.rollNumber(rollDTO);

            //Set the result view value - value of dice roll
            rollResultView.setRollValue(roll);
            //Show the modal
            rollResultView.showModal();
        }
    }

    public void update() throws PlayerExistsException {
        final int FOUR_SECONDS = 4 * 1000;

        int index = userCookie.getPlayerIndex();
        if(facade.getCurrentTurn() == index) {
            rolled = false;
            rollView.setMessage("Roll the dice");
            rollView.showModal();

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (rollView.isModalShowing()) {
                        try {
                            rollView.closeModal();
                            rollDice();
                        } catch (PlayerExistsException | MissingUserCookieException | InvalidStateActionException | CommandExecutionFailed e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, FOUR_SECONDS);
        }
    }
}
