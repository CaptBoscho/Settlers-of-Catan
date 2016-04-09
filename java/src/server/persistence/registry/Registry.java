package server.persistence.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import server.exceptions.PluginExistsException;
import server.persistence.plugin.IDatabase;
import server.persistence.provider.DatabaseFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class Registry implements IRegistry {

    private static final String REGISTRY_URL = "http://soc-registry-service.herokuapp.com/";
    private static IRegistry _instance;

    /**
     * Default Constructor
     */
    private Registry(){

    }

    /**
     * Gets the instance of the Register
     * @return
     */
    public static IRegistry getInstance(){
        if(_instance == null)
            _instance = new Registry();

        return _instance;
    }

    /**
     * Checks if the specified plugin exists
     *
     * @param plugin
     * @return
     */
    @Override
    public boolean pluginExists(final String plugin) {
        // -- TODO make an HTTP GET request to /jars
        // -- look through resulting JSON for plugin
        return false;
    }

    /**
     * Gets a database plugin
     *
     * @param plugin
     * @return
     * @throws PluginExistsException
     */
    @Override
    public IDatabase getPlugin(final String plugin) throws PluginExistsException {
        String url = REGISTRY_URL + "/jars";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = null;
        try {
            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rd != null;
        StringBuilder result = new StringBuilder();
        String line = "";
        try {
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonArray stuff = new JsonParser().parse(result.toString()).getAsJsonArray();
        for(final JsonElement obj : stuff) {
            final JsonObject tmp = obj.getAsJsonObject();
            if(tmp.get("originalname").getAsString().contains(plugin)) {
                // -- found it
                final String pathToJar = REGISTRY_URL + tmp.get("filename");
                DatabaseFacade facade = new DatabaseFacade();
                facade.loadJar(pathToJar);
            }
        }
        return null;
    }
}
