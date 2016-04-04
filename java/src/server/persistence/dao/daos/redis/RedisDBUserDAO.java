package server.persistence.dao.daos.redis;

import server.persistence.dao.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RedisDBUserDAO implements IUserDAO {

    private static RedisDBUserDAO instance = null;

    private RedisDBUserDAO(){

    }

    public static IUserDAO getInstance() {
        if(instance == null){
            instance = new RedisDBUserDAO();
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
