package daos;

import dto.IDTO;
import dto.UserDTO;
import exceptions.UserTableException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/5/2016.
 */
public interface IUserDAO {
    /**
     * Handles adding a user
     * @param dto
     */
    void addUser(UserDTO dto) throws UserTableException, SQLException;

    /**
     * Handles verifying user which returns userID
     *
     * @return
     */
    List<UserDTO> getUsers() throws SQLException, UserTableException;


    /**
     * delete a user
     */
    void deleteUsers() throws SQLException, UserTableException;
}
