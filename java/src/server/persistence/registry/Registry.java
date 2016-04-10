package server.persistence.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import server.exceptions.PluginNotFoundException;
import server.main.Config;
import server.persistence.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class Registry {

    private static final String REGISTRY_URL = "http://soc-registry-service.herokuapp.com/";
    private static Registry _instance;

    /**
     * Default Constructor
     */
    private Registry() {

    }

    /**
     * Gets the instance of the Register
     * @return
     */
    public static Registry getInstance(){
        if(_instance == null)
            _instance = new Registry();

        return _instance;
    }

    /**
     * Gets a database plugin
     *
     * @param plugin
     * @return
     * @throws PluginNotFoundException
     */
    public Plugin getPlugin(final String plugin) throws PluginNotFoundException {
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
        }

        BufferedReader rd = null;
        try {
            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rd != null;
        StringBuilder result = new StringBuilder();
        String line;
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
            final String fileName = tmp.get("originalname").getAsString();
            if(fileName.contains(plugin)) {
                // -- found it
                final String pathToJar = (REGISTRY_URL + tmp.get("filename")).replace("\"", "");
                System.out.println("Fetching plugin JAR from " + pathToJar);
                try {
                    return new Plugin(fileName, new URL(pathToJar));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new PluginNotFoundException();
    }
}
