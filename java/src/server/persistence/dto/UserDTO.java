package server.persistence.dto;

/**
 * Created by boscho on 4/6/16.
 */
public class UserDTO {

    private String userName;
    private String password;
    private int id;

    public UserDTO (int ID, String userName, String password) {
        this.id = ID;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
}
