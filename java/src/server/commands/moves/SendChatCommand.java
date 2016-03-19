package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.SendChatException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.SendChatDTO;

/**
 * A command object that sends a chat
 *
 * @author Joel Bradley
 */
public class SendChatCommand implements ICommand {
    private IFacade facade;
    private SendChatDTO dto;
    /**
     * Constructor
     */
    public SendChatCommand(IFacade facade, SendChatDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Send Chat command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.sendChat(1, dto.getPlayerIndex(), dto.getContent());
        } catch (SendChatException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Failed to send the chat!");
        }
    }
}
