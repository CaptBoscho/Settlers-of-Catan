package shared.model.map;

import shared.exceptions.*;
import shared.locations.*;
import shared.definitions.*;
import shared.model.map.hex.*;
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
    private java.util.Map<Integer, ArrayList<Vertex>> settlements;
    private java.util.Map<Integer, ArrayList<Vertex>> cities;
    private java.util.Map<Integer, ArrayList<Edge>> roads;
    private java.util.Map<Integer, ArrayList<Port>> ports;
    private Random randomGenerator;
    private boolean randomHexes;
    private boolean randomChits;
    private boolean randomPorts;

    /**
     * Default Constructor that initializes the map
     */
    public Map(boolean randomHexes, boolean randomChits, boolean randomPorts) {
        //initialize fields
        hexes = new HashMap<>();
        chits = new HashMap<>();
        edges = new HashMap<>();
        vertices = new HashMap<>();
        randomGenerator = new Random();
        settlements = new HashMap<>();
        cities = new HashMap<>();
        roads = new HashMap<>();
        ports = new HashMap<>();
        this.randomHexes = randomHexes;
        this.randomChits = randomChits;
        this.randomPorts = randomPorts;
        makeMap();
    }

    /*===========================================
                   Interface Methods
     ============================================*/

    @Override
    public java.util.Map<Integer, List<ResourceType>> getResources(int diceRoll) throws InvalidDiceRollException {
        if(diceRoll < 2 || diceRoll > 12) {
            throw new InvalidDiceRollException("Dice roll was " + diceRoll);
        }
        if(diceRoll == 7) {
            throw new InvalidDiceRollException("Need to move robber instead of giving resources");
        }
        ArrayList<HexLocation> chitList = chits.get(diceRoll);
        java.util.Map<Integer, List<ResourceType>> resourceMap = new HashMap<>();
        for (HexLocation hexLoc : chitList) {
            if (robber.getLocation() != hexLoc) {
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.NorthWest);
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.NorthEast);
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.East);
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.SouthEast);
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.SouthWest);
                getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.West);
            }
        }
        return resourceMap;
    }

    @Override
    public List<ResourceType> initiateSettlement(int playerID, VertexLocation vertexLoc)
            throws StructureException, InvalidLocationException, InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(vertex.hasCity()) {
            throw new StructureException("Map is already initialized");
        }
        if(vertex.hasSettlement()) {
            throw new StructureException("Vertex location already has a settlement");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        Settlement settlement = new Settlement(playerID);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        List<ResourceType> resources = new ArrayList<>();
        if(settlements == null) {
            settlements = new ArrayList<>();
            settlements.add(vertex);
            this.settlements.put(playerID, settlements);
        } else {
            settlements.add(vertex);
            if(vertexLoc.getDir() == VertexDirection.NorthWest) {
                initiateResourcesNorthWest(resources, vertexLoc);
            } else {
                initiateResourcesNorthEast(resources, vertexLoc);
            }
        }
        return resources;
    }

    @Override
    public void initiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc)
            throws StructureException, InvalidLocationException, InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(vertex == null || edge == null) {
            throw new InvalidLocationException("Vertex/Edge location is not on the map");
        }
        if(vertex.hasCity()) {
            throw new StructureException("Map is already initialized");
        }
        if(!vertex.hasSettlement()) {
            throw new StructureException("Road must be connected to a settlement");
        }
        if(vertex.getPlayerID() != playerID) {
            throw new StructureException("Settlement belongs to different player");
        }
        if(vertexHasConnectingRoad(playerID, vertexLoc)) {
            throw new StructureException("Road connected to the wrong settlement");
        }
        if(!edgeConnectedToVertex(edgeLoc, vertexLoc)) {
            throw new StructureException("Road not connected to the settlement");
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
    public boolean canBuildRoad(int playerID, EdgeLocation edgeLoc) throws InvalidLocationException,
            InvalidPlayerException {
        if (playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if (edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        return !edge.hasRoad() && edgeHasConnectingRoad(playerID, edgeLoc);
    }

    @Override
    public void buildRoad(int playerID, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException,
            InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
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
    public boolean canBuildSettlement(int playerID, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException {
        if (playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if (vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        return vertex.canBuildSettlement() && !hasNeighborBuildings(vertexLoc) &&
                vertexHasConnectingRoad(playerID, vertexLoc);
    }

    @Override
    public void buildSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(!vertex.canBuildSettlement()) {
            throw new StructureException("Vertex already has a Building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        if(!vertexHasConnectingRoad(playerID, vertexLoc)) {
            throw new StructureException("Vertex location has no connecting road");
        }
        Settlement settlement = new Settlement(playerID);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        settlements.add(vertex);
    }

    @Override
    public boolean canBuildCity(int playerID, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        return vertex.canBuildCity() && vertex.getPlayerID() == playerID;
    }

    @Override
    public void buildCity(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if(!vertex.canBuildCity()) {
            throw new StructureException("A settlement needs to be built first");
        }
        if(vertex.getPlayerID() != playerID) {
            throw new StructureException("The settlement doesn't belong to the player");
        }
        if(vertex.hasCity()) {
            throw new StructureException("The vertex location already has a city");
        }
        City city = new City(playerID);
        vertex.buildCity(city);
        ArrayList<Vertex> cities = this.cities.get(playerID);
        if(cities == null) {
            cities = new ArrayList<>();
            cities.add(vertex);
            this.cities.put(playerID, cities);
        } else {
            cities.add(vertex);
        }
    }

    @Override
    public int getLongestRoadSize(int playerID) throws InvalidPlayerException {
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        return 0;
    }

    @Override
    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException{
        if(playerID < 1 || playerID > 4) {
            throw new InvalidPlayerException("PlayerID was " + playerID);
        }
        Set<PortType> portTypes = new HashSet<>();
        ArrayList<Port> ports = this.ports.get(playerID);
        if(ports != null) {
            for (Port port : ports) {
                portTypes.add(port.getPortType());
            }
        }
        return portTypes;
    }

    @Override
    public Set<Integer> moveRobber(HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException {
        Hex hex = hexes.get(hexLoc);
        if(hex == null || hex.getType() == HexType.WATER) {
            throw new InvalidLocationException("Hex location is not on the map");
        }
        if(robber.getLocation() == hexLoc) {
            throw new AlreadyRobbedException("Robber cannot remain at the same hex location");
        }
        robber.setLocation(hexLoc);
        return getPlayers(hexLoc);
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
        for (EdgeDirection anEdgeDir : edgeDir) {
            EdgeLocation edgeLoc = new EdgeLocation(hexLoc, anEdgeDir);
            Edge edge = new Edge(edgeLoc);
            edges.put(edgeLoc, edge);
        }
        for (VertexDirection aVertexDir : vertexDir) {
            VertexLocation vertexLoc = new VertexLocation(hexLoc, aVertexDir);
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
        int hexTypeIndex;
        HexType hexType;
        if(randomHexes) {
            hexTypeIndex = randomGenerator.nextInt(hexTypes.size());
            hexType = hexTypes.remove(hexTypeIndex);
        } else {
            hexType = hexTypes.remove(0);
        }
        if(hexType == HexType.DESERT) {
            Hex desertHex = new Hex(hexLoc, hexType);
            hexes.put(hexLoc, desertHex);
            robber = new Robber(hexLoc);
        } else {
            int chitIndex;
            int chit;
            if(randomChits) {
                chitIndex = randomGenerator.nextInt(chits.size());
                chit = chits.remove(chitIndex);
            } else {
                chit = chits.remove(0);
            }
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
        if(randomChits) {
            for (int i = 2; i < 13; i++) {
                if (i == 2 || i == 12) {
                    chits.add(i);
                } else if (i != 7) {
                    chits.add(i);
                    chits.add(i);
                }
            }
        } else {
            chits.add(5);
            chits.add(2);
            chits.add(6);
            chits.add(8);
            chits.add(10);
            chits.add(9);
            chits.add(3);
            chits.add(3);
            chits.add(11);
            chits.add(4);
            chits.add(8);
            chits.add(4);
            chits.add(9);
            chits.add(5);
            chits.add(10);
            chits.add(11);
            chits.add(12);
            chits.add(6);
        }
        return chits;
    }

    private ArrayList<HexType> makeHexTypes() {
        ArrayList<HexType> hexTypes = new ArrayList<>();
        if(randomHexes) {
            for (int i = 1; i < 5; i++) {
                if (i == 1) {
                    hexTypes.add(HexType.DESERT);
                    hexTypes.add(HexType.BRICK);
                    hexTypes.add(HexType.ORE);
                    hexTypes.add(HexType.SHEEP);
                    hexTypes.add(HexType.WHEAT);
                    hexTypes.add(HexType.WOOD);
                } else if (i == 2 || i == 3) {
                    hexTypes.add(HexType.BRICK);
                    hexTypes.add(HexType.ORE);
                    hexTypes.add(HexType.SHEEP);
                    hexTypes.add(HexType.WHEAT);
                    hexTypes.add(HexType.WOOD);
                } else if (i == 4) {
                    hexTypes.add(HexType.SHEEP);
                    hexTypes.add(HexType.WHEAT);
                    hexTypes.add(HexType.WOOD);
                }
            }
        } else {
            hexTypes.add(HexType.ORE);
            hexTypes.add(HexType.WHEAT);
            hexTypes.add(HexType.WOOD);
            hexTypes.add(HexType.BRICK);
            hexTypes.add(HexType.SHEEP);
            hexTypes.add(HexType.SHEEP);
            hexTypes.add(HexType.ORE);
            hexTypes.add(HexType.DESERT);
            hexTypes.add(HexType.WOOD);
            hexTypes.add(HexType.WHEAT);
            hexTypes.add(HexType.WOOD);
            hexTypes.add(HexType.WHEAT);
            hexTypes.add(HexType.BRICK);
            hexTypes.add(HexType.ORE);
            hexTypes.add(HexType.BRICK);
            hexTypes.add(HexType.SHEEP);
            hexTypes.add(HexType.WOOD);
            hexTypes.add(HexType.SHEEP);
            hexTypes.add(HexType.WHEAT);
        }
        return hexTypes;
    }

    private void makePorts() {
        ArrayList<PortType> portTypes = makePortTypes();
        int portTypeIndex;
        PortType portType;

        //first port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(1, -1, 1, -1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //second port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(2, 0, 3, 1, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //third port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(3, 2, 2, 2, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //fourth port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(2, 3, 1, 3, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //fifth port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(0, 3, 0, 3, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //sixth port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(-1, 2, -2, 1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //seventh port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(-2, 0, -3, -1, VertexDirection.NorthWest, VertexDirection.NorthEast, portType);

        //eighth port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
        portSetup(-3, -2, -2, -2, VertexDirection.NorthEast, VertexDirection.NorthWest, portType);

        //ninth port
        if(randomPorts) {
            portTypeIndex = randomGenerator.nextInt(portTypes.size());
            portType = portTypes.remove(portTypeIndex);
        } else {
            portType = portTypes.remove(0);
        }
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
        if(randomPorts) {
            for (int i = 1; i < 5; i++) {
                if (i == 1) {
                    portTypes.add(PortType.BRICK);
                    portTypes.add(PortType.ORE);
                    portTypes.add(PortType.SHEEP);
                    portTypes.add(PortType.WHEAT);
                    portTypes.add(PortType.WOOD);
                }
                portTypes.add(PortType.THREE);
            }
        } else {
            portTypes.add(PortType.ORE);
            portTypes.add(PortType.THREE);
            portTypes.add(PortType.SHEEP);
            portTypes.add(PortType.THREE);
            portTypes.add(PortType.THREE);
            portTypes.add(PortType.BRICK);
            portTypes.add(PortType.WOOD);
            portTypes.add(PortType.THREE);
            portTypes.add(PortType.WHEAT);
        }
        return portTypes;
    }

    private void getResourcesFromBuilding(java.util.Map<Integer, List<ResourceType>> resourceMap, HexLocation hexLoc,
                                          VertexDirection vertexDir) {
        ResourceType resourceType = getResourceType(hexLoc);
        VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir);
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            int playerID = vertex.getPlayerID();
            List<ResourceType> resources = resourceMap.get(playerID);
            if(resources == null) {
                resources = new ArrayList<>();
                resources.add(resourceType);
                if(vertex.hasCity()) {
                    resources.add(resourceType);
                }
                resourceMap.put(playerID, resources);
            } else {
                resources.add(resourceType);
                if(vertex.hasCity()) {
                    resources.add(resourceType);
                }
            }
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
        return right != null && right.hasBuilding();
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
        return left != null && left.hasBuilding();
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

    private void initiateResourcesNorthWest(List<ResourceType> resources, VertexLocation vertexLoc) {
        HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        HexLocation lowerLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        HexLocation rightHexLoc = vertexLoc.getHexLoc();
        ResourceType resourceType = getResourceType(upperLeftHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(lowerLeftHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(rightHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
    }

    private void initiateResourcesNorthEast(List<ResourceType> resources, VertexLocation vertexLoc) {
        HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        HexLocation lowerRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        HexLocation leftHexLoc = vertexLoc.getHexLoc();
        ResourceType resourceType = getResourceType(upperRightHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(lowerRightHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(leftHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
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
        return right != null && right.hasRoad() && right.getRoad().getPlayerID() == playerID;
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
        if(lowerRight != null && lowerRight.hasRoad() && lowerRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        Edge left = edges.get(leftEdgeLoc);
        return left != null && left.hasRoad() && left.getRoad().getPlayerID() == playerID;
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
        return rightEdgeLoc.equals(edgeLoc);
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
        return leftEdgeLoc.equals(edgeLoc);
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
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        if(vertexHasConnectingRoad(playerID, vertexLocTwo)) {
            Vertex vertex = vertices.get(vertexLocTwo);
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        return false;
    }

    private HashSet<Integer> getPlayers(HexLocation hexLoc) {
        HashSet<Integer> players = new HashSet<>();
        getPlayers(players, hexLoc, VertexDirection.NorthWest);
        getPlayers(players, hexLoc, VertexDirection.NorthEast);
        getPlayers(players, hexLoc, VertexDirection.East);
        getPlayers(players, hexLoc, VertexDirection.SouthEast);
        getPlayers(players, hexLoc, VertexDirection.SouthWest);
        getPlayers(players, hexLoc, VertexDirection.West);
        return players;
    }

    private void getPlayers(HashSet<Integer> players, HexLocation hexLoc, VertexDirection vertexDir) {
        VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir);
        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            players.add(vertex.getPlayerID());
        }
    }
}
