package shared.model.ai;

/**
 * Created by Kyle 'TMD' Cornelison on 3/19/2016.
 */
public enum AIType {
    SHEEP,
    LARGEST_ARMY,
    LONGEST_ROAD;

    public static String typeToString(AIType type) {
        if(type == AIType.SHEEP) {
            return "Sheep";
        } else if(type == AIType.LARGEST_ARMY) {
            return "Largest Army";
        } else if(type == AIType.LONGEST_ROAD){
            return "Longest Road";
        }
        return null;
    }

    public static AIType translateFromString(String type) {
        if(type.equals("Sheep")) {
            return AIType.SHEEP;
        } else if(type.equals("Largest Army")) {
            return AIType.LARGEST_ARMY;
        } else if(type.equals("Longest Road")) {
            return AIType.LONGEST_ROAD;
        }
        return null;
    }

}
