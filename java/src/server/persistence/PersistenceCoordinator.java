package server.persistence;

import server.commands.ICommand;
import server.main.Config;
import server.managers.GameManager;
import server.managers.UserManager;
import server.persistence.dto.CommandDTO;
import server.persistence.dto.GameDTO;
import server.persistence.dto.UserDTO;
import shared.model.game.Game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * A wrapper around the plugin database to add any additional tracking and
 * analytics that the main program may need to know, such as keeping track of
 * how many commands have been committed and thus when to flush the store.
 *
 * @author Derek Argueta
 */
public class PersistenceCoordinator {

    /**
     * This map keeps track of how many commits have been made for a given
     * game. The map is of gameId: commit-count. It is internally implemented
     * using a Hashmap since order doesn't matter.
     */
    private java.util.Map<Integer, Integer> commandCommitCount;

    /**
     * The PersistenceCoordinator uses the Singleton pattern, so this is the
     * single reference of a PersistenceCoordinator for the runtime of the
     * server.
     */
    private static PersistenceCoordinator instance;

    /**
     * The actual data store.
     */
    private IDatabase database;

    /**
     * Hidden constructor to initialize values.
     */
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

    /**
     * Modifies the data store. This allows for a pluggable dependency-injection
     * style system. To change to a new store mid-runtime, just pass in the
     * object for that new store.
     *
     * @param db An IDatabase that provides all core functionality for
     *           persistence
     */
    public static void setDatabase(final IDatabase db) {
        getInstance().database = db;
    }

    public static IDatabase getDatabase() {
        return getInstance().database;
    }

    /**
     * A wrapper function for adding a user to the data store. If the user's
     * ID conflicts with a user that is already in the data store, then the
     * UserManager will update the user's ID to a non-conflicting ID that is
     * returned by the data store.
     *
     * (@pre) The user is only stored in-memory
     * (@post) The user is persisted in a data store and has a non-conflicting
     * ID
     *
     * @param dto The data-transfer-object for the user that is to be stored.
     */
    public static void addUser(final UserDTO dto) {
        assert dto != null;

        int oldId = dto.getId();
        int newId = getInstance().database.addUser(dto);
        if(oldId != newId) {
            UserManager.getInstance().setNewIdForUser(oldId, newId);
        }
    }

    /**
     * A wrapper function for adding a command to the data store. This function
     * takes into account the commit-count for a given game. If the commit-count
     * reaches a certain value, as dictated by command-line parameters, then
     * the game model is updated and all the commands for that game are wiped.
     *
     * @param dto The data-transfer-object for the command that is to be stored.
     */
    public static void addCommand(final CommandDTO dto) {
        assert dto != null;

        getInstance().database.addCommand(dto);
        int commitCount = getInstance().commandCommitCount.get(dto.getGameID());
        commitCount++;
        getInstance().commandCommitCount.put(dto.getGameID(), commitCount);
        if (commitCount % (Config.commandCount - 1) == 0) {
            getInstance().commandCommitCount.put(dto.getGameID(), 0);
            Game game = Config.facade.getGameByID(dto.getGameID());
            GameDTO gameDTO = new GameDTO(dto.getGameID(), game.getTitle(), game.toJSON().toString());
            getInstance().database.updateGame(gameDTO);
            getInstance().database.deleteCommandsFromGame(dto.getGameID());
        }
    }

    /**
     * A wrapper function that adds a game to the data store. If the sent game
     * has an ID that is already taken, the store will return a new ID that
     * doesn't have a conflict. It is the responsibility of this function to
     * change the ID of the game if required. This is required because the model
     * generates IDs, not the database, so the model is unaware if it assigns
     * an ID that is already occupied in the database.
     *
     * TODO - decouple the responsibility of updating the game ID
     *
     * (@pre) The game is only stored in-memory
     * (@post) The game is stored in persistence and has a non-conflicting ID.
     *
     * @param dto A data-transfer-object that contains the information needed
     *            to store a new game.
     */
    public static int addGame(final GameDTO dto) {
        assert dto != null;

        int oldId = dto.getGameID();
        int gameId = getInstance().database.addGame(dto);

        // update the game ID
        GameManager.getInstance().getGameByID(oldId).setId(gameId);

        // update commit count
        getInstance().commandCommitCount.put(gameId, 0);

        return gameId;
    }

    /**
     * Utilizes Java's built-in Serializable interface to convert ICommand
     * objects into a stream of bytes for storage in a database.
     *
     * @param command The command to be serialized.
     * @return A string containing the bytes for the serialized object.
     */
    public static String serializeCommand(final ICommand command) {
        assert command != null;

        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(command);
            so.flush();
            serializedObject = bo.toString("ISO-8859-1");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return serializedObject;
    }

    /**
     * Utilizes Java's built-in Serializable interface to convert an arbitrary
     * stream of bytes into an ICommand object for retrieval of a serialized
     * ICommand in the data store.
     *
     * @param serializedObject A string containing the bytes of an ICommand
     *                         object.
     * @return An ICommand object constructed from the serializedObject.
     */
    public static ICommand deserializeCommand(final String serializedObject) {
        assert serializedObject != null;
        assert serializedObject.length() > 0;

        ICommand command = null;
        try {
            byte b[] = serializedObject.getBytes("ISO-8859-1");
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            command = (ICommand) si.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return command;
    }

    /**
     * Updates a game's commit-count in the map. This function is primarily
     * used when importing data in the server facade to set the commit-count
     * for pre-existing games.
     *
     * @param gameId The ID of the game being updated.
     * @param commits The number of commits to be associated with the game.
     */
    public void setCommitCount(int gameId, int commits) {
        assert gameId >= 0;
        assert commits >= 0;

        getInstance().commandCommitCount.put(gameId, commits);
    }
}
