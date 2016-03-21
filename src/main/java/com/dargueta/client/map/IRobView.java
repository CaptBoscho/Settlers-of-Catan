package client.map;

/**
 * Interface for the rob view, which lets the user select a player to rob
 */
public interface IRobView extends IOverlayView {
	void setPlayers(RobPlayerInfo[] candidateVictims);
}
