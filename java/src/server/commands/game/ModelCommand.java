package server.commands.game;

import server.commands.ICommand;
import server.facade.IFacade;
import shared.dto.GameModelDTO;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public class ModelCommand implements ICommand {

    private int gameID;
    private IFacade facade;
    private GameModelDTO dto;
    /**
     * Constructor
     */
    public ModelCommand(GameModelDTO dto, IFacade fac) {
        gameID = 1;
        this.dto = dto;
        facade = fac;
    }

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return IDTO
     */
    @Override
    public GameModelDTO execute() {
        return facade.getModel(gameID);
    }

}
