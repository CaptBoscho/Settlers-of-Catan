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

    public boolean hasContent() {
        return catanGameCookieValue.length() > 0 || catanUserCookieValue.length() > 0;
    }

    public void clearCookies() {
        catanUserCookieValue = "";
        catanGameCookieValue = "";
    }

    public void setCatanGameCookieValue(String catanGameCookieValue) {
        assert catanGameCookieValue != null;
        assert catanGameCookieValue.length() > 0;

        this.catanGameCookieValue = catanGameCookieValue;
    }

    public void setCatanUserCookieValue(String catanUserCookieValue) {
        assert catanUserCookieValue != null;
        assert catanUserCookieValue.length() > 0;

        this.catanUserCookieValue = catanUserCookieValue;
    }
}
