package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RobPlayerException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.RobPlayerDTO;
import shared.locations.HexLocation;

/**
 * A command object that robs a player
 *
 * @author Joel Bradley
 */
public final class RobPlayerCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private HexLocation location;
    private int victimIndex;

    /**
     * Communicates with the ServerFacade to carry out the Rob Player command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.robPlayer(this.gameId, this.playerIndex, this.location, this.victimIndex);
        } catch (RobPlayerException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final RobPlayerDTO tmpDTO = (RobPlayerDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getLocation();
        this.victimIndex = tmpDTO.getVictimIndex();
    }

}
