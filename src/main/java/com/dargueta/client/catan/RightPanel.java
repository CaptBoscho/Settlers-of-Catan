package com.dargueta.client.catan;

import javax.swing.*;

import com.dargueta.shared.definitions.PieceType;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {

	private DevCardController devCardController;

	public RightPanel(final IMapController mapController) {
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		// Initialize development card views and controller
		//
		final PlayDevCardView playCardView = new PlayDevCardView();
		final BuyDevCardView buyCardView = new BuyDevCardView();
		IAction soldierAction = new IAction() {
			@Override
			public void execute()
			{
				mapController.playSoldierCard();
			}
		};
		IAction roadAction = new IAction() {
			@Override
			public void execute()
			{
				mapController.playRoadBuildingCard();
			}
		};
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
											new IAction() {
												@Override
												public void execute()
												{
													devCardController.startBuyCard();
												}
											});
		resourceController.setElementAction(ResourceBarElement.PLAY_CARD,
											new IAction() {
												@Override
												public void execute()
												{
													devCardController.startPlayCard();
												}
											});
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
