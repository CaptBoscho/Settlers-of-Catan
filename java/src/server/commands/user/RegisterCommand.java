package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.managers.UserManager;
import shared.dto.AuthDTO;
import shared.dto.IDTO;

/**
 * A command object that registers a player.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class RegisterCommand implements ICommand {

    private String username, password;

    public RegisterCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Register command
     *
     * @return IDTO with information about the registration
     */
    @Override
    public CommandExecutionResult execute() {
        if(UserManager.getInstance().addUser(this.username, this.password)) {
            final String userId = String.valueOf(UserManager.getInstance().getIdForUser(username));
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("name", username);
            result.addCookie("password", password);
            result.addCookie("playerID", userId);
            return result;
        }

        // TODO - throw exception here
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        AuthDTO tmpDTO = (AuthDTO)dto;
        this.username = tmpDTO.getUsername();
        this.password = tmpDTO.getPassword();
    }
}
