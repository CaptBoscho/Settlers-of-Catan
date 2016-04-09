package dto;

/**
 * Created by boscho on 4/6/16.
 */
public class UserDTO {

    private String userName;
    private String password;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public UserDTO(){}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
