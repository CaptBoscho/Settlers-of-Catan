package server.commands;

import server.exceptions.CommandExecutionFailedException;
import shared.dto.IDTO;

import java.io.Serializable;

/**
 * A parent interface for all command objects.  The execute function in each implementation of this interface will
 * carry out a task in the server to get the desires information.
 *
 * @author Danny Harding
 */
public interface ICommand extends Serializable {

    /**
     * The main function on child classes of this interface.  It can be called to activate the set of instructions
     * indicated by that class.
     *
     * @return CommandExecutionResult with information pertaining to the execute function of the given Command object
     */
    CommandExecutionResult execute() throws CommandExecutionFailedException;

    void setParams(IDTO dto);
}
