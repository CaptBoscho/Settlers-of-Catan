package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.BuildSettlementDTO;
import shared.dto.GameModelDTO;

/**
 * A command object that builds a settlement
 *
 * @author Joel Bradley
 */
public class BuildSettlementCommand implements ICommand {

    private BuildSettlementDTO dto;

    public BuildSettlementCommand(BuildSettlementDTO dto) {
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Build Settlement command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
