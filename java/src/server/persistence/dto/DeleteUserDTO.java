package dto;

/**
 * info to delete user
 * Created by boscho on 4/6/16.
 */
public class DeleteUserDTO implements IDTO {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
}
