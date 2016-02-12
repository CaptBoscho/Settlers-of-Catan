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
    private int brickCount;
    private int oreCount;
    private int sheepCount;
    private int wheatCount;
    private int woodCount;

    /**
     *
     * @param playerIndex  Who's discarding
     * @param discardedBrick
     * @param discardedOre
     * @param discardedSheep
     * @param discardedWheat
     * @param discardedWood
     */
    public DiscardCardsDTO(int playerIndex, int discardedBrick, int discardedOre, int discardedSheep, int discardedWheat, int discardedWood) {
        assert playerIndex >= 0;
        assert discardedBrick >= 0;
        assert discardedOre >= 0;
        assert discardedSheep >= 0;
        assert discardedWheat >= 0;
        assert discardedWood >= 0;
        assert discardedBrick + discardedOre + discardedSheep + discardedWheat + discardedWood > 0;

        this.playerIndex = playerIndex;
        this.brickCount = discardedBrick;
        this.oreCount = discardedOre;
        this.sheepCount = discardedSheep;
        this.wheatCount = discardedWheat;
        this.woodCount = discardedWood;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {

        JsonObject inner = new JsonObject();
        inner.addProperty("brick", this.brickCount);
        inner.addProperty("ore", this.oreCount);
        inner.addProperty("sheep", this.sheepCount);
        inner.addProperty("wheat", this.wheatCount);
        inner.addProperty("wood", this.woodCount);

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "discardCards");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.add("discardedCards", inner);
        return obj;
    }
}
