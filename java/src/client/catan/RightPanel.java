package client.catan;

import javax.swing.*;

import shared.definitions.PieceType;
import client.points.*;
import client.resources.*;
import client.base.*;
import client.map.*;
import client.devcards.*;

@SuppressWarnings("serial")
class RightPanel extends JPanel {

	private DevCardController devCardController;

	RightPanel(final IMapController mapController) {
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		// Initialize development card views and controller
		//
		final PlayDevCardView playCardView = new PlayDevCardView();
		final BuyDevCardView buyCardView = new BuyDevCardView();
		IAction soldierAction = () -> mapController.playSoldierCard();
		IAction roadAction = () -> mapController.playRoadBuildingCard();
		devCardController = new DevCardController(playCardView, buyCardView,
												  soldierAction, roadAction);
		playCardView.setController(devCardController);
		buyCardView.setController(devCardController);
		
		// Initialize victory point view and controller
		//
		final PointsView pointsView = new PointsView();
		final GameFinishedView finishedView = new GameFinishedView();
		final PointsController pointsController = new PointsController(pointsView, finishedView);
		pointsView.setController(pointsController);
		
		// Initialize resource bar view and controller
		//
		final ResourceBarView resourceView = new ResourceBarView();
		final ResourceBarController resourceController = new ResourceBarController(resourceView);
		resourceController.setElementAction(ResourceBarElement.ROAD,
											createStartMoveAction(mapController, PieceType.ROAD));
		resourceController.setElementAction(ResourceBarElement.SETTLEMENT,
											createStartMoveAction(mapController, PieceType.SETTLEMENT));
		resourceController.setElementAction(ResourceBarElement.CITY,
											createStartMoveAction(mapController, PieceType.CITY));
		resourceController.setElementAction(ResourceBarElement.BUY_CARD,
				() -> devCardController.startBuyCard());
		resourceController.setElementAction(ResourceBarElement.PLAY_CARD,
				() -> devCardController.startPlayCard());
		resourceView.setController(resourceController);
		
		this.add(pointsView);
		this.add(resourceView);
	}
	
	private IAction createStartMoveAction(final IMapController mapController, final PieceType pieceType) {
		
		return () -> {
            boolean isFree = false;
            boolean allowDisconnected = false;
            mapController.startMove(pieceType, isFree, allowDisconnected);
        };
	}
}
