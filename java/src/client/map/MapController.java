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
	
	public MapController(IMapView view, IRobView robView) {
		super(view);
		setRobView(robView);
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
        facade.addObserver(this);
        initialize();
	}

    public void initialize() {
        TurnTracker.Phase state = facade.getPhase();
        switch(state) {
            case SETUPONE:
                mapState = new SetupOneState(this);
                break;
            case SETUPTWO:
                mapState = new SetupTwoState(this);
                break;
            case ROLLING:
                mapState = new RollingState(this);
                break;
            case ROBBING:
                mapState = new RobbingState(this);
                break;
            case PLAYING:
                mapState = new PlayingState(this);
                break;
            case DISCARDING:
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
		if(mapState.canPlaceRoad(edgeLoc)) {
            mapState.placeRoad(edgeLoc);
            try {
                getView().placeRoad(edgeLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
        }
	}

	public void placeSettlement(VertexLocation vertLoc) {
        if(mapState.canPlaceSettlement(vertLoc)) {
            mapState.placeSettlement(vertLoc);
            try {
                getView().placeSettlement(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
        }
	}

	public void placeCity(VertexLocation vertLoc) {
        if(mapState.canPlaceCity(vertLoc)) {
            mapState.placeCity(vertLoc);
            try {
                getView().placeCity(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
        }
	}

	public void placeRobber(HexLocation hexLoc) {
        if(mapState.canPlaceRobber(hexLoc)) {
            mapState.placeRobber(hexLoc);
            getView().placeRobber(hexLoc);
            getRobView().showModal();
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
