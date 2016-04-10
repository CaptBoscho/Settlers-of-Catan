package server.persistence;

import server.persistence.dto.GameDTO;

/**
 * @author Derek Argueta
 */
public interface IDatabase {

    void clear();

    void shutdown();

    void addGame(GameDTO dto);
}
