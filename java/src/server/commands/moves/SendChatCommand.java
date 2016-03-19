package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that sends a chat
 *
 * @author Joel Bradley
 */
public class SendChatCommand implements ICommand {

    /**
     * Constructor
     */
    public SendChatCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Send Chat command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
