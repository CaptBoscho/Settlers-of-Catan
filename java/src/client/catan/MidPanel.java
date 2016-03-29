package client.catan;

import java.awt.*;
import javax.swing.*;

import client.map.*;

@SuppressWarnings("serial")
class MidPanel extends JPanel {

	private MapController mapController;
	private GameStatePanel gameStatePanel;
	
	MidPanel() {
		
		this.setLayout(new BorderLayout());

		final TradePanel tradePanel = new TradePanel();

		final MapView mapView = new MapView();
		final RobView robView = new RobView();
		mapController = new MapController(mapView, robView);
		mapView.setController(mapController);
		robView.setController(mapController);
		
		gameStatePanel = new GameStatePanel();
		
		this.add(tradePanel, BorderLayout.NORTH);
		this.add(mapView, BorderLayout.CENTER);
		this.add(gameStatePanel, BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(800, 700));
	}
	
	GameStatePanel getGameStatePanel()
	{
		return gameStatePanel;
	}
	
	IMapController getMapController()
	{
		return mapController;
	}
}
