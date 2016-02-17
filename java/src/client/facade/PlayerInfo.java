package client.facade;

import shared.definitions.CatanColor;


public class PlayerInfo{
    private String name;
    int victorypoints;
    private CatanColor color;
    boolean longestroad;
    boolean largestarmy;

    public PlayerInfo(String n, int vp, CatanColor c, boolean lr, boolean la){
        this.name = n;
        this.victorypoints = vp;
        this.color = c;
        this.longestroad = lr;
        this.largestarmy = la;
    }

    public String getName(){
        return this.name;
    }

    public Integer getVictoryPoints(){return this.victorypoints;}

    public CatanColor getColor(){
        return this.color;
    }

    public boolean hasLongestRoad(){return this.longestroad;}

    public boolean hasLargestArmy(){return this.largestarmy;}
}