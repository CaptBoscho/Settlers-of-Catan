package client.communication;

import shared.definitions.*;

/**
 * Message (or entry) displayed in the LogComponent
 */
final class LogEntry {
	
	/**
	 * Color used when displaying the message
	 */
	private CatanColor color;
	
	/**
	 * Message text
	 */
	private String message;
	
	LogEntry(CatanColor color, String message) {
		this.color = color;
		this.message = message;
	}
	
	public CatanColor getColor() {
		return color;
	}
	
	public void setColor(final CatanColor color) {
		this.color = color;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
}
