package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.SendChatException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.SendChatDTO;

/**
 * A command object that sends a chat
 *
 * @author Joel Bradley
 */
public class SendChatCommand implements ICommand {

    private SendChatDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Send Chat command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.sendChat(1, dto.getPlayerIndex(), dto.getContent());
        } catch (SendChatException e) {
            throw new CommandExecutionFailedException("Failed to send the chat!");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (SendChatDTO)dto;
    }
}
