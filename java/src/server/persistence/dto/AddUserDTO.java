package dto;

/**
 * information to add a user to the database
 * Created by boscho on 4/6/16.
 */
public class AddUserDTO implements IDTO {

    public AddUserDTO() {
    }

    private int id;
    private String name;
    private String userName;
    private String password;

    public int getID(){return id;}

    public void setID(int id){ this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
