package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.managers.UserManager;

/**
 * A command object that registers a player.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class RegisterCommand implements ICommand {

    private String username, password;

    public RegisterCommand(final String username, final String password) {
        this.username = username;
        this.password = password;

    }

    /**
     * Communicates with the ServerFacade to carry out the Register command
     *
     * @return IDTO with information about the registration
     */
    @Override
    public CommandExecutionResult execute() {
        if(UserManager.getInstance().addUser(this.username, this.password)) {
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("name", username);
            result.addCookie("password", password);
            return result;
        }

        // TODO - throw exception here
        return null;
    }
}
