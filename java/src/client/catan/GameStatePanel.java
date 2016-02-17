package client.catan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.base.IAction;


@SuppressWarnings("serial")
public class GameStatePanel extends JPanel {

	private JButton button;
	
	public GameStatePanel() {
		this.setLayout(new FlowLayout());
		this.setBackground(Color.white);
		this.setOpaque(true);
		
		button = new JButton();
		
		final Font font = button.getFont();
		final Font newFont = font.deriveFont(font.getStyle(), 20);
		button.setFont(newFont);
		
		button.setPreferredSize(new Dimension(400, 50));
		
		this.add(button);
		
		updateGameState("Waiting for other Players", false);
	}
	
	public void updateGameState(String stateMessage, boolean enable) {
		button.setText(stateMessage);
		button.setEnabled(enable);
	}
	
	public void setButtonAction(final IAction action) {
		final ActionListener[] listeners = button.getActionListeners();
		for(final ActionListener listener : listeners) {
			button.removeActionListener(listener);
		}
		
		ActionListener actionListener = e -> action.execute();
		button.addActionListener(actionListener);
	}
}
