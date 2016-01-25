package shared.model.bank;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * A bank owned by a Player which holds a count of the owners remaining structures
 *
 * @author Danny Harding
 */
public class StructureBank implements JsonSerializable {
    private final int MAX_ROADS = 15;
    private final int MAX_CITIES = 4;
    private final int MAX_SETTLEMENTS = 5;

    int availableRoads;
    int availableCities;
    int availableSettlements;

    /**
     * Creates a full structure bank, setting number of available structures to their MAX.
     */
    public StructureBank() {

    }

    /**
     * Construct a StructureBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public StructureBank(JsonObject json) {

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
