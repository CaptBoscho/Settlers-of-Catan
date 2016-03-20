package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.definitions.CatanColor;
import shared.dto.IDTO;
import shared.dto.JoinGameDTO;

/**
 * A command object that adds a player to a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class JoinCommand implements ICommand {

    private int gameId;
    private CatanColor color;

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return IDTO with information about the join
     */
    @Override
    public CommandExecutionResult execute() {
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        JoinGameDTO tmpDTO = (JoinGameDTO)dto;
        this.gameId = tmpDTO.getGameId();
        this.color = tmpDTO.getColor();
    }
}
