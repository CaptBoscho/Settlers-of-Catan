package server.persistence;

import java.net.URL;

/**
 * A struct-like container for encapsulating information about a plugin. This
 * class is primarily used as a DTO-style object between downloading a plugin
 * and loading the JAR into the runtime.
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
