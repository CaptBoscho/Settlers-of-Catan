package shared.model.bank;

/**
 * A bank owned by a Player which holds a count of the owners remaining structures
 *
 * Created by Danny on 1/20/16.
 */
public class StructureBank {
    private final int MAX_ROADS = 15;
    private final int MAX_CITIES = 4;
    private final int MAX_SETTLEMENTS = 5;

    int availableRoads;
    int availableCities;
    int availableSettlements;

    /**
     * Creates a full structure bank, setting number of available structures to their MAX.
     */
    public void StructureBank() {

    }
}
