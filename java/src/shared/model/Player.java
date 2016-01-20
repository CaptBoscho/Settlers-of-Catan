package shared.model;

import shared.definitions.CatanColor;
import shared.locations.VertexLocation;

/**
 * Representation of a player in the game
 */
public class Player {
    private Integer points;
    private CatanColor color;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private StructureBank structureBank;

    /**
     * Default Constructor
     */
    public Player() {
        this.points = 0;
        this.color = null;
        this.resourceCardBank = new ResourceCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing resources
        this.developmentCardBank = new DevelopmentCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing dev cards
        this.structureBank = new StructureBank(null); //// TODO: 1/19/2016 figure out how danny is doing structures
    }

    /**
     * New Player Constructor
     * @param points Initial points (should be 2 for Catan)
     * @param color Player Color
     */
    public Player(Integer points, CatanColor color) {
        this.points = points;
        this.color = color;
        this.resourceCardBank = new ResourceCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing resources
        this.developmentCardBank = new DevelopmentCardBank(null); //// TODO: 1/19/2016 figure out how danny is doing dev cards
        this.structureBank = new StructureBank(null); //// TODO: 1/19/2016 figure out how danny is doing structures
    }

    /**
     * Load Player Constructor
     * @param points Player Point Counter
     * @param color Player Color
     * @param rCrdBnk Player Resources
     * @param devCrdBnk Player Development Cards
     * @param sBnk Player Structures
     */
    public Player(Integer points, CatanColor color, ResourceCardBank rCrdBnk, DevelopmentCardBank devCrdBnk, StructureBank sBnk) {
        this.points = points;
        this.color = color;
        this.resourceCardBank = rCrdBnk; //// TODO: 1/19/2016 figure out how danny is doing resources
        this.developmentCardBank = devCrdBnk; //// TODO: 1/19/2016 figure out how danny is doing dev cards
        this.structureBank = sBnk; //// TODO: 1/19/2016 figure out how danny is doing structures
    }

    /**
     * Increments the player's points by 1
     */
    public void incrementPoints() {
        incrementPoints(1);
    }

    /**
     * Increments the player's points by value of increment
     * @param increment Number of points to add to the player's score
     */
    public void incrementPoints(int increment) {
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

    public CatanColor getColor() {
        return color;
    }

    public void setColor(CatanColor color) {
        this.color = color;
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

    public StructureBank getStructureBank() {
        return structureBank;
    }

    public void setStructureBank(StructureBank structureBank) {
        this.structureBank = structureBank;
    }
}
