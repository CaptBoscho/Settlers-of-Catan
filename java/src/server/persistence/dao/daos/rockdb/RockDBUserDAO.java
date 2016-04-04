package server.persistence.dao.daos.rockdb;

import server.persistence.dao.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RockDBUserDAO implements IUserDAO {

    private static RockDBUserDAO instance = null;

    private RockDBUserDAO(){

    }

    public static IUserDAO getInstance() {
        if(instance == null){
            instance = new RockDBUserDAO();
        }
        return instance;
    }

    @Override
    public void createUser(String username, String userID, String password) {

    }

    @Override
    public int findUser(String userID, String password) {
        return 0;
    }
}
