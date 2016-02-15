package shared.model.map;

import com.google.gson.Gson;
import com.google.gson.*;
import shared.exceptions.*;
import shared.locations.*;
import shared.definitions.*;
import shared.model.JsonSerializable;
import shared.model.map.hex.*;
import shared.model.structures.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representation of the map in the game. The game map keeps track of all locations, buildings, and chits as well as the
 * special robber character. The map uses a HashMap in the underlying implementation, which allows O(1)
 * insertion/retrieval.
 *
 * @author Joel Bradley
 */
public final class Map implements IMap, JsonSerializable{

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
    public Map(final boolean randomHexes, final boolean randomChits, final boolean randomPorts) {
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

    /**
     * Constructor that builds the map from a json blob
     * @param blob JsonObject
     */
    public Map(final JsonObject blob) {
        assert blob != null;

        final Gson gson = new Gson();
        hexes = new HashMap<>();
        chits = new HashMap<>();
        edges = new HashMap<>();
        vertices = new HashMap<>();
        settlements = new HashMap<>();
        cities = new HashMap<>();
        roads = new HashMap<>();
        ports = new HashMap<>();
        makeOceanHexes();
        makeIslandHexes(gson.fromJson(blob.getAsJsonArray("hexes"), JsonArray.class));
        makePorts(gson.fromJson(blob.getAsJsonArray("ports"), JsonArray.class));
        makeRoads(gson.fromJson(blob.getAsJsonArray("roads"), JsonArray.class));
        makeSettlements(gson.fromJson(blob.getAsJsonArray("settlements"), JsonArray.class));
        makeCities(gson.fromJson(blob.getAsJsonArray("cities"), JsonArray.class));
        HexLocation robberHexLoc = new HexLocation(blob.get("robber").getAsJsonObject());
        robber = new Robber(robberHexLoc);
    }

    /*===========================================
                   Interface Methods
     ============================================*/

    @Override
    public java.util.Map<Integer, List<ResourceType>> getResources(final int diceRoll) throws InvalidDiceRollException {
        assert diceRoll > 0;
        assert this.chits != null;
        assert this.robber != null;

        if(diceRoll < 2 || diceRoll > 12) {
            throw new InvalidDiceRollException("Dice roll was " + diceRoll);
        }
        if(diceRoll == 7) {
            throw new InvalidDiceRollException("Need to move robber instead of giving resources");
        }
        final ArrayList<HexLocation> chitList = chits.get(diceRoll);
        final java.util.Map<Integer, List<ResourceType>> resourceMap = new HashMap<>();
        chitList.stream().filter(hexLoc -> !robber.getLocation().equals(hexLoc)).forEach(hexLoc -> {
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.NorthWest);
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.NorthEast);
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.East);
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.SouthEast);
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.SouthWest);
            getResourcesFromBuilding(resourceMap, hexLoc, VertexDirection.West);
        });
        return resourceMap;
    }

    @Override
    public boolean canInitiateSettlement(final int playerID, VertexLocation vertexLoc) throws InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.settlements != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if (vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        if (this.cities.get(playerID) != null) {
            return false;
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerID);
        if (settlements != null && settlements.size() > 1) {
            return false;
        }
        final ArrayList<Edge> roads = this.roads.get(playerID);
        return !(roads != null && roads.size() > 1) && !vertex.hasBuilding() && !hasNeighborBuildings(vertexLoc);
    }

    @Override
    public List<ResourceType> initiateSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.settlements != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        if(cities != null) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        if(settlements != null && settlements.size() > 1) {
            throw new StructureException("Map is already initialized");
        }
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads != null && roads.size() > 1) {
            throw new StructureException("Map is already initialized");
        }
        if(vertex.hasBuilding()) {
            throw new StructureException("Vertex location already has a building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        final Settlement settlement = new Settlement(playerID);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        settlements = this.settlements.get(playerID);
        final List<ResourceType> resources = new ArrayList<>();
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
    public boolean canInitiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc)
            throws InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
//        assert edgeLoc.getHexLoc().getX() >= 0;
//        assert edgeLoc.getHexLoc().getY() >= 0;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.edges != null;
        assert this.cities != null;
        assert this.settlements != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if (vertex == null || edge == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        ArrayList<Vertex> cities = this.cities.get(playerID);
        if (cities != null) {
            return false;
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        if (settlements == null || settlements.size() > 2) {
            return false;
        }
        ArrayList<Edge> roads = this.roads.get(playerID);
        if (roads != null && roads.size() > 1) {
            return false;
        }
        if (!vertex.hasSettlement()) {
            return false;
        }
        if (vertex.getPlayerID() != playerID) {
            return false;
        }
        return !vertexHasConnectingRoad(playerID, vertexLoc) && edgeConnectedToVertex(edgeLoc, vertexLoc) && !edge.hasRoad();
    }

    @Override
    public void initiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc)
            throws StructureException, InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
//        assert edgeLoc.getHexLoc().getX() >= 0;
//        assert edgeLoc.getHexLoc().getY() >= 0;
        assert edgeLoc.getNormalizedLocation() != null;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.edges != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;
        assert this.settlements != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        Vertex vertex = vertices.get(vertexLoc);
        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(vertex == null || edge == null) {
            throw new InvalidLocationException("Vertex/Edge location is not on the map");
        }
        ArrayList<Vertex> cities = this.cities.get(playerID);
        if(cities != null) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        if(settlements == null) {
            throw new StructureException("Settlement needs to be built first");
        }
        if(settlements.size() > 2) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads != null && roads.size() > 1) {
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
        final Road road = new Road(playerID);
        edge.setRoad(road);
        roads = this.roads.get(playerID);
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
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
//        assert edgeLoc.getHexLoc().getX() >= 0;
//        assert edgeLoc.getHexLoc().getY() >= 0;
        assert edgeLoc.getNormalizedLocation() != null;
        assert this.edges != null;
        assert this.cities != null;
        assert this.roads != null;
        assert this.settlements != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if (edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerID);
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2|| settlements == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            return false;
        }
        return !edge.hasRoad() && edgeHasConnectingRoad(playerID, edgeLoc);
    }

    @Override
    public void buildRoad(int playerID, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
//        assert edgeLoc.getHexLoc().getX() >= 0;
//        assert edgeLoc.getHexLoc().getY() >= 0;
        assert edgeLoc.getNormalizedLocation() != null;
        assert this.edges != null;
        assert this.cities != null;
        assert this.roads != null;
        assert this.settlements != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if(edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerID);
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(edge.hasRoad()) {
            throw new StructureException("Edge already has a Road");
        }
        if(!edgeHasConnectingRoad(playerID, edgeLoc)) {
            throw new StructureException("Road must be connected to existing Road");
        }
        final Road road = new Road(playerID);
        edge.setRoad(road);
        roads = this.roads.get(playerID);
        roads.add(edge);
    }

    @Override
    public boolean canBuildSettlement(int playerID, VertexLocation vertexLoc) throws InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if (vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerID);
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2) {
            return false;
        }
        if(settlements == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            return false;
        }
        return vertex.canBuildSettlement() && !hasNeighborBuildings(vertexLoc) &&
                vertexHasConnectingRoad(playerID, vertexLoc);
    }

    @Override
    public void buildSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            throw new StructureException("Map is not initialized");
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
        final Settlement settlement = new Settlement(playerID);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerID, vertex);
        }
        settlements = this.settlements.get(playerID);
        settlements.add(vertex);
    }

    @Override
    public boolean canBuildCity(int playerID, VertexLocation vertexLoc) throws InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerID);
        final ArrayList<Vertex> cities = this.cities.get(playerID);
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2) {
            return false;
        }
        if(settlements == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            return false;
        }
        return vertex.canBuildCity() && vertex.getPlayerID() == playerID && !vertex.hasCity();
    }

    @Override
    public void buildCity(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;
        assert this.vertices != null;
        assert this.settlements != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerID);
        ArrayList<Vertex> cities = this.cities.get(playerID);
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(cities != null && (cities.size() + settlements.size()) < 2) {
            throw new StructureException("Map is not initialized");
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
        settlements = this.settlements.get(playerID);
        settlements.remove(vertex);
        final City city = new City(playerID);
        vertex.buildCity(city);
        cities = this.cities.get(playerID);
        if(cities == null) {
            cities = new ArrayList<>();
            cities.add(vertex);
            this.cities.put(playerID, cities);
        } else {
            cities.add(vertex);
        }
    }

    @Override
    public int getLongestRoadSize(int playerID) {
        assert playerID >= 0 && playerID <= 3;
        assert this.roads != null;

        int size = 0;
        final ArrayList<Edge> roads = this.roads.get(playerID);
        if(roads != null) {
            for(Edge edge : roads) {
                refreshRoads(roads);
                final ArrayList<EdgeLocation> oldConnectingRoads = new ArrayList<>();
                final int newSize = getLongestRoadSize(0, playerID, edge.getEdgeLoc(), oldConnectingRoads);
                if(newSize > size) {
                    size = newSize;
                }
            }
        }
        return size;
    }

    @Override
    public Set<PortType> getPortTypes(int playerID) {
        assert playerID >= 0 && playerID <= 3;

        final Set<PortType> portTypes = new HashSet<>();
        final ArrayList<Port> ports = this.ports.get(playerID);
        if(ports != null) {
            portTypes.addAll(ports.stream().map(Port::getPortType).collect(Collectors.toList()));
        }
        return portTypes;
    }

    @Override
    public Set<Integer> whoCanGetRobbed() {
        assert this.robber != null;

        return getPlayers(robber.getLocation());
    }

    @Override
    public Set<Integer> moveRobber(HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException {
        assert hexLoc != null;
//        assert hexLoc.getX() >= 0;
//        assert hexLoc.getY() >= 0;
        assert this.hexes != null;
        assert this.hexes.size() > 0;

        final Hex hex = hexes.get(hexLoc);
        if(hex == null || hex.getType() == HexType.WATER) {
            throw new InvalidLocationException("Hex location is not on the map");
        }
        if(robber.getLocation() == hexLoc) {
            throw new AlreadyRobbedException("Robber cannot remain at the same hex location");
        }
        robber.setLocation(hexLoc);
        return getPlayers(hexLoc);
    }

    @Override
    public JsonObject toJSON() {
        return null;
    }

    /*===========================================
                   Deserializer Methods
     ============================================*/

    private void makeIslandHexes(JsonArray jsonArray) {
        assert jsonArray != null;

        Gson gson = new Gson();
        for (JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final HexLocation hexLoc = new HexLocation(gson.fromJson(json.get("location"), JsonObject.class));
            makeEdgesForIslandHex(hexLoc);
            makeVerticesForIslandHex(hexLoc);
            if(!json.has("resource")) {
                Hex hex = new Hex(hexLoc, HexType.DESERT);
                hexes.put(hexLoc, hex);
            } else {
                String resource = json.get("resource").getAsString();
                int chit = json.get("number").getAsInt();
                ArrayList<HexLocation> chits = this.chits.get(chit);
                if(chits == null) {
                    chits = new ArrayList<>();
                    chits.add(hexLoc);
                    this.chits.put(chit, chits);
                } else {
                    chits.add(hexLoc);
                }
                switch(resource) {
                    case "wood":
                        ChitHex chitHex = new ChitHex(hexLoc, HexType.WOOD, chit);
                        hexes.put(hexLoc, chitHex);
                        break;
                    case "brick":
                        chitHex = new ChitHex(hexLoc, HexType.BRICK, chit);
                        hexes.put(hexLoc, chitHex);
                        break;
                    case "sheep":
                        chitHex = new ChitHex(hexLoc, HexType.SHEEP, chit);
                        hexes.put(hexLoc, chitHex);
                        break;
                    case "wheat":
                        chitHex = new ChitHex(hexLoc, HexType.WHEAT, chit);
                        hexes.put(hexLoc, chitHex);
                        break;
                    case "ore":
                        chitHex = new ChitHex(hexLoc, HexType.ORE, chit);
                        hexes.put(hexLoc, chitHex);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void makePorts(JsonArray jsonArray) {
        assert jsonArray != null;

        Gson gson = new Gson();
        for(JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final HexLocation hexLoc = new HexLocation(gson.fromJson(json.get("location"), JsonObject.class));
            final String direction = json.get("direction").getAsString();
            VertexLocation vertexLocOne;
            VertexLocation vertexLocTwo;
            PortType portType = PortType.THREE;
            if(json.has("resource")) {
                String resource = json.get("resource").getAsString();
                switch(resource) {
                    case "wood":
                        portType = PortType.WOOD;
                        break;
                    case "brick":
                        portType = PortType.BRICK;
                        break;
                    case "sheep":
                        portType = PortType.SHEEP;
                        break;
                    case "wheat":
                        portType = PortType.WHEAT;
                        break;
                    case "ore":
                        portType = PortType.ORE;
                        break;
                    default:
                        break;
                }
            }
            switch(direction) {
                case "NW":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.West);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.NorthWest);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                case "N":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.NorthWest);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.NorthEast);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                case "NE":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.NorthEast);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.East);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                case "SE":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.East);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.SouthEast);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                case "S":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.SouthEast);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.SouthWest);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                case "SW":
                    vertexLocOne = new VertexLocation(hexLoc, VertexDirection.SouthWest);
                    vertexLocTwo = new VertexLocation(hexLoc, VertexDirection.West);
                    vertexLocOne = vertexLocOne.getNormalizedLocation();
                    vertexLocTwo = vertexLocTwo.getNormalizedLocation();
                    makePort(portType, vertexLocOne);
                    makePort(portType, vertexLocTwo);
                    break;
                default:
                    break;
            }
        }
    }

    private void makeRoads(final JsonArray jsonArray) {
        assert jsonArray != null;

        for(final JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final int playerID = json.get("owner").getAsInt() + 1;
            EdgeLocation edgeLoc = new EdgeLocation(json.get("location").getAsJsonObject());
            edgeLoc = edgeLoc.getNormalizedLocation();
            final Edge edge = edges.get(edgeLoc);
            final Road road = new Road(playerID);
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
    }

    private void makeSettlements(final JsonArray jsonArray) {
        assert jsonArray != null;

        for(final JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final int playerID = json.get("owner").getAsInt() + 1;
            VertexLocation vertexLoc = new VertexLocation(json.get("location").getAsJsonObject());
            vertexLoc = vertexLoc.getNormalizedLocation();
            final Vertex vertex = vertices.get(vertexLoc);
            final Settlement settlement = new Settlement(playerID);
            vertex.buildSettlement(settlement);
            if(vertex.hasPort()) {
                addPort(playerID, vertex);
            }
            ArrayList<Vertex> settlements = this.settlements.get(playerID);
            if(settlements == null) {
                settlements = new ArrayList<>();
                settlements.add(vertex);
                this.settlements.put(playerID, settlements);
            } else {
                settlements.add(vertex);
            }
        }
    }

    private void makeCities(final JsonArray jsonArray) {
        assert jsonArray != null;

        for(final JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final int playerID = json.get("owner").getAsInt() + 1;
            VertexLocation vertexLoc = new VertexLocation(json.get("location").getAsJsonObject());
            vertexLoc = vertexLoc.getNormalizedLocation();
            final Vertex vertex = vertices.get(vertexLoc);
            final Settlement settlement = new Settlement(playerID);
            vertex.buildSettlement(settlement);
            if(vertex.hasPort()) {
                addPort(playerID, vertex);
            }
            final City city = new City(playerID);
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
        final HexLocation hexLoc = new HexLocation(column, diagonal);
        for (EdgeDirection anEdgeDir : edgeDir) {
            final EdgeLocation edgeLoc = new EdgeLocation(hexLoc, anEdgeDir);
            final Edge edge = new Edge(edgeLoc);
            edges.put(edgeLoc, edge);
        }
        for (VertexDirection aVertexDir : vertexDir) {
            final VertexLocation vertexLoc = new VertexLocation(hexLoc, aVertexDir);
            final Vertex vertex = new Vertex(vertexLoc);
            vertices.put(vertexLoc, vertex);
        }
        Hex oceanHex = new Hex(hexLoc, HexType.WATER);
        hexes.put(hexLoc, oceanHex);
    }

    private void makeIslandHexes() {
        // prepare to load hexes
        final ArrayList<Integer> chits = makeChits();
        final ArrayList<HexType> hexTypes = makeHexTypes();
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
        final HexLocation hexLoc = new HexLocation(column, diagonal);
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
            final Hex desertHex = new Hex(hexLoc, hexType);
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
        final EdgeLocation northWestLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthWest);
        final EdgeLocation northLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
        final EdgeLocation northEastLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
        final Edge northWest = new Edge(northWestLoc);
        final Edge north = new Edge(northLoc);
        final Edge northEast = new Edge(northEastLoc);
        edges.put(northWestLoc, northWest);
        edges.put(northLoc, north);
        edges.put(northEastLoc, northEast);
    }

    private void makeVerticesForIslandHex(HexLocation hexLoc) {
        assert hexLoc != null;

        final VertexLocation northWestLoc = new VertexLocation(hexLoc, VertexDirection.NorthWest);
        final VertexLocation northEastLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
        final Vertex northWest = new Vertex(northWestLoc);
        final Vertex northEast = new Vertex(northEastLoc);
        vertices.put(northWestLoc, northWest);
        vertices.put(northEastLoc, northEast);
    }

    private ArrayList<Integer> makeChits() {
        final ArrayList<Integer> chits = new ArrayList<>();
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
        final ArrayList<HexType> hexTypes = new ArrayList<>();
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
        final HexLocation hexLocOne = new HexLocation(columnOne, diagonalOne);
        final HexLocation hexLocTwo = new HexLocation(columnTwo, diagonalTwo);
        final VertexLocation vertexLocOne = new VertexLocation(hexLocOne, vertexDirOne);
        final VertexLocation vertexLocTwo = new VertexLocation(hexLocTwo, vertexDirTwo);
        makePort(portType, vertexLocOne);
        makePort(portType, vertexLocTwo);
    }

    private void makePort(PortType portType, VertexLocation vertexLoc) {
        final Port port = new Port(portType, vertexLoc);
        final Vertex vertex = vertices.get(vertexLoc);
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
        final ResourceType resourceType = getResourceType(hexLoc);
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            final int playerID = vertex.getPlayerID();
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
        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final VertexLocation upperLeftVertexLoc = new VertexLocation(upperLeftHexLoc, VertexDirection.NorthEast);
        final Vertex upperLeft = vertices.get(upperLeftVertexLoc);
        if (upperLeft != null && upperLeft.hasBuilding()) {
            return true;
        }
        final HexLocation lowerLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        final VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        final Vertex lowerLeft = vertices.get(lowerLeftVertexLoc);
        if(lowerLeft != null && lowerLeft.hasBuilding()) {
            return true;
        }
        final HexLocation hexLoc = vertexLoc.getHexLoc();
        final VertexLocation rightLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
        final Vertex right = vertices.get(rightLoc);
        return right != null && right.hasBuilding();
    }

    private boolean hasNeighborBuildingsNorthEast(VertexLocation vertexLoc) {
        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final VertexLocation upperRightVertexLoc = new VertexLocation(upperRightHexLoc, VertexDirection.NorthWest);
        final Vertex upperRight = vertices.get(upperRightVertexLoc);
        if (upperRight != null && upperRight.hasBuilding()) {
            return true;
        }
        final HexLocation lowerRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        final VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        final Vertex lowerRight = vertices.get(lowerRightVertexLoc);
        if(lowerRight != null && lowerRight.hasBuilding()) {
            return true;
        }
        final HexLocation hexLoc = vertexLoc.getHexLoc();
        final VertexLocation leftLoc = new VertexLocation(hexLoc, VertexDirection.NorthWest);
        final Vertex left = vertices.get(leftLoc);
        return left != null && left.hasBuilding();
    }

    private void addPort(int playerID, Vertex vertex) {
        assert playerID >= 0 && playerID <= 3;

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
        assert resources != null;
        assert vertexLoc != null;

        final HexLocation leftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final HexLocation upperHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.North);
        final HexLocation lowerHexLoc = vertexLoc.getHexLoc();
        ResourceType resourceType = getResourceType(leftHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(upperHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(lowerHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
    }

    private void initiateResourcesNorthEast(List<ResourceType> resources, VertexLocation vertexLoc) {
        assert resources != null;
        assert vertexLoc != null;

        final HexLocation rightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final HexLocation lowerHexLoc = vertexLoc.getHexLoc();
        final HexLocation upperHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.North);
        ResourceType resourceType = getResourceType(rightHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(lowerHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
        resourceType = getResourceType(upperHexLoc);
        if(resourceType != null) {
            resources.add(resourceType);
        }
    }

    private ResourceType getResourceType(HexLocation hexLoc) {
        assert hexLoc != null;

        final Hex hex = hexes.get(hexLoc);
        final HexType hexType = hex.getType();
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
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            return vertexHasConnectingRoadNorthWest(playerID, vertexLoc);
        } else {
            return vertexHasConnectingRoadNorthEast(playerID, vertexLoc);
        }
    }

    private boolean vertexHasConnectingRoadNorthWest(int playerID, VertexLocation vertexLoc) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;

        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        final Edge upperLeft = edges.get(upperLeftEdgeLoc);
        if(upperLeft != null && upperLeft.hasRoad() && upperLeft.getRoad().getPlayerID() == playerID) {
            return true;
        }
        final EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        final Edge lowerLeft = edges.get(lowerLeftEdgeLoc);
        if(lowerLeft != null && lowerLeft.hasRoad() && lowerLeft.getRoad().getPlayerID() == playerID) {
            return true;
        }
        final EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge right = edges.get(rightEdgeLoc);
        return right != null && right.hasRoad() && right.getRoad().getPlayerID() == playerID;
    }

    private boolean vertexHasConnectingRoadNorthEast(int playerID, VertexLocation vertexLoc) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;

        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        final Edge upperRight = edges.get(upperRightEdgeLoc);
        if(upperRight != null && upperRight.hasRoad() && upperRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        final EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        final Edge lowerRight = edges.get(lowerRightEdgeLoc);
        if(lowerRight != null && lowerRight.hasRoad() && lowerRight.getRoad().getPlayerID() == playerID) {
            return true;
        }
        final EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge left = edges.get(leftEdgeLoc);
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
        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        if(upperLeftEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        final EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        if(lowerLeftEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        final EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        return rightEdgeLoc.equals(edgeLoc);
    }

    private boolean edgeConnectedToVertexNorthEast(EdgeLocation edgeLoc, VertexLocation vertexLoc) {
        assert edgeLoc != null;
        assert vertexLoc != null;

        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        if(upperRightEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        final EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        if(lowerRightEdgeLoc.equals(edgeLoc)) {
            return true;
        }
        final EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        return leftEdgeLoc.equals(edgeLoc);
    }

    private boolean edgeHasConnectingRoad(int playerID, EdgeLocation edgeLoc) {
        assert playerID >= 0 && playerID <= 3;

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
        assert playerID >= 0 && playerID <= 3;

        final HexLocation lowerLeftHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        final VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        final VertexLocation upperRightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        return edgeHasConnectingRoad(playerID, lowerLeftVertexLoc, upperRightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorth(int playerID, EdgeLocation edgeLoc) {
        assert playerID >= 0 && playerID <= 3;

        final VertexLocation leftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        final VertexLocation rightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerID, leftVertexLoc, rightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorthEast(int playerID, EdgeLocation edgeLoc) {
        assert playerID >= 0 && playerID <= 3;

        final HexLocation lowerRightHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        final VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        final VertexLocation upperLeftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerID, lowerRightVertexLoc, upperLeftVertexLoc);
    }

    private boolean edgeHasConnectingRoad(int playerID, VertexLocation vertexLocOne, VertexLocation vertexLocTwo) {
        assert playerID >= 0 && playerID <= 3;

        if(vertexHasConnectingRoad(playerID, vertexLocOne)) {
            final Vertex vertex = vertices.get(vertexLocOne);
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        if(vertexHasConnectingRoad(playerID, vertexLocTwo)) {
            final Vertex vertex = vertices.get(vertexLocTwo);
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        return false;
    }

    private HashSet<Integer> getPlayers(HexLocation hexLoc) {
        assert hexLoc != null;

        final HashSet<Integer> players = new HashSet<>();
        getPlayers(players, hexLoc, VertexDirection.NorthWest);
        getPlayers(players, hexLoc, VertexDirection.NorthEast);
        getPlayers(players, hexLoc, VertexDirection.East);
        getPlayers(players, hexLoc, VertexDirection.SouthEast);
        getPlayers(players, hexLoc, VertexDirection.SouthWest);
        getPlayers(players, hexLoc, VertexDirection.West);
        return players;
    }

    private void getPlayers(HashSet<Integer> players, HexLocation hexLoc, VertexDirection vertexDir) {
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            players.add(vertex.getPlayerID());
        }
    }

    private void refreshRoads(ArrayList<Edge> roads) {
        assert roads != null;
        for(Edge edge: roads) {
            edge.getRoad().setVisited(false);
        }
    }

    private int getLongestRoadSize(int size, int playerID, EdgeLocation edgeLoc, ArrayList<EdgeLocation> oldConnectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert oldConnectingRoads != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if(edge.getRoad().isVisited()) {
            return size;
        } else {
            edge.getRoad().setVisited(true);
            size++;
            final ArrayList<EdgeLocation> connectingRoads = getConnectingRoads(playerID, edgeLoc);
            int oldSize = size;
            for(EdgeLocation road : connectingRoads) {
                if(!oldConnectedToNew(oldConnectingRoads, road)) {
                    int newSize = getLongestRoadSize(oldSize, playerID, road, connectingRoads);
                    if (newSize > size) {
                        size = newSize;
                    }
                }
            }
            edge.getRoad().setVisited(false);
            return size;
        }
    }

    private boolean oldConnectedToNew(ArrayList<EdgeLocation> oldConnectingRoads, EdgeLocation road) {
        assert oldConnectingRoads != null;
        assert road != null;

        return oldConnectingRoads.contains(road);
    }

    private ArrayList<EdgeLocation> getConnectingRoads(int playerID, EdgeLocation edgeLoc) {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;

        edgeLoc = edgeLoc.getNormalizedLocation();

        // TODO - review two lines below. Why is empty ArrayList being passed?
        ArrayList<EdgeLocation> connectingRoads = new ArrayList<>();
        connectingRoads = getConnectingRoadsForEdge(playerID, edgeLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdge(int playerID, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;
        assert connectingRoads != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        if(edgeLoc.getDir() == EdgeDirection.NorthWest) {
            connectingRoads = getConnectingRoadsForEdgeNorthWest(playerID, edgeLoc, connectingRoads);
        } else if(edgeLoc.getDir() == EdgeDirection.North) {
            connectingRoads = getConnectingRoadsForEdgeNorth(playerID, edgeLoc, connectingRoads);
        } else {
            connectingRoads = getConnectingRoadsForEdgeNorthEast(playerID, edgeLoc, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorthWest(int playerID, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;

        final HexLocation lowerLeftHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        final VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        final VertexLocation upperRightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        connectingRoads = getConnectingRoadsForEdge(playerID, lowerLeftVertexLoc, upperRightVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorth(int playerID, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;

        final VertexLocation leftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        final VertexLocation rightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        connectingRoads = getConnectingRoadsForEdge(playerID, leftVertexLoc, rightVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorthEast(int playerID, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert edgeLoc != null;

        final HexLocation lowerRightHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        final VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        final VertexLocation upperLeftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        connectingRoads =  getConnectingRoadsForEdge(playerID, lowerRightVertexLoc, upperLeftVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdge(int playerID, VertexLocation vertexLocOne, VertexLocation vertexLocTwo, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLocOne != null;
        assert vertexLocTwo != null;
        assert connectingRoads != null;
        assert !vertexLocOne.equals(vertexLocTwo);

        Vertex vertex = vertices.get(vertexLocOne);
        if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
            connectingRoads = getConnectingRoadsForVertex(playerID, vertexLocOne, connectingRoads);
        } else if(vertex != null && !vertex.hasBuilding()) {
            connectingRoads = getConnectingRoadsForVertex(playerID, vertexLocOne, connectingRoads);
        }
        vertex = vertices.get(vertexLocTwo);
        if(vertex != null && vertex.hasBuilding() && vertex.getPlayerID() == playerID) {
            connectingRoads = getConnectingRoadsForVertex(playerID, vertexLocTwo, connectingRoads);
        } else if(vertex != null && !vertex.hasBuilding()) {
            connectingRoads = getConnectingRoadsForVertex(playerID, vertexLocTwo, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertex(int playerID, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            connectingRoads = getConnectingRoadsForVertexNorthWest(playerID, vertexLoc, connectingRoads);
        } else {
            connectingRoads = getConnectingRoadsForVertexNorthEast(playerID, vertexLoc, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertexNorthWest(int playerID, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        final Edge upperLeft = edges.get(upperLeftEdgeLoc);
        if(upperLeft != null && upperLeft.hasRoad() && upperLeft.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(upperLeftEdgeLoc);
        }
        final EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        final Edge lowerLeft = edges.get(lowerLeftEdgeLoc);
        if(lowerLeft != null && lowerLeft.hasRoad() && lowerLeft.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(lowerLeftEdgeLoc);
        }
        final EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge right = edges.get(rightEdgeLoc);
        if(right != null && right.hasRoad() && right.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(rightEdgeLoc);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertexNorthEast(int playerID, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerID >= 0 && playerID <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        final Edge upperRight = edges.get(upperRightEdgeLoc);
        if(upperRight != null && upperRight.hasRoad() && upperRight.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(upperRightEdgeLoc);
        }
        final EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        final Edge lowerRight = edges.get(lowerRightEdgeLoc);
        if(lowerRight != null && lowerRight.hasRoad() && lowerRight.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(lowerRightEdgeLoc);
        }
        final EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge left = edges.get(leftEdgeLoc);
        if(left != null && left.hasRoad() && left.getRoad().getPlayerID() == playerID) {
            connectingRoads.add(leftEdgeLoc);
        }
        return connectingRoads;
    }
}
