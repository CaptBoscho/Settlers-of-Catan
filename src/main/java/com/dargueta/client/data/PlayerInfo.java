package com.dargueta.client.data;

import com.dargueta.shared.definitions.CatanColor;

/**
 * Used to pass player information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique player ID</li>
 * <li>PlayerIndex: Player's order in the game [0-3]</li>
 * <li>Name: Player's name (non-empty string)</li>
 * <li>Color: Player's color (cannot be null)</li>
 * </ul>
 * 
 */
public class PlayerInfo {
	
	private int id;
    private int playerIndex;
    private String name;
    private CatanColor color;
    private int victory;
    private boolean longestroad;
    private boolean largestarmy;
	
	public PlayerInfo() {
		setId(-1);
		setPlayerIndex(-1);
		setName("");
		setColor(CatanColor.WHITE);
        this.longestroad = false;
        this.largestarmy = false;
        victory = 0;
	}

	public PlayerInfo(String name, int victorypoints, CatanColor color, int identifier, int index, boolean longestRoad, boolean largestArmy){
        assert(name != null);
        assert(victorypoints >=0);
        //assert(color != null);
        assert(identifier >= 0);
        //assert(index >= 0);
        assert(index<4);

        this.name = name;
        this.victory = victorypoints;
        this.color = color;
        this.id = identifier;
        this.playerIndex = index;
        this.longestroad = longestRoad;
        this.largestarmy = largestArmy;
    }
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getPlayerIndex()
	{
		return playerIndex;
	}
	
	public void setPlayerIndex(int playerIndex)
	{
		this.playerIndex = playerIndex;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public CatanColor getColor()
	{
		return color;
	}
	
	public void setColor(CatanColor color)
	{
		this.color = color;
	}

    //wrote these things
    public void setVictoryPoints(int vp){this.victory = vp;}

    public int getVictoryPoints(){return this.victory;}

    public void setLongestRoad(boolean lr){this.longestroad = lr;}

    public boolean hasLongestRoad(){return this.longestroad;}

    public void setLargestArmy(boolean la){this.largestarmy = la;}

    public boolean hasLargestArmy(){return this.largestarmy;}

	@Override
	public int hashCode()
	{
		return 31 * this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerInfo other = (PlayerInfo) obj;
		
		return this.id == other.id;
	}
}
