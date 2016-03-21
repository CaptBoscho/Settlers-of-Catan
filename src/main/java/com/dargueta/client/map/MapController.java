package client.map;

import java.util.*;

import client.facade.Facade;
import client.services.UserCookie;
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
    private boolean firstRound;
    private boolean secondRound;
    private boolean robbingRound;
	
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
        firstRound = true;
        secondRound = true;
        robbingRound = true;
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
        if(facade.getCurrentTurn() != userCookie.getPlayerIndex()) {
            robbingRound = true;
            firstRound = true;
            secondRound = true;
        }
        int totalPlayers = facade.getPlayers().size();
        if(state == TurnTracker.Phase.ROBBING && facade.getCurrentTurn() == userCookie.getPlayerIndex() && robbingRound) {
            mapState.startMove(PieceType.ROBBER, true, true);
            robbingRound = false;
        } else if(state == TurnTracker.Phase.SETUPONE && facade.getCurrentTurn() == userCookie.getPlayerIndex() &&
                firstRound && totalPlayers == 4) {
            if(facade.getMap().getSettlements().get(userCookie.getPlayerIndex()) == null) {
                mapState.startMove(PieceType.SETTLEMENT, true, true);
            } else if(facade.getMap().getRoads().get(userCookie.getPlayerIndex()) == null){
                mapState.startMove(PieceType.ROAD, true, true);
            }
            firstRound = false;
        } else if(state == TurnTracker.Phase.SETUPTWO && facade.getCurrentTurn() == userCookie.getPlayerIndex() &&
                secondRound) {
            if(facade.getMap().getSettlements().get(userCookie.getPlayerIndex()).size() == 1) {
                mapState.startMove(PieceType.SETTLEMENT, true, true);
            } else if(facade.getMap().getRoads().get(userCookie.getPlayerIndex()).size() == 1){
                mapState.startMove(PieceType.ROAD, true, true);
            }
            secondRound = false;
        }
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
