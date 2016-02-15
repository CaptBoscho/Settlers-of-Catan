package shared.model.bank;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * A bank owned by a Player which holds a count of the owners remaining structures
 *
 * @author Danny Harding
 */
public final class StructureBank {
    private final int MAX_ROADS = 15;
    private final int MAX_SETTLEMENTS = 5;
    private final int MAX_CITIES = 4;

    int availableRoads;
    int availableCities;
    int availableSettlements;

    /**
     * Creates a full structure bank, setting number of available structures to their MAX.
     */
    public StructureBank() {
        availableRoads = MAX_ROADS;
        availableCities = MAX_CITIES;
        availableSettlements = MAX_SETTLEMENTS;
    }

    /**
     * A constructor used to initiate an already made structure bank for a player.
     * @param availableRoads Number of roads the player has left to play.
     * @param availableSettlements Number of settlements the player has left to play.
     * @param availableCities Number of cities the player has left to play.
     */
    public StructureBank(int availableRoads, int availableSettlements, int availableCities) {
        this.availableRoads = availableRoads;
        this.availableSettlements = availableSettlements;
        this.availableCities = availableCities;
    }

    public boolean canBuildRoad() {
        return availableRoads > 0;
    }

    public void buildRoad() {
        availableRoads--;
    }

    public boolean canBuildSettlement() {
        return availableSettlements > 0;
    }

    public void buildSettlement() {
        availableSettlements--;
    }

    public boolean canBuildCity() {
        return availableCities > 0;
    }

    public void buildCity() {
        availableCities--;
    }

    public int getAvailableCities() {
        return availableCities;
    }

    public int getAvailableRoads() {
        return availableRoads;
    }

    public int getAvailableSettlements() {
        return availableSettlements;
    }
}
