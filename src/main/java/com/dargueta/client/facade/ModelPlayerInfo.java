package com.dargueta.client.facade;

import com.dargueta.shared.definitions.CatanColor;


public class ModelPlayerInfo{
    private int id;
    private String name;
    private int victorypoints;
    private CatanColor color;
    private boolean longestroad;
    private boolean largestarmy;

    public ModelPlayerInfo(int id, String n, int vp, CatanColor c, boolean lr, boolean la){
        this.id = id;
        this.name = n;
        this.victorypoints = vp;
        this.color = c;
        this.longestroad = lr;
        this.largestarmy = la;
    }

    public int getId(){return this.id;}

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