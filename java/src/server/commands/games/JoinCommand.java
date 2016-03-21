package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.JoinGameException;
import server.main.Config;
import shared.definitions.CatanColor;
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

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return CommandExecutionResult with information about the join
     */
    @Override
    public CommandExecutionResult execute() {
        try {
            return Config.facade.join(this.gameId, this.color);
        } catch (JoinGameException e) {
            // this is so dumb. ugh.
            e.printStackTrace();
        }

        // TODO - SO DUMB
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        JoinGameDTO tmpDTO = (JoinGameDTO)dto;
        this.gameId = tmpDTO.getGameId();
        this.color = tmpDTO.getColor();
    }
}
