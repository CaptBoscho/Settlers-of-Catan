package client.services;

/**
 * @author Derek Argueta
 */
public class UserCookie {

    private static UserCookie instance;

    private String catanUserCookieValue = "";
    private String catanGameCookieValue = "";

    protected UserCookie() { }

    public static UserCookie getInstance() {
        if(instance == null) {
            instance = new UserCookie();
        }

        return instance;
    }

    public String getCompleteCookieValue() {
        if(this.catanGameCookieValue.equals("")) {
            return "catan.user=" + this.catanUserCookieValue;
        } else {
            return "catan.user=" + this.catanUserCookieValue + "; " + "catan.game=" + this.catanGameCookieValue;
        }
    }

    public String getCatanGameCookieValue() {
        return catanGameCookieValue;
    }

    public void setCatanGameCookieValue(String catanGameCookieValue) {
        this.catanGameCookieValue = catanGameCookieValue;
    }

    public String getCatanUserCookieValue() {
        return catanUserCookieValue;
    }

    public void setCatanUserCookieValue(String catanUserCookieValue) {
        this.catanUserCookieValue = catanUserCookieValue;
    }
}
