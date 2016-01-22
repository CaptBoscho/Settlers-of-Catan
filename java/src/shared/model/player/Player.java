package shared.model.player;

import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.model.bank.StructureBank;
import shared.definitions.CatanColor;

/**
 * Representation of a player in the game
 */
public class Player {
    private static int _id;
    private Name name;
    private boolean discarded;
    private int monuments;
    private int playerIndex;
    private boolean playedDevCard;
    private int victoryPoints;
    private CatanColor color;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private StructureBank structureBank;

    /**
     * Default Constructor
     */
    public Player() {
        this.victoryPoints = 0;
        this.color = null;
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
        this.structureBank = new StructureBank();
    }

    /**
     * New Player Constructor
     * @param points    Initial points (should be 2 for Catan)
     * @param color     Player Color
     * @param name      Player Name
     * @param index     Player Index
     */
    public Player(int points, CatanColor color, int index, Name name) {
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
        this.structureBank = new StructureBank();
        this.name = name;
        this.playerIndex = index;
    }

    /**
     * Load Player Constructor
     * @param points    Player Point Counter
     * @param color     Player Color
     * @param rCrdBnk   Player Resources
     * @param devCrdBnk Player Development Cards
     * @param sBnk      Player Structures
     * @param name      Player Name
     * @param index     Player Index
     */
    public Player(int points, CatanColor color, ResourceCardBank rCrdBnk, DevelopmentCardBank devCrdBnk, StructureBank sBnk, int index, Name name) {
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = rCrdBnk;
        this.developmentCardBank = devCrdBnk;
        this.structureBank = sBnk;
        this.name = name;
        this.playerIndex = index;
    }

    /**
     * Increments the player's points by 1
     */
    public void incrementPoints() {
        incrementPoints(1);
    }

    /**
     * Increments the player's points by value of increment
     *
     * @param increment Number of points to add to the player's score
     */
    public void incrementPoints(int increment) {
        this.victoryPoints += increment;
    }

    /*===========================================
                   Getters/Setters
     ============================================*/
    public static int get_id() {
        return _id;
    }

    public static void set_id(int _id) {
        Player._id = _id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public boolean hasDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean isPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
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