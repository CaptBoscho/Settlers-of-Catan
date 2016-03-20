package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonopolyException;
import server.facade.IFacade;
import server.facade.ServerFacade;
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
    IFacade facade;

    /**
     * Constructor
     */
    public MonopolyCommand() {
        this.dto = dto;
        this.facade = facade;
    }

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return ServerFacade.getInstance().monopoly(1, dto.getPlayerIndex(), dto.getResource());
//        } catch (MonopolyException | InvalidTypeException e) {
//            throw new CommandExecutionFailedException("MonopolyCommand failed to execute properly");
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
