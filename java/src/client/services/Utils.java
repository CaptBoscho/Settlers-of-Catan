package client.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.Header;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Common networking utility functions
 *
 * @author Derek Argueta
 */
public class Utils {

    public static String buildUrl(String host, int port) {
        return "http://" + host + ":" + port;
    }

    private static String getStringFromHttpResponse(HttpResponse response) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static String sendPost(String url, JsonObject body) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        if(body != null) {
            StringEntity postingString = null;
            try {
                postingString = new StringEntity(new Gson().toJson(body));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setEntity(postingString);
        }
        post.setHeader("Content-type", "application/json");
        if(UserCookie.getInstance().hasContent()) {
            post.setHeader("Cookie", UserCookie.getInstance().getCompleteCookieValue());
        }
        try {
            assert httpClient != null;
            HttpResponse response = httpClient.execute(post);
            if(response.containsHeader("Set-cookie")) {
                Header cookieHeader = response.getFirstHeader("Set-cookie");
                String cookieVal = cookieHeader.getValue();
                String[] cookies = cookieVal.split(";");
                for(String cookie : cookies) {
                    if(cookie.startsWith("catan.user")) {
                        UserCookie.getInstance().setCatanUserCookieValue(cookie.substring(cookie.indexOf("=") + 1));
                    } else if(cookie.startsWith("catan.game")) {
                        UserCookie.getInstance().setCatanGameCookieValue(cookie.substring(cookie.indexOf("=") + 1));
                    }
                }
            }
            System.out.println(response.getStatusLine());
            return Utils.getStringFromHttpResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendGet(String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        if(UserCookie.getInstance().hasContent()) {
            get.setHeader("Cookie", UserCookie.getInstance().getCompleteCookieValue());
        }
        try {
            HttpResponse response = httpClient.execute(get);
            return Utils.getStringFromHttpResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}