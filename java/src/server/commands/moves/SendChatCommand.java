package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.SendChatException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.SendChatDTO;

import java.io.Serializable;

/**
 * A command object that sends a chat
 *
 * @author Joel Bradley
 */
public final class SendChatCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;
    private String content;

    /**
     * Communicates with the ServerFacade to carry out the Send Chat command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert this.content != null;

        try {
            return Config.facade.sendChat(this.gameId, this.playerIndex, this.content);
        } catch (SendChatException e) {
            throw new CommandExecutionFailedException("Failed to send the chat!");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final SendChatDTO tmpDTO = (SendChatDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.content = tmpDTO.getContent();
    }
}
