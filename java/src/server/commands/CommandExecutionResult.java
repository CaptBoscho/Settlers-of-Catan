package server.commands;

import java.util.List;

/**
 * This is a transportation object to store the result of command executions.
 * It emulates some of the fields of an HTTP response, so that we can use an
 * actual organized object to store results, rather than relying on a more dangerous
 * or unstable protocol such as arbitrary strings.
 *
 * @author Derek Argueta
 */
public class CommandExecutionResult {
    private List<String> newCookies;
    private String body;

    public CommandExecutionResult(final String info) {
        body = info;
    }

    public List<String> getNewCookies() {
        return this.newCookies;
    }

    public String getBody() {
        return this.body;
    }

    public boolean hasNewCookies() {
        return this.newCookies != null && this.newCookies.size() > 0;
    }
}
