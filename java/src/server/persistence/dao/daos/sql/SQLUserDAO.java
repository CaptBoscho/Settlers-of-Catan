package server.persistence.dao.daos.sql;

import server.persistence.dao.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class SQLUserDAO implements IUserDAO {

    private static SQLUserDAO instance = null;
    private SQLUserDAO() {
    }

    public static IUserDAO getInstance() {
        if(instance == null){
            instance = new SQLUserDAO();
        }
        return instance;
    }

    @Override
    public void createUser(String username, String userID, String password) {

    }

    @Override
    public int findUser(String userID, String password) {
        return -1;
    }
}
