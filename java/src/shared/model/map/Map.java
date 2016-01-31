package shared.model.map;

import shared.exceptions.*;
import shared.locations.*;
import shared.definitions.*;
import shared.model.map.hex.ChitHex;
import shared.model.map.hex.Hex;
import shared.model.structures.*;

import java.util.*;

/**
 * Representation of the map in the game. The game map keeps track of all locations, buildings, and chits as well as the
 * special robber character. The map uses a HashMap in the underlaying implementation, which allows O(1)
 * insertion/retrieval.
 *
 * @author Joel Bradley
 */
public class Map implements IMap {

    private java.util.Map<HexLocation, Hex> hexes;
    private java.util.Map<EdgeLocation, Edge> edges;
    private java.util.Map<VertexLocation, Vertex> vertices;
    private java.util.Map<Integer, ArrayList<HexLocation>> chits;
    private Robber robber;
    private java.util.Map<Integer, ArrayList<Vertex>> buildings;
    private java.util.Map<Integer, ArrayList<Edge>> roads;
    private java.util.Map<Integer, ArrayList<Port>> ports;
    private Random randomGenerator;

    /**
     * Default Constructor that initializes the map
     */
    public Map() {
        //initialize fields
        hexes = new HashMap<>();
        chits = new HashMap<>();
        edges = new HashMap<>();
        vertices = new HashMap<>();
        randomGenerator = new Random();
        buildings = new HashMap<>();
        roads = new HashMap<>();
        ports = new HashMap<>();
        makeMap();
    }

    /*===========================================
                   Interface Methods
     ============================================*/

    @Override
    public void giveResources(int diceRoll) throws InvalidDiceRollException {
        if(diceRoll < 2 || diceRoll > 12) {
            StringBuilder error = new StringBuilder();
            error.append("Dice roll was ");
            error.append(diceRoll);
            throw new InvalidDiceRollException(error.toString());
        }
        if(diceRoll == 7) {
            throw new InvalidDiceRollException("Need to move robber instead of giving resources");
        }
        ArrayList<HexLocation> chitList = chits.get(diceRoll);
        for(int i=0; i<chitList.size(); i++) {
            HexLocation hexLoc = chitList.get(i);
            if(robber.getLocation() != hexLoc) {
                ResourceType resourceType = getResourceType(hexLoc);
                VertexLocation northWestLoc = new VertexLocation(hexLoc, VertexDirection.NorthWest);
                VertexLocation northEastLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
                VertexLocation eastLoc = new VertexLocation(hexLoc, VertexDirection.East);
                VertexLocation southEastLoc = new VertexLocation(hexLoc, VertexDirection.SouthEast);
                VertexLocation southWestLoc = new VertexLocation(hexLoc, VertexDirection.SouthWest);
                VertexLocation westLoc = new VertexLocation(hexLoc, VertexDirection.West);
                giveResourcesToBuilding(northWestLoc, resourceType);
                giveResourcesToBuilding(northEastLoc, resourceType);
                giveResourcesToBuilding(eastLoc, resourceType);
                giveResourcesToBuilding(southEastLoc, resourceType);
                giveResourcesToBuilding(southWestLoc, resourceType);
                giveResourcesToBuilding(westLoc, resourceType);
            }
        }
    }

