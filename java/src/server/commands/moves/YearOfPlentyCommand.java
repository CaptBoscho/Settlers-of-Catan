package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.YearOfPlentyException;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.GameModelDTO;
import shared.dto.PlayYOPCardDTO;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public class YearOfPlentyCommand implements ICommand {

    PlayYOPCardDTO dto;
    IFacade facade;

    /**
     * Constructor
     */
    public YearOfPlentyCommand(PlayYOPCardDTO dto, IFacade facade) {
        this.dto = dto;
        this.facade = facade;
    }

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return ServerFacade.getInstance().yearOfPlenty(1, dto.getPlayerIndex(), dto.getResource1(), dto.getResource2());
        } catch (YearOfPlentyException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("YearOfPlentyCommand failed to execute properly");
        }
    }

}
