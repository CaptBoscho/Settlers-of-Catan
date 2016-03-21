package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.YearOfPlentyException;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlayYOPCardDTO;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public class YearOfPlentyCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private ResourceType resourceOne;
    private ResourceType resourceTwo;

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.yearOfPlenty(this.gameId, this.playerIndex, this.resourceOne, this.resourceTwo);
        } catch (YearOfPlentyException e) {
            throw new CommandExecutionFailedException("YearOfPlentyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlayYOPCardDTO tmpDTO = (PlayYOPCardDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.resourceOne = tmpDTO.getResource1();
        this.resourceTwo = tmpDTO.getResource2();
    }

}
