package shared.model.game.trade;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trade done between two players
 *
 * @author Danny Harding
 */
public final class Trade {
    TradePackage package1;
    TradePackage package2;
    private int sender;
    private int receiver;
    private List<ResourceType> sending = new ArrayList<>();
    private List<ResourceType> receiving = new ArrayList<>();
    private int wood = 0;
    private int brick = 0;
    private int wheat = 0;
    private int sheep = 0;
    private int ore = 0;
    private boolean active = false;

    public Trade(TradePackage package1, TradePackage package2) {
        assert package1 != null;
        assert package1.getUserID() >= 0;
        assert package2 != null;
        assert package2.getUserID() >= 0;
        assert !package1.equals(package2);

        this.package1 = package1;
        this.package2 = package2;
    }

    public Trade() {
        sender = -1;
        receiver = -1;
        wood = 0;
        brick = 0;
        wheat = 0;
        sheep = 0;
        ore = 0;
        active = false;
    }

    public TradePackage getPackage1() {
        return package1;
    }

    public TradePackage getPackage2() {
        return package2;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public int getWood() {
        return wood;
    }

    public int getWheat() {
        return wheat;
    }

    public int getBrick() {
        return brick;
    }

    public int getSheep() {
        return sheep;
    }

    public int getOre() {
        return ore;
    }

    public boolean isActive(){return active;}

    public void setActive(boolean act){active = act;}
    /**
     * Constructs a Trade object from a JSON blob
     *
     * @param json the JSON representation of the object
     */
    public Trade(JsonObject json) {
        sender = json.get("sender").getAsInt();
        receiver = json.get("receiver").getAsInt();

        JsonObject jj = json.get("offer").getAsJsonObject();
        brick = jj.get("brick").getAsInt();
        ore = jj.get("ore").getAsInt();
        sheep = jj.get("sheep").getAsInt();
        wheat = jj.get("wheat").getAsInt();
        wood = jj.get("wood").getAsInt();
        active = true;
    }

    /**
     * Gives resources from package1 to Player from package2 and vice versa.
     * (@pre) package1 and package2 both have at least 1 resource
     *
     * (@post) The resource(s) from package1 are now in package2, and vice versa
     */
    public void switchResources() {
        final List<ResourceType> ghost = package1.getResources();
        package1.setResources(package2.getResources());
        package2.setResources(ghost); //todo JUnit tests
    }

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        int brickCount = 0;
        int oreCount = 0;
        int sheepCount = 0;
        int wheatCount = 0;
        int woodCount = 0;
        for(ResourceType res : this.package1.getResources()) {
            switch(res) {
                case BRICK:
                    brickCount += 1;
                    break;
                case ORE:
                    oreCount += 1;
                    break;
                case SHEEP:
                    sheepCount += 1;
                    break;
                case WHEAT:
                    wheatCount += 1;
                    break;
                case WOOD:
                    woodCount += 1;
            }
        }

        for(ResourceType res : this.package2.getResources()) {
            switch(res) {
                case BRICK:
                    brickCount -= 1;
                    break;
                case ORE:
                    oreCount -= 1;
                    break;
                case SHEEP:
                    sheepCount -= 1;
                    break;
                case WHEAT:
                    wheatCount -= 1;
                    break;
                case WOOD:
                    woodCount -= 1;
            }
        }

        final JsonObject obj = new JsonObject();
        obj.addProperty("sender",sender);
        obj.addProperty("receiver",receiver);

        JsonObject offer = new JsonObject();
        offer.addProperty("brick", brickCount);
        offer.addProperty("ore", oreCount);
        offer.addProperty("sheep", sheepCount);
        offer.addProperty("wheat", wheatCount);
        offer.addProperty("wood", woodCount);
        obj.add("offer",offer);
        return obj;
    }
}
