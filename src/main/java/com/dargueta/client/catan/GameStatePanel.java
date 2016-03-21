package com.dargueta.client.catan;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.dargueta.client.base.IAction;


@SuppressWarnings("serial")
public class GameStatePanel extends JPanel {

	private MyButton button;
	
	public GameStatePanel() {
		this.setLayout(new FlowLayout());
		this.setBackground(Color.white);
		this.setOpaque(true);
		
		button = new MyButton();
		
		final Font font = button.getFont();
		final Font newFont = font.deriveFont(font.getStyle(), 20);
		button.setFont(newFont);
		
		button.setPreferredSize(new Dimension(400, 50));
		
		this.add(button);
		
		updateGameState("Waiting for other Players", false, Color.white);
	}
	
	public void updateGameState(String stateMessage, boolean enable, Color color) {
		button.setBackground(color);
		button.setText(stateMessage);
		button.setEnabled(enable);
		button.setColor(color);
		button.repaint();
	}
	
	public void setButtonAction(final IAction action) {
		final ActionListener[] listeners = button.getActionListeners();
		for(final ActionListener listener : listeners) {
			button.removeActionListener(listener);
		}
		
		ActionListener actionListener = e -> action.execute();
		button.addActionListener(actionListener);
	}

	class MyButton extends JButton {

		private Color color;

		public MyButton() {
			super();
			super.setContentAreaFilled(false);
		}

		public void setColor(Color color) {
			this.color = color;
			this.setBackground(color);
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(this.color);
			g.fillRect(0, 0, getWidth(), getHeight());
			super.paintComponent(g);
		}

		@Override
		public void setContentAreaFilled(boolean b) {
		}
	}
}
