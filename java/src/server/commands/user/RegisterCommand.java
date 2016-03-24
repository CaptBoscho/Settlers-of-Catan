package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.managers.UserManager;
import shared.dto.AuthDTO;
import shared.dto.IDTO;

/**
 * A command object that registers a player.
 *
 * @author Danny Harding
 */
public final class RegisterCommand implements ICommand {

    private boolean paramsSet = false;
    private String username, password;

    /**
     * Communicates with the ServerFacade to carry out the Register command
     *
     * @return CommandExecutionResult with information about the registration
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.paramsSet;
        assert this.username != null;
        assert this.password != null;

        if(UserManager.getInstance().addUser(this.username, this.password)) {
            final String userId = String.valueOf(UserManager.getInstance().getIdForUser(username));
            CommandExecutionResult result = new CommandExecutionResult("Success");

            // TODO - implement a reasonable cookie scheme
            result.addCookie("catan.user", "%7B%22name%22%3A%22" + username + "%22%2C%22password%22%3A%22" + password + "%22%2C%22playerID%22%3A" + userId + "%7D");
            return result;
        }

        // TODO - throw exception here
        return new CommandExecutionResult("credentials already exist");
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final AuthDTO tmpDTO = (AuthDTO)dto;
        this.username = tmpDTO.getUsername();
        this.password = tmpDTO.getPassword();
    }
}
