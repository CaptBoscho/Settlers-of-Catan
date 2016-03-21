package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.YearOfPlentyException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.PlayYOPCardDTO;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public class YearOfPlentyCommand implements ICommand {

    PlayYOPCardDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.yearOfPlenty(1, dto.getPlayerIndex(), dto.getResource1(), dto.getResource2());
        } catch (YearOfPlentyException e) {
            throw new CommandExecutionFailedException("YearOfPlentyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (PlayYOPCardDTO)dto;
    }

}
