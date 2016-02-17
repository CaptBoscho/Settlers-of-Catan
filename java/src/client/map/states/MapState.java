package client.map.states;

import client.data.RobPlayerInfo;
import client.facade.Facade;
import client.map.MapController;
import client.services.MissingUserCookieException;
import client.services.UserCookie;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.exceptions.PlayerExistsException;
import shared.locations.*;
import shared.model.map.Edge;
import shared.model.map.Map;
import shared.model.map.Vertex;
import shared.model.map.hex.Hex;
import shared.model.player.Player;

import java.util.ArrayList;

/**
 * @author Joel Bradley
 *
 * Base class for the state of the Map Controller
 */
abstract public class MapState {

    protected MapController mapController;
    protected Facade facade;
    protected UserCookie userCookie;
    /**
     * Constructor
     */
    public MapState(MapController mapController){
        this.mapController = mapController;
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
    }

    protected HexLocation getUIHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()-hexLoc.getX());
    }

    protected EdgeLocation getUIEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getUIHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    protected VertexLocation getUIVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getUIHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }

    protected HexLocation getModelHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()+hexLoc.getX());
    }

    protected EdgeLocation getModelEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getModelHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    protected VertexLocation getModelVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getModelHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }

    /**
     * Initializes the state
     */
    protected void initFromModel() {

        Map map = facade.getMap();

        //draw hexes
        java.util.Map<HexLocation, Hex> hexes = map.getHexes();
        for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
            mapController.getView().addHex(getUIHexLocation(entry.getKey()), entry.getValue().getType());
        }

        //draw chits
        java.util.Map<Integer, ArrayList<HexLocation>> chits = map.getChits();
        for(java.util.Map.Entry<Integer, ArrayList<HexLocation>> entry : chits.entrySet()) {
            ArrayList<HexLocation> hexLocList = entry.getValue();
            for(HexLocation hexLoc : hexLocList) {
                mapController.getView().addNumber(getUIHexLocation(hexLoc), entry.getKey());
            }
        }

        //draw ports
        java.util.Map<VertexLocation, Vertex> vertices = map.getVertices();

        //first port
        HexLocation hexLoc = new HexLocation(1,-1);
        PortType portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //second port
        hexLoc = new HexLocation(2,0);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //third port
        hexLoc = new HexLocation(3,2);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //fourth port
        hexLoc = new HexLocation(2,3);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //fifth port
        hexLoc = new HexLocation(0,3);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //sixth port
        hexLoc = new HexLocation(-2,1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //seventh port
        hexLoc = new HexLocation(-3,-1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthEast)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthEast), portType);

        //eighth port
        hexLoc = new HexLocation(-2,-2);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.NorthWest), portType);

        //ninth port
        hexLoc = new HexLocation(-1,-1);
        portType = vertices.get(new VertexLocation(hexLoc, VertexDirection.NorthWest)).getPort().getPortType();
        mapController.getView().addPort(new EdgeLocation(getUIHexLocation(hexLoc), EdgeDirection.North), portType);

        //draw roads
        java.util.Map<Integer, ArrayList<Edge>> roads = map.getRoads();
        for(java.util.Map.Entry<Integer, ArrayList<Edge>> entry : roads.entrySet()) {
            ArrayList<Edge> roadList = entry.getValue();
            for(Edge edge : roadList) {
                try {
                    mapController.getView().placeRoad(getUIEdgeLocation(edge.getEdgeLoc()),
                            facade.getPlayerColorByID(entry.getKey()));
                } catch (PlayerExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //draw settlements
        java.util.Map<Integer, ArrayList<Vertex>> settlements = map.getSettlements();
        for(java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : settlements.entrySet()) {
            ArrayList<Vertex> settlementList = entry.getValue();
            for(Vertex vertex : settlementList) {
                try {
                    mapController.getView().placeSettlement(getUIVertexLocation(vertex.getVertexLoc()),
                            facade.getPlayerColorByID(entry.getKey()));
                } catch (PlayerExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //draw cities
        java.util.Map<Integer, ArrayList<Vertex>> cities = map.getCities();
        for(java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : cities.entrySet()) {
            ArrayList<Vertex> cityList = entry.getValue();
            for(Vertex vertex : cityList) {
                try {
                    mapController.getView().placeCity(getUIVertexLocation(vertex.getVertexLoc()),
                            facade.getPlayerColorByID(entry.getKey()));
                } catch (PlayerExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //drawRobber
        mapController.getView().placeRobber(getUIHexLocation(map.getRobber().getLocation()));

    }

    abstract public boolean canPlaceRoad(EdgeLocation edgeLoc);

    abstract public boolean canPlaceSettlement(VertexLocation vertLoc);

    abstract public boolean canPlaceCity(VertexLocation vertLoc);

    abstract public boolean canPlaceRobber(HexLocation hexLoc);

    abstract public void placeRoad(EdgeLocation edgeLoc);

    abstract public void placeSettlement(VertexLocation vertLoc);

    abstract public void placeCity(VertexLocation vertLoc);

    abstract public void placeRobber(HexLocation hexLoc);

    abstract public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected);

    abstract public void cancelMove();

    abstract public void playSoldierCard();

    abstract public void playRoadBuildingCard();

    abstract public void robPlayer(RobPlayerInfo victim);
}
