package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.main.Config;
import shared.dto.CreateGameDTO;
import shared.dto.IDTO;

/**
 * A command object that creates a game.
 *
 * @author Danny Harding
 */
public final class CreateCommand implements ICommand {

    private String gameName;
    private boolean randomTiles;
    private boolean randomPorts;
    private boolean randomNumbers;

    /**
     * Communicates with the ServerFacade to carry out the Create game command
     *
     * @return CommandExecutionResult with information about the game creation
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.gameName != null;
        assert !this.gameName.equals("");

        return Config.facade.create(this.gameName, this.randomTiles, this.randomPorts, this.randomNumbers);
    }

    @Override
    public void setParams(IDTO dto) {
        CreateGameDTO tmpDTO = (CreateGameDTO)dto;
        gameName = tmpDTO.getName();
        randomTiles = tmpDTO.isRandomTiles();
        randomPorts = tmpDTO.isRandomPorts();
        randomNumbers = tmpDTO.isRandomNumbers();
    }
}
