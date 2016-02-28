package client.join;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import client.base.*;
import client.data.*;

/**
 * Implementation for the join game view, which lets the user select a game to
 * join
 */
@SuppressWarnings("serial")
public class JoinGameView extends OverlayView implements IJoinGameView {

	private final int LABEL_TEXT_SIZE = 40;
	private final int PANEL_TEXT_SIZE = 14;
	private final int BUTTON_TEXT_SIZE = 28;
	private final int BORDER_WIDTH = 10;

	private JButton createButton;

	private GameInfo[] games;
	private PlayerInfo localPlayer;

	public JoinGameView() {
		this.initialize();
	}

	private void initialize() {
		this.initializeView();
	}

	private void initializeView() {
		this.removeAll();
//		this.updateUI();
		System.out.println("starting view initialize");
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

		final JLabel label = new JLabel("Welcome to the game hub");
		Font labelFont = label.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE);
		label.setFont(labelFont);
		final JLabel subLabel = new JLabel("Join an existing game, or create your own");
		labelFont = subLabel.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE * 2 / 3);
		subLabel.setFont(labelFont);

		final JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		labelPanel.add(label);
		labelPanel.add(subLabel);
		this.add(labelPanel, BorderLayout.NORTH);


		// This is the header layout
		final JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(0, 4));
		final JLabel hash = new JLabel("#");
		labelFont = new Font(labelFont.getFontName(), Font.BOLD, PANEL_TEXT_SIZE);
		hash.setFont(labelFont);
		final JLabel name = new JLabel("Name");
		name.setFont(labelFont);
		final JLabel currentPlayer = new JLabel("Current Players");
		currentPlayer.setFont(labelFont);
		final JLabel join = new JLabel("Join");
		join.setFont(labelFont);

		gamePanel.add(hash);
		gamePanel.add(name);
		gamePanel.add(currentPlayer);
		gamePanel.add(join);

		// This is the looped layout
		if (games != null && games.length > 0) {
			labelFont = labelFont.deriveFont(labelFont.getStyle(), PANEL_TEXT_SIZE);
			for (GameInfo game : games) {
				final JLabel tmp1 = new JLabel(String.valueOf(game.getId()));
				tmp1.setFont(labelFont);
				gamePanel.add(tmp1);
				final JLabel tmp2 = new JLabel(game.getTitle());
				System.out.println("found game " + game.getTitle());
				tmp2.setFont(labelFont);
				gamePanel.add(tmp2);
				String players = String.valueOf(game.getPlayers().size()) + "/4 : ";
				for (int j = 0; j < game.getPlayers().size(); j++) {
					if (j < game.getPlayers().size() - 1) {
						players = players + game.getPlayers().get(j).getName() + ", ";
					} else {
						players = players + game.getPlayers().get(j).getName();
					}
				}
				final JLabel tmp3 = new JLabel(players);
				tmp3.setFont(labelFont);
				gamePanel.add(tmp3);
				JButton joinButton;
				
				if (game.getPlayers().contains(localPlayer)) {
					joinButton = new JButton("Re-Join");
				} else if (game.getPlayers().size() >= 4) {
					joinButton = new JButton("Full");
					joinButton.setEnabled(false);
				} else {
					joinButton = new JButton("Join");
				}
				joinButton.setActionCommand("" + game.getId());
				joinButton.addActionListener(actionListener);
				gamePanel.add(joinButton);
			}
		}

		//Add all the above
		this.add(gamePanel, BorderLayout.CENTER);

		createButton = new JButton("Create game");
		Font buttonFont = createButton.getFont();
		buttonFont = buttonFont.deriveFont(buttonFont.getStyle(), BUTTON_TEXT_SIZE);
		createButton.addActionListener(actionListener);
		createButton.setFont(buttonFont);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(createButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.updateUI();
		System.out.println("finishing initialize");
	}

	@Override
	public IJoinGameController getController() {
		return (IJoinGameController) super.getController();
	}

	@Override
	public void setGames(GameInfo[] games, PlayerInfo localPlayer) {
		if(!Arrays.equals(this.games, games)) {
			this.games = games;
			this.localPlayer = localPlayer;
			this.removeAll();
			this.initialize();
		}
	}
	
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == createButton) {
				getController().startCreateNewGame();
			} else {
				try {
					int gameId = Integer.parseInt(e.getActionCommand());
					GameInfo game = null;
					for (final GameInfo g : games) {
						if (g.getId() == gameId) {
							game = g;
							break;
						}
					}
					getController().startJoinGame(game);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
	};
}
