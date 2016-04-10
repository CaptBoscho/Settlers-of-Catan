package server.persistence.database;

import server.persistence.dto.CommandDTO;
import server.persistence.dto.GameDTO;
import server.persistence.dto.UserDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by boscho on 4/9/16.
 */
public class DatabaseFacade implements IDatabase {
    private HashMap<String, Method> methods;
    private HashMap<String, Object> instances;

    public DatabaseFacade() {
        instances = new HashMap<>();
        methods = new HashMap<>();
    }

    @Override
    public void loadJar(String url){
        try {

            instances = new HashMap<>();
            methods = new HashMap<>();

            URL jarURL = new URL(url);
            URLClassLoader child = new URLClassLoader(new URL[] {jarURL}, getClass().getClassLoader());


            Class gameDao = Class.forName("daos.GameDAO", true, child);
            Object game = gameDao.newInstance();
            instances.put("gameDao", game);

            Method getAllGames = gameDao.getDeclaredMethod("getAllGames");
            methods.put("getAllGames",getAllGames);
            Method getGameModel = gameDao.getDeclaredMethod("getGameModel", Integer.TYPE);
            methods.put("getGameModel",getGameModel);
            Method updateGame = gameDao.getDeclaredMethod("updateGame", GameDTO.class);
            methods.put("updateGame",updateGame);
            Method deleteAllGames = gameDao.getDeclaredMethod("deleteAllGames");
            methods.put("deleteAllGames",deleteAllGames);
            Method deleteGame = gameDao.getDeclaredMethod("deleteGame", Integer.TYPE);
            methods.put("deleteGame",deleteGame);
            Method addGameObject = gameDao.getDeclaredMethod("addGameObject", GameDTO.class);
            methods.put("addGameObject",addGameObject);

            Class userDao = Class.forName("daos.UserDAO", true, child);
            Object user = userDao.newInstance();
            instances.put("userDao", user);

            Method addUser = userDao.getDeclaredMethod("addUser", UserDTO.class);
            methods.put("addUser",addUser);
            Method getUsers = userDao.getDeclaredMethod("getUsers");
            methods.put("getUsers",getUsers);
            Method deleteUsers = userDao.getDeclaredMethod("deleteUsers");
            methods.put("deleteUsers",deleteUsers);

            Class commandDao = Class.forName("daos.CommandDAO", true, child);
            Object command = commandDao.newInstance();
            instances.put("commandDao",command);

            Method addCommand = commandDao.getDeclaredMethod("addCommand", CommandDTO.class);
            methods.put("addCommand",addCommand);
            Method getCommands = commandDao.getDeclaredMethod("getCommands", Integer.TYPE);
            methods.put("getCommands",getCommands);
            Method getAllCommands = commandDao.getDeclaredMethod("getAllCommands");
            methods.put("getAllCommands",getAllCommands);
            Method deleteAllCommands = commandDao.getDeclaredMethod("deleteAllCommands");
            methods.put("deleteAllCommands",deleteAllCommands);
            Method deleteCommandsFromGame = commandDao.getDeclaredMethod("deleteCommandsFromGame", Integer.TYPE);
            methods.put("deleteCommandsFromGame",deleteCommandsFromGame);

        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        //// TODO: 4/9/16 implement this
    }

    @Override
    public List<GameDTO> getAllGames() throws InvocationTargetException, IllegalAccessException {
        Object result = methods.get("getAllGames").invoke(instances.get("gameDao"));
        return (List<GameDTO>)result;
    }

    @Override
    public void addGameObject(GameDTO dto) throws InvocationTargetException, IllegalAccessException {
        methods.get("addGameObject").invoke(instances.get("gameDao"), dto);
    }

    @Override
    public GameDTO getGameModel(int gameId) throws InvocationTargetException, IllegalAccessException {
        Object result = methods.get("getGameModel").invoke(instances.get("gameDao"),gameId);
        return (GameDTO) result;
    }

    @Override
    public void updateGame(GameDTO dto) throws InvocationTargetException, IllegalAccessException {
        methods.get("updateGame").invoke(instances.get("gameDao"),dto);
    }

    @Override
    public void deleteAllGames() throws InvocationTargetException, IllegalAccessException {
        methods.get("deleteAllGames").invoke(instances.get("gameDao"));
    }

    @Override
    public void deleteGame(int gameId) throws InvocationTargetException, IllegalAccessException {
        methods.get("deleteGame").invoke(instances.get("gameDao"),gameId);
    }

    @Override
    public void addCommand(CommandDTO dto) throws InvocationTargetException, IllegalAccessException {
        methods.get("addCommand").invoke(instances.get("commandDao"),dto);
    }

    @Override
    public List<CommandDTO> getCommands(int gameId) throws InvocationTargetException, IllegalAccessException {
        Object result = methods.get("getCommands").invoke(instances.get("commandDao"),gameId);
        return (List<CommandDTO>) result;
    }

    @Override
    public List<CommandDTO> getAllCommands() throws InvocationTargetException, IllegalAccessException {
        Object result = methods.get("getCommands").invoke(instances.get("commandDao"));
        return (List<CommandDTO>) result;
    }

    @Override
    public void deleteAllCommands() throws InvocationTargetException, IllegalAccessException {
        methods.get("deleteAllCommands").invoke(instances.get("commandDao"));
    }

    @Override
    public void deleteCommandsFromGame(int gameId) throws InvocationTargetException, IllegalAccessException {
        methods.get("deleteCommandsFromGame").invoke(instances.get("commandDao"));
    }

    @Override
    public void addUser(UserDTO dto) throws InvocationTargetException, IllegalAccessException {
        methods.get("addUser").invoke(instances.get("userDao"),dto);
    }

    @Override
    public List<UserDTO> getUsers() throws InvocationTargetException, IllegalAccessException {
        System.out.println(methods.toString());
        System.out.println(instances.toString());
        System.out.println(methods.containsKey("getUsers"));
        System.out.println(instances.containsKey("userDao"));
        Object result = methods.get("getUsers").invoke(instances.get("userDao"));
        return (List<UserDTO>) result;
    }

    @Override
    public void deleteUsers() throws InvocationTargetException, IllegalAccessException {
        methods.get("deleteUsers").invoke(instances.get("userDao"));
    }
}
