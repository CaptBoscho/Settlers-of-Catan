package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.MaritimeTradeException;
import server.facade.IFacade;
import shared.definitions.ResourceType;
import shared.dto.IDTO;
import shared.dto.MaritimeTradeDTO;
import shared.dto.GameModelDTO;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public class MaritimeTradeCommand implements ICommand {

    private int gameID;
    private MaritimeTradeDTO dto;
    IFacade facade;

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() {
        try {
            facade.maritimeTrade(1, dto);
        } catch(MaritimeTradeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }
}
