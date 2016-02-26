package client.roll;

import client.facade.Facade;
import client.services.*;
import shared.dto.RollNumberDTO;
import shared.exceptions.InvalidStateActionException;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Dice;
import shared.model.game.TurnTracker;

import java.util.Observable;

/**
 * Created by Kyle 'TMD' Cornelison on 2/24/2016.
 *
 * Base class for Roll Controller states
 */
public class RollControllerState {
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
        //Create a dice object
        Dice roller = new Dice(2);
        //Roll the dice
        int roll = 7;

        //Tell the facade
        // TODO: 2/24/2016 Ask corbin if the facade needs to be told as well
        
        //Tell the server
        int id = userCookie.getPlayerId();
        int index = facade.getPlayerIndexByID(id);
        RollNumberDTO rollDTO = new RollNumberDTO(index, roll);
        server.rollNumber(rollDTO);

        //Set the result view value - value of dice roll
        rollResultView.setRollValue(roll);
        //Show the modal
        rollResultView.showModal();
    }

    public void update() throws PlayerExistsException {
        int id = userCookie.getPlayerId();
        if(facade.getCurrentTurn() == facade.getPlayerIndexByID(id)) {
            rollView.setMessage("Roll the dice");
            rollView.showModal();
        }
    }
}
