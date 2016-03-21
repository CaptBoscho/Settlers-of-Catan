package com.dargueta.client.catan;

import java.awt.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class LeftPanel extends JPanel {

	private GameHistoryView historyView;
	private ChatView chatView;
	private TurnTrackerView turnView;

	public LeftPanel(TitlePanel titlePanel, GameStatePanel gameStatePanel) {
		
		this.setLayout(new BorderLayout());

		final JTabbedPane tabPane = new JTabbedPane();
		final Font font = tabPane.getFont();
		final Font newFont = font.deriveFont(font.getStyle(), 20);
		tabPane.setFont(newFont);
		
		historyView = new GameHistoryView();
		final GameHistoryController historyController = new GameHistoryController(historyView);
		historyView.setController(historyController);
		
		chatView = new ChatView();
		final ChatController chatController = new ChatController(chatView);
        chatView.setController(chatController);
		
		turnView = new TurnTrackerView(titlePanel, gameStatePanel);
		final TurnTrackerController turnController = new TurnTrackerController(turnView);
		turnView.setController(turnController);
		
//		gameStatePanel.setController(turnController);
		
		tabPane.add("game History", historyView);
		tabPane.add("Chat Messages", chatView);
		
		this.add(tabPane, BorderLayout.CENTER);
		this.add(turnView, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(350, 700));
	}

	public GameHistoryView getHistoryView() {
		return historyView;
	}

	public ChatView getChatView() {
		return chatView;
	}

	public TurnTrackerView getTurnView() {
		return turnView;
	}
}
