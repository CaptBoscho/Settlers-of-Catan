package server.persistence;

import server.persistence.dto.UserDTO;

/**
 * A wrapper around the plugin database to add any additional tracking and
 * analytics that the main program may need to know, such as keeping track of
 * how many commands have been committed and thus when to flush the store.
 *
 * @author Derek Argueta
 */
public class PersistenceCoordinator {

    private int commandCommitCount;
    private static PersistenceCoordinator instance;
    private IDatabase database;

    private PersistenceCoordinator() {
        this.commandCommitCount = 0;
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
}
