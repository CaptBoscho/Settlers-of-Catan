package server.persistence;

import server.main.Config;
import server.persistence.dto.CommandDTO;
import server.persistence.dto.UserDTO;

import java.util.HashMap;

/**
 * A wrapper around the plugin database to add any additional tracking and
 * analytics that the main program may need to know, such as keeping track of
 * how many commands have been committed and thus when to flush the store.
 *
 * @author Derek Argueta
 */
public class PersistenceCoordinator {

    private java.util.Map<Integer, Integer> commandCommitCount;
    private static PersistenceCoordinator instance;
    private IDatabase database;

    private PersistenceCoordinator() {
        this.commandCommitCount = new HashMap<>();
        this.database = null;
    }

    public static PersistenceCoordinator getInstance() {
        if(instance == null) {
            instance = new PersistenceCoordinator();
        }
        return instance;
    }

    public static void setDatabase(IDatabase db) {
        getInstance().database = db;
    }

    public static IDatabase getDatabase() {
        return getInstance().database;
    }

    public static void addUser(UserDTO dto) {
        getInstance().database.addUser(dto);
    }

    public static void addCommand(CommandDTO dto) {
        getInstance().database.addCommand(dto);
        getInstance().commandCommitCount++;
        if(getInstance().commandCommitCount % Config.commandCount == 0) {
            getInstance().commandCommitCount = 0;
            getInstance().database.updateGame(dto);
            getInstance().database.deleteCommandsFromGame(0);
        }
    }
}
