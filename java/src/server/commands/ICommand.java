package server.commands;

import shared.dto.IDTO;

import server.exceptions.CommandExecutionFailedException;

/**
 * A parent interface for all command objects.  The execute function in each implementation of this interface will
 * carry out a task in the server to get the desires information.
 *
 * Created by Danny Harding on 3/9/16.
 */
public interface ICommand {

    /**
     * The main function on child classes of this interface.  It can be called to activate the set of instructions
     * indicated by that class.
     *
     * @return IDTO with information pertaining to the execute function of the given Command object
     */
    IDTO execute() throws CommandExecutionFailedException;
}
