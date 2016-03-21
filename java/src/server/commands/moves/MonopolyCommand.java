package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonopolyException;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlayMonopolyDTO;
import shared.model.bank.InvalidTypeException;

/**
 * A command object that plays a monopoly card
 *
 * @author Joel Bradley
 */
public class MonopolyCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private ResourceType resourceType;

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.monopoly(this.gameId, this.playerIndex, this.resourceType);
        } catch (MonopolyException e) {
            throw new CommandExecutionFailedException("MonopolyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlayMonopolyDTO tmpDTO = (PlayMonopolyDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        try {
            this.resourceType = tmpDTO.getResource();
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
    }

}
