package client.services;

import client.misc.MessageView;
import client.services.exceptions.*;
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

/**
 * Common networking utility functions
 *
 * @author Derek Argueta
 */
final class Utils {

    private static void displayBadIOMessage() {
        MessageView view = new MessageView();
        view.setTitle("Bad Connection - IOException");
        view.setMessage("Unable to communicate with the server");
        view.showModal();
    }

    static String buildUrl(String host, int port) {
        assert host != null;
        assert host.length() > 0;
        assert port > 0;

        return "http://" + host + ":" + port;
    }

    private static String getStringFromHttpResponse(final HttpResponse response) {
        assert response != null;

        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    static String sendPost(final String url, JsonObject body) throws BadHttpRequestException {
        assert url != null;
        assert url.length() > 0;

        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);

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
            try {
                post.setHeader("Cookie", UserCookie.getInstance().getCompleteCookieValue());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            assert httpClient != null;
            final HttpResponse response = httpClient.execute(post);
            switch(response.getStatusLine().getStatusCode()) {
                case 400:
                    throw new BadRequestException();
                case 401:
                    throw new UnauthorizedException();
                case 404:
                    throw new NotFoundException();
                case 500:
                    throw new InternalServerErrorException();
            }
            if(response.containsHeader("Set-cookie")) {
                final Header cookieHeader = response.getFirstHeader("Set-cookie");
                UserCookie.getInstance().setCookies(cookieHeader.getValue());
            }
            return Utils.getStringFromHttpResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            displayBadIOMessage();
        }
        return null;
    }

    static String sendGet(final String url) {
        assert url != null;
        assert url.length() > 0;

        final HttpGet get = new HttpGet(url);
        if(UserCookie.getInstance().hasContent()) {
            try {
                get.setHeader("Cookie", UserCookie.getInstance().getCompleteCookieValue());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            return Utils.getStringFromHttpResponse(HttpClientBuilder.create().build().execute(get));
        } catch (IOException e) {
            e.printStackTrace();
            displayBadIOMessage();
        }
        return null;
    }
}