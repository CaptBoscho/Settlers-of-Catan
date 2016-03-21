package com.dargueta.client.turntracker;

/**
 * Interface for the turn tracker controller
 */
public interface ITurnTrackerController extends IController {
	
	/**
	 * This is called when the local player ends their turn
	 */
	void endTurn();
}

