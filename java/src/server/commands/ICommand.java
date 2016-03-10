package server.commands;

import com.google.gson.JsonObject;

/**
 * Created by dannyharding on 3/9/16.
 */
public interface ICommand {
    JsonObject execute();
}
