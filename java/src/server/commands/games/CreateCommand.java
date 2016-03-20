package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CreateGameException;
import server.main.Config;
import shared.dto.CreateGameDTO;
import shared.dto.IDTO;

/**
 * A command object that creates a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class CreateCommand implements ICommand {

    private CreateGameDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Create game command
     *
     * @return CommandExecutionResult with information about the game creation
     */
    @Override
    public CommandExecutionResult execute() {
        final String gameName = this.dto.getName();
        final boolean randomTiles = this.dto.isRandomTiles();
        final boolean randomPorts = this.dto.isRandomPorts();
        final boolean randomNumbers = this.dto.isRandomNumbers();
        return Config.facade.create(gameName, randomTiles, randomPorts, randomNumbers);
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (CreateGameDTO)dto;
    }
}
