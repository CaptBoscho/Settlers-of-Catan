package client.map;

import java.util.*;

import client.facade.Facade;
import client.map.states.*;
import client.services.UserCookie;
import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import shared.model.game.TurnTracker;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;
	private Facade facade;
    private UserCookie userCookie;
    private client.map.MapState mapState;
	
	public MapController(IMapView view, IRobView robView) {
		super(view);
		setRobView(robView);
        facade = Facade.getInstance();
        userCookie.getInstance();
        facade.addObserver(this);
        initialize();
	}

    public void initialize() {
        TurnTracker.Phase state = facade.getPhase();
        switch(state) {
            case TurnTracker.Phase.SETUPONE:
                mapState = new SetupOneState(this);
                break;
            case TurnTracker.Phase.SETUPTWO:
                mapState = new SetupTwoState(this);
                break;
            case TurnTracker.Phase.ROLLING:
                mapState = new RollingState(this);
                break;
            case TurnTracker.Phase.ROBBING:
                mapState = new RobbingState(this);
                break;
            case TurnTracker.Phase.PLAYING:
                mapState = new PlayingState(this);
                break;
            case TurnTracker.Phase.DISCARDING:
                mapState = new DiscardingState(this);
                break;
            default:
                break;
        }
    }
	
	public IMapView getView() {
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}

	private void setRobView(IRobView robView) {
		this.robView = robView;
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return mapState.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return mapState.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
        return mapState.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return mapState.canPlaceRobber(hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		mapState.placeRoad(edgeLoc);
		getView().placeRoad(edgeLoc, facade.getPlayerColorByID(userCookie.getPlayerID()));
	}

	public void placeSettlement(VertexLocation vertLoc) {
		mapState.placeSettlement(vertLoc);
		getView().placeSettlement(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerID()));
	}

	public void placeCity(VertexLocation vertLoc) {
		mapState.placeCity(vertLoc);
		getView().placeCity(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerID()));
	}

	public void placeRobber(HexLocation hexLoc) {
		getView().placeRobber(hexLoc);
		getRobView().showModal();
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		getView().startDrop(pieceType, CatanColor.ORANGE, true);
	}
	
	public void cancelMove() {
		// TODO -- implement
	}
	
	public void playSoldierCard() {
		// TODO -- implement
	}
	
	public void playRoadBuildingCard() {
		// TODO -- implement
	}
	
	public void robPlayer(RobPlayerInfo victim) {	
		// TODO -- implement
	}

	@Override
	public void update(Observable o, Object arg) {
        initialize();
	}
}
