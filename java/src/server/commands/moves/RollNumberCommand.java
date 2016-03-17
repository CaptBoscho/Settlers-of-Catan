package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.RollNumberException;
import server.facade.IFacade;
import server.facade.ServerFacade;
import server.managers.GameManager;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;
import shared.dto.RollNumberDTO;

/**
 * A command object that rolls a number
 *
 * @author Joel Bradley
 */
public class RollNumberCommand implements ICommand {
    private int value;
    private int playerIndex;
    private IFacade facade;

    /**
     * Constructor
     */
    public RollNumberCommand(IFacade facade, RollNumberDTO roll) {
        this.facade = facade;
        this.value = roll.getValue();
        this.playerIndex = roll.getPlayerIndex();
    }

    /**
     * Communicates with the ServerFacade to carry out the Roll Number command
     * @return JsonObject
     */
    @Override
    public IDTO execute() {
        int gameID = 10;
        try {
            return facade.rollNumber(gameID, playerIndex, value);
        } catch (RollNumberException e) {
            return new GameModelDTO();
        }
    }

}
