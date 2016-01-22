package client.services;

/**
 * Created by derek on 1/17/16.
 */
public class UtilService {

    private static final String BASE_PATH = "/util";

    public static boolean changeLogLevel(String logLevel) {
        final String endpoint = BASE_PATH + "/changeLogLevel";
        return true;
    }
}
