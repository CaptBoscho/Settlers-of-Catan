package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Handlers {
    public abstract static class BaseFile implements Route {

        public BaseFile(String rootPath) {
            this.rootPath = rootPath;
        }

        protected String rootPath;
        protected String getRequestPath(HttpExchange exchange){
            return exchange.getRequestURI().getPath().substring(1);
        }

        protected void sendFile(HttpExchange exchange, String filepath) throws IOException{
            try {
                byte[] response = FileUtils.readFile(filepath);
                ArrayList<String> mimetypes = new ArrayList<>();
                mimetypes.add(FileUtils.getMimeType(filepath));
                exchange.getResponseHeaders().put("Content­type", mimetypes);
            } catch (IOException ioe){
                exchange.sendResponseHeaders(404, -1);
                System.out.println("Couldn't find the file " + new
                        File(filepath).getAbsolutePath());
            }
        }
    }

    // get the file from the system
    public static class BasicFile extends BaseFile {
        public BasicFile(String rootPath) {
            super(rootPath);
        }

        public void handle(HttpExchange exchange) throws IOException {
            String filepath = this.rootPath + this.getRequestPath(exchange);
            this.sendFile(exchange, filepath);
        }

        @Override
        public Object handle(Request request, Response response) throws Exception {
            String filepath = this.rootPath + request.pathInfo().substring(1);
            byte[] resp = FileUtils.readFile(filepath);
            ArrayList<String> mimetypes = new ArrayList<>();
            mimetypes.add(FileUtils.getMimeType(filepath));
            response.type(mimetypes.get(0));
            return resp;
        }
    }

    // appends ".json" to the request before getting the proper file from the file system
    public static class JSONAppender extends BaseFile{
        public JSONAppender(String rootPath){ super(rootPath);}

        public void handle(HttpExchange exchange) throws IOException {
            System.out.println( this.rootPath + " ___ " + this.getRequestPath(exchange));
            this.sendFile(exchange, this.rootPath + this.getRequestPath(exchange) + ".json");
        }

        @Override
        public Object handle(Request request, Response response) throws Exception {
            String filepath = this.rootPath + request.pathInfo().substring(1) + ".json";
            byte[] resp = FileUtils.readFile(filepath);
            ArrayList<String> mimetypes = new ArrayList<>();
            mimetypes.add(FileUtils.getMimeType(filepath));
            response.type(mimetypes.get(0));
            return resp;
        }
    }
}