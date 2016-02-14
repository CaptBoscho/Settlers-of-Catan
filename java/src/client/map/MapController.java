package client.map;

import java.util.*;

import shared.definitions.*;
import shared.exceptions.PlayerExistsException;
import shared.locations.*;
import client.base.*;
import client.data.*;
import shared.model.game.Game;
import shared.model.map.Edge;
import shared.model.map.Map;
import shared.model.map.Vertex;
import shared.model.map.hex.Hex;
import shared.model.player.Player;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;
	private Game game;
	
	public MapController(IMapView view, IRobView robView) {
		super(view);
		setRobView(robView);
		initFromModel();
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

    private HexLocation getUIHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()-hexLoc.getX());
    }
	
	protected void initFromModel() {

        Map map = game.getMap();

        //draw hexes
        java.util.Map<HexLocation, Hex> hexes = map.getHexes();
        for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
            getView().addHex(getUIHexLocation(entry.getKey()), entry.getValue().getType());
        }

        //draw chits
        java.util.Map<Integer, ArrayList<HexLocation>> chits = map.getChits();
        for(java.util.Map.Entry<Integer, ArrayList<HexLocation>> entry : chits.entrySet()) {
            ArrayList<HexLocation> hexLocList = entry.getValue();
            for(HexLocation hexLoc : hexLocList) {
                getView().addNumber(getUIHexLocation(hexLoc), entry.getKey());
            }
        }

        //draw ports
        java.util.Map<VertexLocation, Vertex> vertices = map.getVertices();

        //first port
        HexLocation hexLoc = new HexLocation(1,-1);
        PortType portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //second port
        hexLoc = new HexLocation(2,0);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //third port
        hexLoc = new HexLocation(3,2);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //fourth port
        hexLoc = new HexLocation(2,3);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //fifth port
        hexLoc = new HexLocation(0,3);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //sixth port
        hexLoc = new HexLocation(-2,1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //seventh port
        hexLoc = new HexLocation(-3,-1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //eighth port
        hexLoc = new HexLocation(-2,-2);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //ninth port
        hexLoc = new HexLocation(-1,-1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //draw roads TODO:figure out how to handle this player doesn't exist exception
        java.util.Map<Integer, ArrayList<Edge>> roads = map.getRoads();
        for(java.util.Map.Entry<Integer, ArrayList<Edge>> entry : roads.entrySet()) {
            Player player = new Player();
            try {
                player = game.getPlayerManager().getPlayerByID(entry.getKey());
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
            ArrayList<Edge> roadList = entry.getValue();
            for(Edge edge : roadList) {
                getView().placeRoad(new EdgeLocation(getUIHexLocation(edge.getEdgeLoc().getHexLoc()), edge.getEdgeLoc().getDir()), player.getColor());
            }
        }

        //draw settlements TODO:figure out how to handle this player doesn't exist exception
        java.util.Map<Integer, ArrayList<Vertex>> settlements = map.getSettlements();
        for(java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : settlements.entrySet()) {
            Player player = new Player();
            try {
                player = game.getPlayerManager().getPlayerByID(entry.getKey());
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
            ArrayList<Vertex> settlementList = entry.getValue();
            for(Vertex vertex : settlementList) {
                getView().placeSettlement(new VertexLocation(getUIHexLocation(vertex.getVertexLoc().getHexLoc()), vertex.getVertexLoc().getDir()), player.getColor());
            }
        }

        //draw cities TODO:figure out how to handle this player doesn't exist exception
        java.util.Map<Integer, ArrayList<Vertex>> cities = map.getCities();
        for(java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : cities.entrySet()) {
            Player player = new Player();
            try {
                player = game.getPlayerManager().getPlayerByID(entry.getKey());
            } catch (PlayerExistsException e) {
                System.out.println(e.getMessage());
            }
            ArrayList<Vertex> cityList = entry.getValue();
            for(Vertex vertex : cityList) {
                getView().placeCity(new VertexLocation(getUIHexLocation(vertex.getVertexLoc().getHexLoc()), vertex.getVertexLoc().getDir()), player.getColor());
            }
        }

        //drawRobber
        getView().placeRobber(getUIHexLocation(map.getRobber().getLocation()));

	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		return true;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		// TODO -- implement
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		// TODO -- implement
		return true;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		// TODO -- implement
		return true;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		
		getView().placeRoad(edgeLoc, CatanColor.ORANGE);
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		getView().placeSettlement(vertLoc, CatanColor.ORANGE);
	}

	public void placeCity(VertexLocation vertLoc) {
		
		getView().placeCity(vertLoc, CatanColor.ORANGE);
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

	}
}
