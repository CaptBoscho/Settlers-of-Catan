package shared.model;

import shared.locations.VertexLocation;

/**
 * @name Player
 * @description Player Class - represents a player in the game
 */
public class Player {
    //private Integer id; //// TODO: 1/19/2016 Should we use the index in the list of players as the id or have an actual id?
    private Integer points;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private StructureBank structureBank;

    /**
     * @name Player
     * @description Player class constructor
     */
    public Player(){
        this.points = 2;
        this.resourceCardBank = new ResourceCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing resources
        this.developmentCardBank = new DevelopmentCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing dev cards
        this.structureBank = new StructureBank(null); //// TODO: 1/19/2016 figure out how danny is doing structures
    }

    /**
     * @name incrementPoints
     * @description increments the player's points by 1
     */
    public void incrementPoints(){
        incrementPoints(1);
    }

    /**
     * @name incrementPoints
     * @description increments the player's points by value of increment
     * @param increment
     */
    public void incrementPoints(int increment){
        this.points += increment;
    }

    /*===========================================
                   Getters/Setters
     ============================================*/

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ResourceCardBank getResourceCardBank() {
        return resourceCardBank;
    }

    public void setResourceCardBank(ResourceCardBank resourceCardBank) {
        this.resourceCardBank = resourceCardBank;
    }

    public DevelopmentCardBank getDevelopmentCardBank() {
        return developmentCardBank;
    }

    public void setDevelopmentCardBank(DevelopmentCardBank developmentCardBank) {
        this.developmentCardBank = developmentCardBank;
    }
}