    @Override
    public void initiateSettlement(int playerID, VertexLocation vertexLoc)
            throws StructureException, InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(vertex.hasBuilding()) {
            throw new StructureException("Vertex location already has a building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        Settlement settlement = new Settlement(); //TODO:pass in userid into settlement
        vertex.setBuilding(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        ArrayList<Vertex> buildings = this.buildings.get(playerID);
        if(buildings == null) {
            buildings = new ArrayList<>();
            buildings.add(vertex);
            this.buildings.put(playerID, buildings);
        } else {
            buildings.add(vertex);
            if(vertexLoc.getDir() == VertexDirection.NorthWest) {
                initiateResourcesNorthWest(vertexLoc);
            } else {
                initiateResourcesNorthEast(vertexLoc);
            }
        }
    }

    @Override
    public void initiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc)
            throws StructureException, InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(vertex == null || edge == null) {
            throw new InvalidLocationException("Vertex/Edge location is not on the map");
        }
        if(!vertex.hasBuilding()) {
            throw new StructureException("Road must be connected to a Building");
        }
        if(vertex.getBuilding().getPlayerID() != playerID) {
            throw new StructureException("Building belongs to different player");
        }
        if(vertexHasConnectingRoad(playerID, vertexLoc)) {
            throw new StructureException("Road connected to the wrong building");
        }
        if(!edgeConnectedToVertex(edgeLoc, vertexLoc)) {
            throw new StructureException("Road not connected to the building");
        }
        if(edge.hasRoad()) {
            throw new StructureException("Edge location already has a road");
        }
        Road road = new Road(playerID);
        edge.setRoad(road);
        ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null) {
            roads = new ArrayList<>();
            roads.add(edge);
            this.roads.put(playerID, roads);
        } else {
            roads.add(edge);
        }
    }

    @Override
    public boolean canBuildRoad(int playerID, EdgeLocation edgeLoc) throws InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return false;
        }
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        if(edge.hasRoad()) {
            return false;
        }
        if(!edgeHasConnectingRoad(playerID, edgeLoc)) {
            return false;
        }
        return true;
    }

    @Override
    public void buildRoad(int playerID, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return;
        }
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        if(edge.hasRoad()) {
            throw new StructureException("Edge already has a Road");
        }
        if(!edgeHasConnectingRoad(playerID, edgeLoc)) {
            throw new StructureException("Road must be connected to existing Road");
        }
        Road road = new Road(playerID);
        edge.setRoad(road);
        ArrayList<Edge> roads = this.roads.get(playerID);
        roads.add(edge);
    }

    @Override
    public boolean canBuildSettlement(int playerID, VertexLocation vertexLoc) throws InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return false;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(vertex.hasBuilding()) {
            return false;
        }
        if(hasNeighborBuildings(vertexLoc)) {
            return false;
        }
        if(!vertexHasConnectingRoad(playerID, vertexLoc)) {
            return false;
        }
        return true;
    }

    @Override
    public void buildSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(vertex.hasBuilding()) {
            throw new StructureException("Vertex already has a Building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        if(!vertexHasConnectingRoad(playerID, vertexLoc)) {
            throw new StructureException("Vertex location has no connecting road");
        }
        Settlement settlement = new Settlement(); //TODO:pass in userid into settlement
        vertex.setBuilding(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        ArrayList<Vertex> buildings = this.buildings.get(playerID);
        buildings.add(vertex);
    }

    @Override
    public boolean canBuildCity(int playerID, VertexLocation vertexLoc) throws InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return false;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(vertex.hasBuilding() && vertex.getBuilding().getPlayerID() == playerID) {
            Building building = vertex.getBuilding();
            Settlement instance;
            try {
                instance = Settlement.class.newInstance();
                if(building.getClass() == instance.getClass()) {
                    return true;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void buildCity(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        if(playerID < 1 || playerID > 4) {
            return;
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(!vertex.hasBuilding()) {
            throw new StructureException("A Settlement needs to be built first");
        }
        if(vertex.getBuilding().getPlayerID() != playerID) {
            throw new StructureException("The Building doesn't belong to the player");
        }
        Building building = vertex.getBuilding();
        Settlement instance;
        try {
            instance = Settlement.class.newInstance();
            if(building.getClass() != instance.getClass()) {
                throw new StructureException("A City is already built at the vertex location");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ArrayList<Vertex> buildings = this.buildings.get(playerID);
        buildings.remove(vertex);
        City city = new City(); //TODO:pass in userID into city
        vertex.setBuilding(city);
        buildings.add(vertex);
    }

    @Override
    public int getLongestRoadSize(int playerID) {
        return 0;
    }

    @Override
    public Set<PortType> getPortTypes(int playerID) {
        if(playerID < 1 || playerID > 4) {
            return null;
        }
        Set<PortType> portTypes = new HashSet<>();
        ArrayList<Port> ports = this.ports.get(playerID);
        if(ports != null) {
            for(int i=0; i<ports.size(); i++) {
                portTypes.add(ports.get(i).getPortType());
            }
        }
        return portTypes;
    }

    @Override
    public void moveRobber(HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException {
        Hex hex = hexes.get(hexLoc);
        if(hex == null || hex.getType() == HexType.WATER) {
            throw new InvalidLocationException("Hex location is not on the map");
        }
        if(robber.getLocation() == hexLoc) {
            throw new AlreadyRobbedException("Robber cannot remain at the same hex location");
        }
        robber.setLocation(hexLoc);
    }

    /*===========================================
                   Private Methods
     ============================================*/

    private void makeMap() {
        makeOceanHexes();
        makeIslandHexes();
        makePorts();
    }

    private void makeOceanHexes() {
        // transverse border of map
        int column = -3;
        int diagonal = -3;
        ArrayList<EdgeDirection> edgeDir = new ArrayList<>();
        ArrayList<VertexDirection> vertexDir = new ArrayList<>();
        while(column <= 0) {
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            column++;
        }
        diagonal++;
        while(column <= 3) {
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            column++;
            diagonal++;
        }
        column--;
        edgeDir.add(EdgeDirection.NorthWest);
        vertexDir.add(VertexDirection.NorthWest);
        while(diagonal <= 3) {
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            diagonal++;
        }
        column--;
        diagonal--;
        edgeDir.add(EdgeDirection.North);
        vertexDir.add(VertexDirection.NorthEast);
        while(column >= 0) {
            if(column == 0) {
                edgeDir.remove(EdgeDirection.NorthWest);
            }
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            column--;
        }
        diagonal--;
        edgeDir.add(EdgeDirection.NorthEast);
        while(column >= -3) {
            if(column == -3) {
                edgeDir.remove(EdgeDirection.North);
                vertexDir.remove(VertexDirection.NorthWest);
            }
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            column--;
            diagonal--;
        }
        column++;
        while(diagonal >= -2) {
            makeOceanHex(column, diagonal, edgeDir, vertexDir);
            diagonal--;
        }
    }

    private void makeOceanHex(int column, int diagonal, ArrayList<EdgeDirection> edgeDir,
                              ArrayList<VertexDirection> vertexDir) {
        HexLocation hexLoc = new HexLocation(column, diagonal);
        for(int i=0; i<edgeDir.size(); i++) {
            EdgeLocation edgeLoc = new EdgeLocation(hexLoc, edgeDir.get(i));
            Edge edge = new Edge(edgeLoc);
            edges.put(edgeLoc, edge);
        }
        for(int i=0; i<vertexDir.size(); i++) {
            VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir.get(i));
            Vertex vertex = new Vertex(vertexLoc);
            vertices.put(vertexLoc, vertex);
        }
        Hex oceanHex = new Hex(hexLoc, HexType.WATER);
        hexes.put(hexLoc, oceanHex);
    }

    private void makeIslandHexes() {
        // prepare to load hexes
        ArrayList<Integer> chits = makeChits();
        ArrayList<HexType> hexTypes = makeHexTypes();
        // transverse island
        int column = -2;
        int diagonal = -2;
        while(diagonal <= 0) {
            makeIslandHex(column, diagonal, chits, hexTypes);
            diagonal++;
        }
        column++;
        diagonal = -2;
        while(diagonal <= 1) {
            makeIslandHex(column, diagonal, chits, hexTypes);
            diagonal++;
        }
        column++;
        diagonal = -2;
        while(diagonal <= 2) {
            makeIslandHex(column, diagonal, chits, hexTypes);
            diagonal++;
        }
        column++;
        diagonal = -1;
        while(diagonal <= 2) {
            makeIslandHex(column, diagonal, chits, hexTypes);
            diagonal++;
        }
        column++;
        diagonal = 0;
        while(diagonal <= 2) {
            makeIslandHex(column, diagonal, chits, hexTypes);
            diagonal++;
        }
    }

    private void makeIslandHex(int column, int diagonal, ArrayList<Integer> chits, ArrayList<HexType> hexTypes) {
        HexLocation hexLoc = new HexLocation(column, diagonal);
        makeEdgesForIslandHex(hexLoc);
        makeVerticesForIslandHex(hexLoc);
        int hexTypeIndex = randomGenerator.nextInt(hexTypes.size());
        HexType hexType = hexTypes.remove(hexTypeIndex);
        if(hexType == HexType.DESERT) {
            Hex desertHex = new Hex(hexLoc, hexType);
            hexes.put(hexLoc, desertHex);
            robber = new Robber(hexLoc);
        } else {
            int chitIndex = randomGenerator.nextInt(chits.size());
            int chit = chits.remove(chitIndex);
            ChitHex chitHex = new ChitHex(hexLoc, hexType, chit);
            hexes.put(hexLoc, chitHex);
            ArrayList<HexLocation> chitList = this.chits.get(chit);
            if(chitList == null) {
                chitList = new ArrayList<>();
                chitList.add(hexLoc);
                this.chits.put(chit, chitList);
            } else {
                chitList.add(hexLoc);
            }
        }
    }

    private void makeEdgesForIslandHex(HexLocation hexLoc) {
        EdgeLocation northWestLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthWest);
        EdgeLocation northLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
        EdgeLocation northEastLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
        Edge northWest = new Edge(northWestLoc);
        Edge north = new Edge(northLoc);
        Edge northEast = new Edge(northEastLoc);
        edges.put(northWestLoc, northWest);
        edges.put(northLoc, north);
        edges.put(northEastLoc, northEast);
    }

    private void makeVerticesForIslandHex(HexLocation hexLoc) {
        VertexLocation northWestLoc = new VertexLocation(hexLoc, VertexDirection.NorthWest);
        VertexLocation northEastLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
        Vertex northWest = new Vertex(northWestLoc);
        Vertex northEast = new Vertex(northEastLoc);
        vertices.put(northWestLoc, northWest);
        vertices.put(northEastLoc, northEast);
    }

    private ArrayList<Integer> makeChits() {
        ArrayList<Integer> chits = new ArrayList<>();
        for(int i=2; i<13; i++) {
            if(i==2 || i==12) {
                chits.add(i);
            } else if(i!=7) {
                chits.add(i);
                chits.add(i);
            }
        }
        return chits;
    }

    private ArrayList<HexType> makeHexTypes() {
        ArrayList<HexType> hexTypes = new ArrayList<>();
        for(int i=1; i<5; i++) {
            if(i==1) {
                hexTypes.add(HexType.DESERT);
                hexTypes.add(HexType.BRICK);
                hexTypes.add(HexType.ORE);
                hexTypes.add(HexType.SHEEP);
                hexTypes.add(HexType.WHEAT);
                hexTypes.add(HexType.WOOD);
            } else if(i==2 || i==3) {
                hexTypes.add(HexType.BRICK);
                hexTypes.add(HexType.ORE);
                hexTypes.add(HexType.SHEEP);
                hexTypes.add(HexType.WHEAT);
                hexTypes.add(HexType.WOOD);
            } else if(i==4) {
                hexTypes.add(HexType.SHEEP);
                hexTypes.add(HexType.WHEAT);
                hexTypes.add(HexType.WOOD);
            }
        }
        return hexTypes;
    }

    private void makePorts() {
        ArrayList<PortType> portTypes = makePortTypes();

        //first port
        int portTypeIndex = randomGenerator.nextInt(portTypes.size());
        PortType portType = portTypes.remove(portTypeIndex);
        portSetup(1, -1, 1, -1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //second port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(2, 0, 3, 1, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //third port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(3, 2, 2, 2, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //fourth port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(2, 3, 1, 3, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //fifth port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(0, 3, 0, 3, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //sixth port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(-1, 2, -2, 1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //seventh port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(-2, 0, -3, -1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //eighth port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(-3, -2, -2, -2, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //ninth port
        portTypeIndex = randomGenerator.nextInt(portTypes.size());
        portType = portTypes.remove(portTypeIndex);
        portSetup(-1, -2, -1, -2, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);
    }

    private void portSetup(int columnOne, int diagonalOne, int columnTwo, int diagonalTwo,
                           VertexDirection vertexDirOne, VertexDirection vertexDirTwo, PortType portType) {
        HexLocation hexLocOne = new HexLocation(columnOne, diagonalOne);
        HexLocation hexLocTwo = new HexLocation(columnTwo, diagonalTwo);
        VertexLocation vertexLocOne = new VertexLocation(hexLocOne, vertexDirOne);
        VertexLocation vertexLocTwo = new VertexLocation(hexLocTwo, vertexDirTwo);
        makePort(portType, vertexLocOne);
        makePort(portType, vertexLocTwo);
    }

    private void makePort(PortType portType, VertexLocation vertexLoc) {
        Port port = new Port(portType, vertexLoc);
        Vertex vertex = vertices.get(vertexLoc);
        vertex.setPort(port);
    }

    private ArrayList<PortType> makePortTypes() {
        ArrayList<PortType> portTypes = new ArrayList<>();
        for(int i=1; i<5; i++) {
            if(i==1) {
                portTypes.add(PortType.BRICK);
                portTypes.add(PortType.ORE);
                portTypes.add(PortType.SHEEP);
                portTypes.add(PortType.WHEAT);
                portTypes.add(PortType.WOOD);
            }
            portTypes.add(PortType.THREE);
        }
        return portTypes;
    }

    private void giveResourcesToBuilding(VertexLocation vertexLoc, ResourceType resourceType) {
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            vertex.giveResources(resourceType);
        }
    }

    private boolean hasNeighborBuildings(VertexLocation vertexLoc) {
        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            return hasNeighborBuildingsNorthWest(vertexLoc);
        } else {
            return hasNeighborBuildingsNorthEast(vertexLoc);
        }
    }

    private boolean hasNeighborBuildingsNorthWest(VertexLocation vertexLoc) {
        HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        VertexLocation upperLeftVertexLoc = new VertexLocation(upperLeftHexLoc, VertexDirection.NorthEast);
        Vertex upperLeft = vertices.get(upperLeftVertexLoc);
        if (upperLeft != null && upperLeft.hasBuilding()) {
            return true;
        }
        HexLocation lowerLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        Vertex lowerLeft = vertices.get(lowerLeftVertexLoc);
        if(lowerLeft != null && lowerLeft.hasBuilding()) {
            return true;
        }
        HexLocation hexLoc = vertexLoc.getHexLoc();
        VertexLocation rightLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
        Vertex right = vertices.get(rightLoc);
        if(right.hasBuilding()) {
            return true;
        }
        return false;
    }

    private boolean hasNeighborBuildingsNorthEast(VertexLocation vertexLoc) {
        HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        VertexLocation upperRightVertexLoc = new VertexLocation(upperRightHexLoc, VertexDirection.NorthWest);
        Vertex upperRight = vertices.get(upperRightVertexLoc);
        if (upperRight != null && upperRight.hasBuilding()) {
            return true;
        }
        HexLocation lowerRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        Vertex lowerRight = vertices.get(lowerRightVertexLoc);
        if(lowerRight != null && lowerRight.hasBuilding()) {
            return true;
        }
        HexLocation hexLoc = vertexLoc.getHexLoc();
        VertexLocation leftLoc = new VertexLocation(hexLoc, VertexDirection.NorthWest);
        Vertex left = vertices.get(leftLoc);
        if(left.hasBuilding()) {
            return true;
        }
        return false;
    }

    private void addPort(int playerID, Vertex vertex) {
        if(vertex.hasPort()) {
            ArrayList<Port> ports = this.ports.get(playerID);
            if(ports == null) {
                ports = new ArrayList<>();
                ports.add(vertex.getPort());
                this.ports.put(playerID, ports);
            } else {
                ports.add(vertex.getPort());
            }
        }
    }

    private void initiateResourcesNorthWest(VertexLocation vertexLoc) {
        ArrayList<ResourceType> resourceList = new ArrayList<>();
        HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        HexLocation lowerLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        HexLocation rightHexLoc = vertexLoc.getHexLoc();
        ResourceType resourceType = getResourceType(upperLeftHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        resourceType = getResourceType(lowerLeftHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        resourceType = getResourceType(rightHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        for(int i=0; i<resourceList.size(); i++) {
            giveResourcesToBuilding(vertexLoc, resourceList.get(i));
        }
    }

    private void initiateResourcesNorthEast(VertexLocation vertexLoc) {
        ArrayList<ResourceType> resourceList = new ArrayList<>();
        HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        HexLocation lowerRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        HexLocation leftHexLoc = vertexLoc.getHexLoc();
        ResourceType resourceType = getResourceType(upperRightHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        resourceType = getResourceType(lowerRightHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        resourceType = getResourceType(leftHexLoc);
        if(resourceType != null) {
            resourceList.add(resourceType);
        }
        for(int i=0; i<resourceList.size(); i++) {
            giveResourcesToBuilding(vertexLoc, resourceList.get(i));
        }
    }

    private ResourceType getResourceType(HexLocation hexLoc) {
        Hex hex = hexes.get(hexLoc);
        HexType hexType = hex.getType();
        if(hexType == HexType.BRICK) {
            return ResourceType.BRICK;
        } else if(hexType == HexType.ORE) {
            return ResourceType.ORE;
        } else if(hexType == HexType.SHEEP) {
            return ResourceType.SHEEP;
        } else if(hexType == HexType.WHEAT) {
            return ResourceType.WHEAT;
        } else if(hexType == HexType.WOOD) {
            return ResourceType.WOOD;
        }
        return null;
    }

    private boolean vertexHasConnectingRoad(int playerID, VertexLocation vertexLoc) {
        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            return vertexHasConnectingRoadNorthWest(playerID, vertexLoc);
        } else {
            return vertexHasConnectingRoadNorthEast(playerID, vertexLoc);
        }
    }

    private boolean vertexHasConnectingRoadNorthWest(int playerID, VertexLocation vertexLoc) {
        HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        Edge upperLeft = edges.get(upperLeftEdgeLoc);
        if(upperLeft != null && upperLeft.hasRoad() && upperLeft.getRoad().getPlayerID() == playerID) {
            return true;
        }
        EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        Edge lowerLeft = edges.get(lowerLeftEdgeLoc);
        if(lowerLeft != null && lowerLeft.hasRoad() && lowerLeft.getRoad().getPlayerID() == playerID) {
            return true;
        }
        EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        Edge right = edges.get(rightEdgeLoc);
        if(right != null && right.hasRoad() && right.getRoad().getPlayerID() == playerID) {
            return true;
        }
        return false;
    }

    private boolean vertexHasConnectingRoadNorthEast(int playerID, VertexLocation vertexLoc) {
        HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        Edge upperRight = edges.get(upperRightEdgeLoc);
        if(upperRight != null && upperRight.hasRoad() && upperRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        Edge lowerRight = edges.get(lowerRightEdgeLoc);
        if(lowerRight != null && lowerRight.hasRoad() && upperRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        Edge left = edges.get(leftEdgeLoc);
        if(left != null && left.hasRoad() && upperRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        return false;
    }

    private boolean edgeConnectedToVertex(EdgeLocation edgeLoc, VertexLocation vertexLoc) {
        edgeLoc = edgeLoc.getNormalizedLocation();
        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            return edgeConnectedToVertexNorthWest(edgeLoc, vertexLoc);
        } else {
            return edgeConnectedToVertexNorthEast(edgeLoc, vertexLoc);
        }
    }

    private boolean edgeConnectedToVertexNorthWest(EdgeLocation edgeLoc, VertexLocation vertexLoc) {
        HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        if(upperLeftEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        if(lowerLeftEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        if(rightEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        return false;
    }

    private boolean edgeConnectedToVertexNorthEast(EdgeLocation edgeLoc, VertexLocation vertexLoc) {
        HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        if(upperRightEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        if(lowerRightEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        if(leftEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        return false;
    }

    private boolean edgeHasConnectingRoad(int playerID, EdgeLocation edgeLoc) {
        edgeLoc = edgeLoc.getNormalizedLocation();
        if(edgeLoc.getDir() == EdgeDirection.NorthWest) {
            return edgeHasConnectingRoadNorthWest(playerID, edgeLoc);
        } else if(edgeLoc.getDir() == EdgeDirection.North) {
            return edgeHasConnectingRoadNorth(playerID, edgeLoc);
        } else {
            return edgeHasConnectingRoadNorthEast(playerID, edgeLoc);
        }
    }

    private boolean edgeHasConnectingRoadNorthWest(int playerID, EdgeLocation edgeLoc) {
        HexLocation lowerLeftHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        VertexLocation upperRightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        return edgeHasConnectingRoad(playerID, lowerLeftVertexLoc, upperRightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorth(int playerID, EdgeLocation edgeLoc) {
        VertexLocation leftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        VertexLocation rightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerID, leftVertexLoc, rightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorthEast(int playerID, EdgeLocation edgeLoc) {
        HexLocation lowerRightHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        VertexLocation upperLeftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerID, lowerRightVertexLoc, upperLeftVertexLoc);
    }

    private boolean edgeHasConnectingRoad(int playerID, VertexLocation vertexLocOne, VertexLocation vertexLocTwo) {
        if(vertexHasConnectingRoad(playerID, vertexLocOne)) {
            Vertex vertex = vertices.get(vertexLocOne);
            if(vertex != null && vertex.hasBuilding() && vertex.getBuilding().getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        if(vertexHasConnectingRoad(playerID, vertexLocTwo)) {
            Vertex vertex = vertices.get(vertexLocTwo);
            if(vertex != null && vertex.hasBuilding() && vertex.getBuilding().getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        return false;
    }

}