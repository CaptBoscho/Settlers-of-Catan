package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RollNumberException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.RollNumberDTO;

/**
 * A command object that rolls a number
 *
 * @author Joel Bradley
 */
public class RollNumberCommand implements ICommand {
    private int gameId;
    private int value;
    private int playerIndex;
    private IFacade facade;

    /**
     * Constructor
     */
    public RollNumberCommand(IFacade facade, RollNumberDTO roll) {
        this.facade = facade;
        this.gameId = roll.getGameId();
        this.value = roll.getValue();
        this.playerIndex = roll.getPlayerIndex();
    }

    /**
     * Communicates with the ServerFacade to carry out the Roll Number command
     * @return JsonObject
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.rollNumber(gameId, playerIndex, value);
        } catch (RollNumberException e) {
            throw new CommandExecutionFailedException("Error occurred while rolling! GameId: " + gameId +
                    " PlayerIndex: " + playerIndex + " value: " + value);
        }
    }
}
