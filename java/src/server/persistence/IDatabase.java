package server.persistence;

import server.persistence.dto.CommandDTO;
import server.persistence.dto.GameDTO;
import server.persistence.dto.UserDTO;

import java.util.List;

/**
 * @author Derek Argueta
 */
public interface IDatabase {

    void clear();

    void shutdown();

    int addUser(UserDTO dto);

    List<UserDTO> getUsers();

    int addGame(GameDTO dto);

    List<GameDTO> getAllGames();

    List<CommandDTO> getCommands(int gameId);

    void addCommand(CommandDTO dto);

    void updateGame(GameDTO dto);

    void deleteCommandsFromGame(int gameId);
}
