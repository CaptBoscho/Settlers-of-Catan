package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonopolyException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.PlayMonopolyDTO;
import shared.model.bank.InvalidTypeException;

/**
 * A command object that plays a monopoly card
 *
 * @author Joel Bradley
 */
public class MonopolyCommand implements ICommand {

    PlayMonopolyDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.monopoly(1, dto.getPlayerIndex(), dto.getResource());
        } catch (MonopolyException | InvalidTypeException e) {
            throw new CommandExecutionFailedException("MonopolyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (PlayMonopolyDTO)dto;
    }

}
