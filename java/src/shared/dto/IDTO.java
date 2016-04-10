package shared.dto;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by Kyle 'TMD' Cornelison on 3/16/2016.
 */
public interface IDTO extends Serializable {

    JsonObject toJSON();
}
