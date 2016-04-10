package server.persistence;

import java.net.URL;

/**
 * A struct-like container for encapsulating information about a plugin
 *
 * @author Derek Argueta
 */
public final class Plugin {

    private String name;
    private URL downloadUrl;

    public Plugin(String name, URL downloadUrl) {
        this.name = name;
        this.downloadUrl = downloadUrl;
    }

    public URL getDownloadUrl() {
        return this.downloadUrl;
    }

    public String getName() {
        return this.name;
    }
}
