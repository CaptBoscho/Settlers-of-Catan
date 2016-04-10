package server.persistence;

import server.persistence.dto.GameDTO;
import server.persistence.dto.UserDTO;

/**
 * @author Derek Argueta
 */
public interface IDatabase {

    void clear();

    void shutdown();

    void addUser(UserDTO dto);

    void addGame(GameDTO dto);
}
