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
public final class MonopolyCommand implements ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;
    private ResourceType resourceType;

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert resourceType != null;

        try {
            return Config.facade.monopoly(this.gameId, this.playerIndex, this.resourceType);
        } catch (MonopolyException e) {
            throw new CommandExecutionFailedException("MonopolyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
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
