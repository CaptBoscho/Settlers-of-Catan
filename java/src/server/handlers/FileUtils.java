package server.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class FileUtils { //http://stackoverflow.com/questions/4246360/java­loading­binary­files

    private static final Map<String,String> MIMETYPES = makeMimeTypes();

    private static Map<String,String> makeMimeTypes (){
        final Map<String,String> mimeTypes = new HashMap<String,String>();
        mimeTypes.put(".js", "application/javascript");
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".html", "text/html");
        return mimeTypes;
    }

    static byte[] readFile(final String path) throws IOException {
        // Make a file object from the path name
        final File file = new File(path);
        // Find the size
        final int size = (int)file.length();
        // Create a buffer big enough to hold the file
        final byte[] contents = new byte[size];
        // Create an input stream from the file object
        final FileInputStream in = new FileInputStream(file);
        // Read it all
        in.read(contents);
        // Close the file
        in.close();
        return contents;
    }

    static String getMimeType(final String path){
        final String ending = path.substring(path.lastIndexOf('.'),path.length());
        if (MIMETYPES.get(ending) != null){
            return MIMETYPES.get(ending);
        } else {
            return "";
        }
    }
}