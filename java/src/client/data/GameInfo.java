package client.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

/**
 * Used to pass game information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique game ID</li>
 * <li>Title: game title (non-empty string)</li>
 * <li>Players: List of players who have joined the game (can be empty)</li>
 * </ul>
 * 
 */
public final class GameInfo {
	private int id;
	private String title;
	private List<PlayerInfo> players;
	
	public GameInfo() {
		setId(-1);
		setTitle("");
		players = new ArrayList<>();
	}

    public GameInfo(String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.id = obj.get("id").getAsInt();
        this.title = obj.get("title").getAsString();
        this.players = new ArrayList<>();
    }
	
	public int getId()
	{
		return id;
	}
	
	public void setId(final int id)
	{
		this.id = id;
	}

	public void setPlayers(final List<PlayerInfo> players) {
		this.players = players;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(final String title)
	{
		this.title = title;
	}
	
	public void addPlayer(final PlayerInfo newPlayer)
	{
		players.add(newPlayer);
	}
	
	public List<PlayerInfo> getPlayers()
	{
		return Collections.unmodifiableList(players);
	}

	@Override
    public String toString() {
        return this.title;
    }
}
