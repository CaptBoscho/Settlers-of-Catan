package server.persistence.provider;

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
public class DatabaseFacade {
    private HashMap<String, Method> methods;
    private HashMap<String, Object> instances;

    public void loadJar(String url){
        try {
            URL jarURL = new URL(url);
            URLClassLoader child = new URLClassLoader(new URL[] {jarURL}, getClass().getClassLoader());

            Class gameDao = Class.forName("GameDAO",true,child);
            Object game = gameDao.newInstance();
            instances.put("gameDao", game);

            Class[] gameint = new Class[1];
            gameint[0] = Integer.class;
            Class[] gameDto = new Class[1];
            gameDto[0] = GameDTO.class;

            Method getAllGames = gameDao.getDeclaredMethod("getAllGames", null);
            methods.put("getAllGames",getAllGames);
            Method getGameModel = gameDao.getDeclaredMethod("getGameModel",gameint);
            methods.put("getGameModel",getGameModel);
            Method updateGame = gameDao.getDeclaredMethod("updateGame",gameDto);
            methods.put("updateGame",updateGame);
            Method deleteAllGames = gameDao.getDeclaredMethod("deleteAllGames",null);
            methods.put("deleteAllGames",deleteAllGames);
            Method deleteGame = gameDao.getDeclaredMethod("deleteGame",gameint);
            methods.put("deleteGame",deleteGame);

            Class userDao = Class.forName("UserDAO",true,child);
            Object user = userDao.newInstance();
            instances.put("userDao", user);

            Class[] userDto = new Class[1];
            userDto[0] = UserDTO.class;

            Method addUser = userDao.getDeclaredMethod("addUser",userDto);
            methods.put("addUser",addUser);
            Method getUsers = userDao.getDeclaredMethod("getUsers",null);
            methods.put("getUsers",getUsers);
            Method deleteUsers = userDao.getDeclaredMethod("deleteUsers",null);
            methods.put("deleteUsers",deleteUsers);

            Class commandDao = Class.forName("CommandDAO",true,child);
            Object command = commandDao.newInstance();
            instances.put("commandDao",command);

            Class[] commandDto = new Class[1];
            commandDto[0] = CommandDTO.class;

            Method addCommand = commandDao.getDeclaredMethod("addCommand",commandDto);
            methods.put("addCommand",addCommand);
            Method getCommands = commandDao.getDeclaredMethod("getCommands",gameint);
            methods.put("getCommands",getCommands);
            Method getAllCommands = commandDao.getDeclaredMethod("getAllCommands",null);
            methods.put("getAllCommands",getAllCommands);
            Method deleteAllCommands = commandDao.getDeclaredMethod("deleteAllCommands",null);
            methods.put("deleteAllCommands",deleteAllCommands);
            Method deleteCommandsFromGame = commandDao.getDeclaredMethod("deleteCommandsFromGame",gameint);
            methods.put("deleteCommandsFromGame",deleteCommandsFromGame);

        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<GameDTO> getAllGames() throws InvocationTargetException, IllegalAccessException {
        Object result = methods.get("getAllGames").invoke(instances.get("gameDao"));
        return (List<GameDTO>)result;
    }
}
