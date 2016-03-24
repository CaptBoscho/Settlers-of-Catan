package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.LoginException;
import server.main.Config;
import server.managers.UserManager;
import shared.dto.AuthDTO;
import shared.dto.IDTO;

/**
 * A command object that logs a player in
 *
 * @author Danny Harding
 */
public final class LoginCommand implements ICommand {

    private boolean paramsSet = false;
    private String username, password;

    /**
     * Communicates with the ServerFacade to carry out the Login command
     *
     * @return CommandExecutionResult with information about the login
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.paramsSet;
        assert this.username != null;
        assert this.password != null;

        if(Config.facade.login(this.username, this.password)) {
            final String userId = String.valueOf(UserManager.getInstance().getIdForUser(username));
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("catan.user", "%7B%22name%22%3A%22" + username + "%22%2C%22password%22%3A%22" + password + "%22%2C%22playerID%22%3A" + userId + "%7D");
            return result;
        } else {
            CommandExecutionResult result = new CommandExecutionResult("Failed");
            result.triggerError(401);
            return result;
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;
        assert dto instanceof AuthDTO;

        this.paramsSet = true;
        final AuthDTO tmpDTO = (AuthDTO)dto;
        this.username = tmpDTO.getUsername();
        this.password = tmpDTO.getPassword();
    }
}
