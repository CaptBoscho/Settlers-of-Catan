package com.dargueta.client.communication;

import java.util.List;

/**
 * game history view interface
 */
public interface IGameHistoryView extends IView {
	
	/**
	 * Sets the history messages to be displayed in the view.
	 * 
	 * @param entries
	 *            The history messages to display
	 */
	void setEntries(List<LogEntry> entries);
}

