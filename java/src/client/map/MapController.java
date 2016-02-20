package client.map;

import java.util.*;

import client.facade.Facade;
import client.map.states.*;
import client.services.UserCookie;
import shared.definitions.*;
import shared.exceptions.PlayerExistsException;
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
    private MapState mapState;
    private SetupOneState setupOneState;
    private SetupTwoState setupTwoState;
    private RollingState rollingState;
    private DiscardingState discardingState;
    private RobbingState robbingState;
    private PlayingState playingState;
	
	public MapController(IMapView view, IRobView robView) {
		super(view);
		setRobView(robView);
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
        setupOneState = SetupOneState.getInstance();
        setupTwoState = SetupTwoState.getInstance();
        rollingState = RollingState.getInstance();
        discardingState = DiscardingState.getInstance();
        robbingState = RobbingState.getInstance();
        playingState = PlayingState.getInstance();
        facade.addObserver(this);
	}

    public void initialize() {
        TurnTracker.Phase state = facade.getPhase();
        switch(state) {
            case SETUPONE:
                mapState = setupOneState;
                break;
            case SETUPTWO:
                mapState = setupTwoState;
                break;
            case ROLLING:
                mapState = rollingState;
                break;
            case ROBBING:
                mapState = robbingState;
                break;
            case PLAYING:
                mapState = playingState;
                break;
            case DISCARDING:
                mapState = discardingState;
                break;
            default:
                break;
        }
        mapState.setController(this);
        mapState.initFromModel();
    }
	
	public IMapView getView() {
		return (IMapView)super.getView();
	}
	
	public IRobView getRobView() {
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
		if(mapState.canPlaceRoad(edgeLoc)) {
            mapState.placeRoad(edgeLoc);
        }
	}

	public void placeSettlement(VertexLocation vertLoc) {
        if(mapState.canPlaceSettlement(vertLoc)) {
            mapState.placeSettlement(vertLoc);
        }
	}

	public void placeCity(VertexLocation vertLoc) {
        if(mapState.canPlaceCity(vertLoc)) {
            mapState.placeCity(vertLoc);
        }
	}

	public void placeRobber(HexLocation hexLoc) {
        if(mapState.canPlaceRobber(hexLoc)) {
            mapState.placeRobber(hexLoc);
        }
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		mapState.startMove(pieceType, isFree, allowDisconnected);
	}
	
	public void cancelMove() {
		mapState.cancelMove();
	}
	
	public void playSoldierCard() {
		mapState.playSoldierCard();
	}
	
	public void playRoadBuildingCard() {
		mapState.playRoadBuildingCard();
	}
	
	public void robPlayer(RobPlayerInfo victim) {	
		mapState.robPlayer(victim);
	}

	@Override
	public void update(Observable o, Object arg) {
        initialize();
	}
}
