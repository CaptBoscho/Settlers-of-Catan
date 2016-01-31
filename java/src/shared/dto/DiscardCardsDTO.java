package shared.dto;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;

import java.util.List;

/**
 * @author Derek Argueta
 */
public class DiscardCardsDTO implements JsonSerializable {

    private int playerIndex;
    private List<ResourceType> resourceList;

    /**
     *
     * @param playerIndex  Who's discarding
     * @param resourceList
     */
    public DiscardCardsDTO(int playerIndex, List<ResourceType> resourceList) {
        assert playerIndex >= 0;
        assert resourceList != null;
        assert resourceList.size() > 0;
        this.playerIndex = playerIndex;
        this.resourceList = resourceList;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
