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
    private TradePackage package1;
    private TradePackage package2;
    private int sender;
    private int receiver;
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

        this.sender = package1.getUserID();
        this.receiver = package2.getUserID();

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

    public void setSender(int sender1){sender = sender1;}

    public void setReceiver(int receiver1){receiver = receiver1;}

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

    private void createTradePackages(){
        List<ResourceType> sending = new ArrayList<>();
        List<ResourceType> receiving = new ArrayList<>();
        if(brick <0){
            int brick1 = brick * -1;
            for(int i=0; i<brick1; i++){
                receiving.add(ResourceType.BRICK);
            }
        }else{
            for(int i=0; i<brick; i++){
                sending.add(ResourceType.BRICK);
            }
        }
        if(wood <0){
            int wood1 = wood * -1;
            for(int i=0; i<wood1; i++){
                receiving.add(ResourceType.BRICK);
            }
        }else{
            for(int i=0; i<wood; i++){
                sending.add(ResourceType.WOOD);
            }
        }
        if(wheat <0){
            int wheat1 = wheat* -1;
            for(int i=0; i<wheat1; i++){
                receiving.add(ResourceType.WHEAT);
            }
        }else{
            for(int i=0; i<wheat; i++){
                sending.add(ResourceType.WHEAT);
            }
        }
        if(sheep < 0){
            int sheep1 = sheep * -1;
            for(int i=0; i<sheep1; i++){
                receiving.add(ResourceType.SHEEP);
            }
        }else{
            for(int i=0; i<sheep; i++){
                sending.add(ResourceType.SHEEP);
            }
        }
        if(ore < 0){
            int ore1 = ore * -1;
            for(int i=0; i<ore1; i++){
                receiving.add(ResourceType.ORE);
            }
        }else{
            for(int i=0; i<ore; i++){
                sending.add(ResourceType.ORE);
            }
        }

        package1 = new TradePackage(sender, sending);
        package2 = new TradePackage(receiver, receiving);
    }

    public void setActive(boolean act){active = act;}
    /**
     * Constructs a Trade object from a JSON blob
     *
     * @param json the JSON representation of the object
     */
    public Trade(JsonObject json) {
        brick = json.get("brick").getAsInt();
        ore = json.get("ore").getAsInt();
        sheep = json.get("sheep").getAsInt();
        wheat = json.get("wheat").getAsInt();
        wood = json.get("wood").getAsInt();
        active = true;
        createTradePackages();
    }

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {


        final JsonObject obj = new JsonObject();
        obj.addProperty("playerIndex",sender);

        obj.add("offer",resourceListToJson());

        obj.addProperty("receiver",receiver);
        return obj;
    }

    public JsonObject resourceListToJson(){
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
        JsonObject offer = new JsonObject();
        offer.addProperty("brick", brickCount);
        offer.addProperty("ore", oreCount);
        offer.addProperty("sheep", sheepCount);
        offer.addProperty("wheat", wheatCount);
        offer.addProperty("wood", woodCount);
        return offer;
    }
}
