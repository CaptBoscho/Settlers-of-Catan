package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.JoinGameException;
import server.main.Config;
import shared.definitions.CatanColor;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.JoinGameDTO;

/**
 * A command object that adds a player to a game.
 *
 * @author Danny Harding
 */
public class JoinCommand implements ICommand {

    private int gameId;
    private CatanColor color;
    private int playerId;
    private String username;

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return CommandExecutionResult with information about the join
     */
    @Override
    public CommandExecutionResult execute() {
        try {
            return Config.facade.join(this.gameId, this.color, this.playerId, this.username);
        } catch (JoinGameException e) {
            // this is so dumb. ugh.
            e.printStackTrace();
        }

        // TODO - SO DUMB
        return null;
    }

    /**
     *
     * @param dto The parameters for the command to be executed
     */
    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final JoinGameDTO tmpDTO = (JoinGameDTO)cookieDTO.getDto();
        this.gameId = tmpDTO.getGameId();
        this.color = tmpDTO.getColor();
        this.playerId = cookieDTO.getPlayerId();
        this.username = cookieDTO.getUsername();
    }
}
