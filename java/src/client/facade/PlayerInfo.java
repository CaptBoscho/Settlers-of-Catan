package client.facade;

import shared.definitions.CatanColor;


public class PlayerInfo{
    private String name;
    private String uname;
    private CatanColor color;

    public PlayerInfo(String u, String n, CatanColor c){
        this.name = n;
        this.uname = u;
        this.color = c;
    }

    public String getName(){
        return this.name;
    }

    public String getUserName(){
        return this.uname;
    }

    public CatanColor getColor(){
        return this.color;
    }
}