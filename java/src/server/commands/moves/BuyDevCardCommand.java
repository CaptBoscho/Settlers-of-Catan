package server.commands.moves;

import client.services.CommandExecutionFailed;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import server.commands.ICommand;
import server.exceptions.BuyDevCardException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.BuyDevCardDTO;
import shared.dto.GameModelDTO;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public class BuyDevCardCommand implements ICommand {
    IFacade facade;
    BuyDevCardDTO dto;

    /**
     * Constructor
     */
    public BuyDevCardCommand(BuyDevCardDTO dto, IFacade facade) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.buyDevCard(1, dto.getPlayerIndex());
        } catch (BuyDevCardException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("BuyDevCardCommand failed to execute properly");
        }
    }

}
