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
 * Representation of the map in the game. The game map keeps track of all
 * locations, buildings, and chits as well as the special robber character. The
 * map uses a HashMap in the underlying implementation, which allows O(1)
 * insertion/retrieval.
 *
 * @author Joel Bradley
 */
public final class Map implements IMap, JsonSerializable {

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
        robberHexLoc = getModelHexLocation(robberHexLoc);
        robber = new Robber(robberHexLoc);
    }

    /*===========================================
                   Interface Methods
     ============================================*/

    /**
     * Gives resources out to players
     * @param diceRoll int In the range of 2 to 12, excluding 7
     * @return A map of resources to give to each player
     * @throws InvalidDiceRollException Throws exception if diceRoll is less than 2 or greater than 12 or equal to 7
     */
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

    /**
     * Informs if a Settlement can be initiated at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     */
    @Override
    public boolean canInitiateSettlement(final int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.settlements != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if (vertex == null) {
            return false;
        }
        if (this.cities.get(playerIndex) != null) {
            return false;
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        if (settlements != null && settlements.size() > 1) {
            return false;
        }
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        return !(roads != null && roads.size() > 1) && !vertex.hasBuilding() && !hasNeighborBuildings(vertexLoc);
    }

    /**
     * Builds a Settlement in setup phase and gives out resources if it is the players second turn
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return A list of resources to give to the player
     * @throws StructureException Throws exception if the Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    @Override
    public List<ResourceType> initiateSettlement(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
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
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        if(cities != null) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        if(settlements != null && settlements.size() > 1) {
            throw new StructureException("Map is already initialized");
        }
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads != null && roads.size() > 1) {
            throw new StructureException("Map is already initialized");
        }
        if(vertex.hasBuilding()) {
            throw new StructureException("Vertex location already has a building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        final Settlement settlement = new Settlement(playerIndex);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerIndex, vertex);
        }
        settlements = this.settlements.get(playerIndex);
        final List<ResourceType> resources = new ArrayList<>();
        if(settlements == null) {
            settlements = new ArrayList<>();
            settlements.add(vertex);
            this.settlements.put(playerIndex, settlements);
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

    /**
     * Informs if a road can be initiated at an edge location if connected to a vertex location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @return boolean
     */
    @Override
    public boolean canInitiateRoad(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
        assert this.vertices != null;
        assert this.edges != null;
        assert this.cities != null;
        assert this.settlements != null;
        assert this.roads != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        ArrayList<Vertex> possibleVertices = new ArrayList<>();
        if(edgeLoc.getDir() == EdgeDirection.NorthWest) {
            Vertex west = vertices.get(new VertexLocation(edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest), VertexDirection.NorthEast));
            Vertex northWest = vertices.get(new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest));
            possibleVertices.add(west);
            possibleVertices.add(northWest);
        } else if(edgeLoc.getDir() == EdgeDirection.North) {
            Vertex northWest = vertices.get(new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest));
            Vertex northEast = vertices.get(new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast));
            possibleVertices.add(northWest);
            possibleVertices.add(northEast);
        } else {
            Vertex northEast = vertices.get(new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast));
            Vertex east = vertices.get(new VertexLocation(edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast), VertexDirection.NorthWest));
            possibleVertices.add(northEast);
            possibleVertices.add(east);
        }
        if (edge == null) {
            return false;
        }
        ArrayList<Vertex> cities = this.cities.get(playerIndex);
        if (cities != null) {
            return false;
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        if (settlements == null || settlements.size() > 2) {
            return false;
        }
        ArrayList<Edge> roads = this.roads.get(playerIndex);
        if (roads != null && roads.size() > 1) {
            return false;
        }
        for(Vertex vertex : possibleVertices) {
            if(vertex.hasSettlement() && vertex.getPlayerIndex() == playerIndex && !vertexHasConnectingRoad(playerIndex,
                    vertex.getVertexLoc()) && !edge.hasRoad()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a Road in setup phase
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws exception if the Road can't be built at the EdgeLocation
     * @throws InvalidLocationException Throws exception if vertex/edge location is not on the map
     */
    @Override
    public void initiateRoad(int playerIndex, EdgeLocation edgeLoc)
            throws StructureException, InvalidLocationException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
        assert edgeLoc.getNormalizedLocation() != null;
        assert this.edges != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;
        assert this.settlements != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        Edge edge = edges.get(edgeLoc);
        if(edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        ArrayList<Vertex> cities = this.cities.get(playerIndex);
        if(cities != null) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        if(settlements == null) {
            throw new StructureException("Settlement needs to be built first");
        }
        if(settlements.size() > 2) {
            throw new StructureException("Map is already initialized");
        }
        ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads != null && roads.size() > 1) {
            throw new StructureException("Map is already initialized");
        }
        if(canInitiateRoad(playerIndex, edgeLoc)) {
            final Road road = new Road(playerIndex);
            edge.setRoad(road);
            roads = this.roads.get(playerIndex);
            if(roads == null) {
                roads = new ArrayList<>();
                roads.add(edge);
                this.roads.put(playerIndex, roads);
            } else {
                roads.add(edge);
            }
        }
    }

    /**
     * Informs if a Road can be built at an edge location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @return boolean
     */
    @Override
    public boolean canBuildRoad(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
        assert edgeLoc.getNormalizedLocation() != null;
        assert this.edges != null;
        assert this.cities != null;
        assert this.roads != null;
        assert this.settlements != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if (edge == null) {
            return false;
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            return false;
        }
        if(settlements == null && cities == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(settlements == null && cities.size() < 2) {
            return false;
        }
        return !edge.hasRoad() && edgeHasConnectingRoad(playerIndex, edgeLoc);
    }

    /**
     * Builds a Road at an edge location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws Exception if Road can't be built at the edge location
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     */
    @Override
    public void buildRoad(final int playerIndex, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
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
        final ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(edge.hasRoad()) {
            throw new StructureException("Edge already has a Road");
        }
        if(!edgeHasConnectingRoad(playerIndex, edgeLoc)) {
            throw new StructureException("Road must be connected to existing Road");
        }

        // TODO - unused variable
        final Road road = new Road(playerIndex);
        edge.setRoad(road);

        // TODO - this condition is always false - should refactor
        if(roads == null) {
            roads = new ArrayList<>();
            roads.add(edge);
            this.roads.put(playerIndex, roads);
        } else {
            roads.add(edge);
        }
    }

    /**
     * Informs if a Settlement can be built at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     */
    @Override
    public boolean canBuildSettlement(int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if (vertex == null) {
            return false;
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            return false;
        }
        if(settlements == null && cities == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(settlements == null && cities.size() < 2) {
            return false;
        }
        return vertex.canBuildSettlement() && !hasNeighborBuildings(vertexLoc) &&
                vertexHasConnectingRoad(playerIndex, vertexLoc);
    }

    /**
     * Builds a Settlement at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    @Override
    public void buildSettlement(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(!vertex.canBuildSettlement()) {
            throw new StructureException("Vertex already has a Building");
        }
        if(hasNeighborBuildings(vertexLoc)) {
            throw new StructureException("Vertex location has a neighboring building");
        }
        if(!vertexHasConnectingRoad(playerIndex, vertexLoc)) {
            throw new StructureException("Vertex location has no connecting road");
        }
        final Settlement settlement = new Settlement(playerIndex);
        vertex.buildSettlement(settlement);
        if(vertex.hasPort()) {
            addPort(playerIndex, vertex);
        }

        // TODO - this condition is always false - refactor
        if(settlements == null) {
            settlements = new ArrayList<>();
            settlements.add(vertex);
            this.settlements.put(playerIndex, settlements);
        } else {
            settlements.add(vertex);
        }
    }

    /**
     * Informs if a City can be built at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     */
    @Override
    public boolean canBuildCity(int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
        assert vertexLoc.getNormalizedLocation() != null;
        assert this.vertices != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            return false;
        }
        final ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        final ArrayList<Vertex> cities = this.cities.get(playerIndex);
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            return false;
        }
        if(settlements == null && cities == null) {
            return false;
        }
        if(cities == null && settlements.size() < 2) {
            return false;
        }
        if(settlements == null && cities.size() < 2) {
            return false;
        }
        return vertex.canBuildCity() && vertex.getPlayerIndex() == playerIndex && !vertex.hasCity();
    }

    /**
     * Builds a City at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a City can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    @Override
    public void buildCity(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
        assert this.vertices != null;
        assert this.settlements != null;
        assert this.cities != null;
        assert this.roads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex == null) {
            throw new InvalidLocationException("Vertex location is not on the map");
        }
        ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
        ArrayList<Vertex> cities = this.cities.get(playerIndex);
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads == null || roads.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities == null) {
            throw new StructureException("Map is not initialized");
        }
        if(cities == null && settlements.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(settlements == null && cities.size() < 2) {
            throw new StructureException("Map is not initialized");
        }
        if(!vertex.canBuildCity()) {
            throw new StructureException("A settlement needs to be built first");
        }
        if(vertex.getPlayerIndex() != playerIndex) {
            throw new StructureException("The settlement doesn't belong to the player");
        }
        if(vertex.hasCity()) {
            throw new StructureException("The vertex location already has a city");
        }
        settlements.remove(vertex);
        final City city = new City(playerIndex);
        vertex.buildCity(city);
        if(vertex.hasPort()) {
            addPort(playerIndex, vertex);
        }
        if(cities == null) {
            cities = new ArrayList<>();
            cities.add(vertex);
            this.cities.put(playerIndex, cities);
        } else {
            cities.add(vertex);
        }
    }

    /**
     * Gets the size of the longest road of a player
     * @param playerIndex int
     * @return int Size of the longest road of a player
     */
    @Override
    public int getLongestRoadSize(int playerIndex) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert this.roads != null;

        int size = 0;
        final ArrayList<Edge> roads = this.roads.get(playerIndex);
        if(roads != null) {
            for(Edge edge : roads) {
                refreshRoads(roads);
                final ArrayList<EdgeLocation> oldConnectingRoads = new ArrayList<>();
                final int newSize = getLongestRoadSize(0, playerIndex, edge.getEdgeLoc(), oldConnectingRoads);
                if(newSize > size) {
                    size = newSize;
                }
            }
        }
        return size;
    }

    /**
     * Gets all the port types that a player has
     * @param playerIndex int
     * @return A set of port types
     */
    @Override
    public Set<PortType> getPortTypes(int playerIndex) {
        assert playerIndex >= 0 && playerIndex <= 3;

        final Set<PortType> portTypes = new HashSet<>();
        final ArrayList<Port> ports = this.ports.get(playerIndex);
        if(ports != null) {
            portTypes.addAll(ports.stream().map(Port::getPortType).collect(Collectors.toList()));
        }
        return portTypes;
    }

    /**
     * Informs if the robber can be moved to a hex location
     * @param hexLoc HexLocation
     * @return boolean
     */
    @Override
    public boolean canMoveRobber(HexLocation hexLoc) {
        Hex hex = hexes.get(hexLoc);
        if(hex == null || hex.getType() == HexType.WATER) {
            return false;
        }
        return !robber.getLocation().equals(hexLoc);
    }

    /**
     * Informs who can be robbed at a hex location
     * @return A set of playerIndex that can be robbed
     */
    @Override
    public Set<Integer> whoCanGetRobbed(int playerIndex, HexLocation hexLoc) {
        assert this.robber != null;
        return getPlayers(playerIndex, hexLoc);
    }

    /**
     * Moves the Robber to a new hex location
     * @param hexLoc HexLocation
     * @return A set of playerIndex that can be robbed
     * @throws AlreadyRobbedException Throws exception if Robber is moved to where it is already at
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    @Override
    public Set<Integer> moveRobber(int playerIndex, HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException {
        assert hexLoc != null;
        assert this.hexes != null;
        assert this.hexes.size() > 0;

        final Hex hex = hexes.get(hexLoc);
        if(hex == null || hex.getType() == HexType.WATER) {
            throw new InvalidLocationException("Hex location is not on the map");
        }
        if(robber.getLocation().equals(hexLoc)) {
            throw new AlreadyRobbedException("Robber cannot remain at the same hex location");
        }
        robber.setLocation(hexLoc);
        return getPlayers(playerIndex, hexLoc);
    }

    /**
     * Deletes a road if a player cancels in the middle of the play road building card
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws InvalidLocationException throws exception if edge locaiton is not on the map
     * @throws StructureException throws exception if edge location doesn't have a road to delete
     */
    @Override
    public void deleteRoad(int playerIndex, EdgeLocation edgeLoc) throws InvalidLocationException, StructureException {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert edgeLoc.getDir() != null;
        assert edgeLoc.getHexLoc() != null;
        assert edgeLoc.getNormalizedLocation() != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if(edge == null) {
            throw new InvalidLocationException("Edge location is not on the map");
        }
        if(edge.getRoad() == null) {
            throw new StructureException("Edge doesn't have a road to delete");
        }
        edge.setRoad(null);
        ArrayList<Edge> roads = this.roads.get(playerIndex);
        for(int i=0; i<roads.size(); i++) {
            if (roads.get(i).getEdgeLoc().equals(edge.getEdgeLoc())) {
                roads.remove(i);
            }
        }
    }

    /*===========================================
                   Serializer Methods
     ============================================*/

    private HexLocation getServerHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()-hexLoc.getX());
    }

    private EdgeLocation getServerEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getServerHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    private VertexLocation getServerVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getServerHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }

    /**
     * Serializes the Map to a JsonObject
     * @return JsonObject
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.add("hexes", serializeHexes());
        json.add("ports", serializePorts());
        json.add("roads", serializeRoads());
        json.add("settlements", serializeSettlements());
        json.add("cities", serializeCities());
        json.addProperty("radius", 3);
        json.add("robber", getServerHexLocation(robber.getLocation()).toJSON());
        return json;
    }

    private JsonArray serializeHexes() {
        JsonArray hexes = new JsonArray();
        for(java.util.Map.Entry<HexLocation, Hex> entry : this.hexes.entrySet()) {
            JsonObject hex = new JsonObject();
            HexLocation hexLoc = getServerHexLocation(entry.getKey());
            hex.add("location", hexLoc.toJSON());
            HexType hexType = entry.getValue().getType();
            if(hexType != HexType.WATER && hexType != HexType.DESERT) {
                switch(hexType) {
                    case ORE:
                        hex.addProperty("resource", "ore");
                        break;
                    case BRICK:
                        hex.addProperty("resource", "brick");
                        break;
                    case SHEEP:
                        hex.addProperty("resource", "sheep");
                        break;
                    case WHEAT:
                        hex.addProperty("resource", "wheat");
                        break;
                    case WOOD:
                        hex.addProperty("resource", "wood");
                        break;
                    default:
                        break;
                }
                hex.addProperty("number", ((ChitHex)entry.getValue()).getChit());
            }
            if(hexType != HexType.WATER) {
                hexes.add(hex);
            }
        }
        return hexes;
    }

    private JsonArray serializePorts() {
        JsonArray ports = new JsonArray();
        ports.add(serializePort(1, -2, VertexDirection.SouthWest));
        ports.add(serializePort(3, 0, VertexDirection.West));
        ports.add(serializePort(3, 2, VertexDirection.NorthWest));
        ports.add(serializePort(2, 3, VertexDirection.NorthWest));
        ports.add(serializePort(0, 3, VertexDirection.NorthEast));
        ports.add(serializePort(-2, 1, VertexDirection.East));
        ports.add(serializePort(-3, -1, VertexDirection.East));
        ports.add(serializePort(-3, -3, VertexDirection.SouthEast));
        ports.add(serializePort(-1, -3, VertexDirection.SouthWest));
        return ports;
    }

    private JsonObject serializePort(int x, int y, VertexDirection vertexDir) {
        JsonObject port = new JsonObject();
        HexLocation hexLoc = new HexLocation(x, y);
        VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir);
        vertexLoc = vertexLoc.getNormalizedLocation();
        PortType portType = vertices.get(vertexLoc).getPort().getPortType();
        if(portType != PortType.THREE) {
            switch(portType) {
                case ORE:
                    port.addProperty("resource", "ore");
                    break;
                case BRICK:
                    port.addProperty("resource", "brick");
                    break;
                case SHEEP:
                    port.addProperty("resource", "sheep");
                    break;
                case WHEAT:
                    port.addProperty("resource", "wheat");
                    break;
                case WOOD:
                    port.addProperty("resource", "wood");
                    break;
                default:
                    break;
            }
            port.addProperty("ratio", 2);
        } else {
            port.addProperty("ratio", 3);
        }
        port.add("location", getServerHexLocation(hexLoc).toJSON());
        switch(vertexDir) {
            case NorthWest:
                port.addProperty("direction", "NW");
                break;
            case NorthEast:
                port.addProperty("direction", "N");
                break;
            case East:
                port.addProperty("direction", "NE");
                break;
            case SouthEast:
                port.addProperty("direction", "SE");
                break;
            case SouthWest:
                port.addProperty("direction", "S");
                break;
            case West:
                port.addProperty("direction", "SW");
                break;
            default:
                break;
        }
        return port;
    }

    private JsonArray serializeRoads() {
        JsonArray roads = new JsonArray();
        if(this.roads != null) {
            for (java.util.Map.Entry<Integer, ArrayList<Edge>> entry : this.roads.entrySet()) {
                for (Edge road : entry.getValue()) {
                    JsonObject json = new JsonObject();
                    json.addProperty("owner", entry.getKey());
                    json.add("location", getServerEdgeLocation(road.getEdgeLoc()).toJSON());
                    roads.add(json);
                }
            }
        }
        return roads;
    }

    private JsonArray serializeSettlements() {
        JsonArray settlements = new JsonArray();
        if(this.settlements != null) {
            for (java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : this.settlements.entrySet()) {
                for (Vertex settlement : entry.getValue()) {
                    JsonObject json = new JsonObject();
                    json.addProperty("owner", entry.getKey());
                    json.add("location", getServerVertexLocation(settlement.getVertexLoc()).toJSON());
                    settlements.add(json);
                }
            }
        }
        return settlements;
    }

    private JsonArray serializeCities() {
        JsonArray cities = new JsonArray();
        if(this.cities != null) {
            for (java.util.Map.Entry<Integer, ArrayList<Vertex>> entry : this.cities.entrySet()) {
                for (Vertex city : entry.getValue()) {
                    JsonObject json = new JsonObject();
                    json.addProperty("owner", entry.getKey());
                    json.add("location", getServerVertexLocation(city.getVertexLoc()).toJSON());
                    cities.add(json);
                }
            }
        }
        return cities;
    }

    /*===========================================
                   Deserializer Methods
     ============================================*/

    private HexLocation getModelHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()+hexLoc.getX());
    }

    private EdgeLocation getModelEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getModelHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    private VertexLocation getModelVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getModelHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }

    private void makeIslandHexes(JsonArray jsonArray) {
        assert jsonArray != null;

        Gson gson = new Gson();
        for (JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            HexLocation hexLoc = new HexLocation(gson.fromJson(json.get("location"), JsonObject.class));
            hexLoc = getModelHexLocation(hexLoc);
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
            HexLocation hexLoc = new HexLocation(gson.fromJson(json.get("location"), JsonObject.class));
            hexLoc = getModelHexLocation(hexLoc);
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
            final int playerIndex = json.get("owner").getAsInt();
            EdgeLocation edgeLoc = new EdgeLocation(json.get("location").getAsJsonObject());
            edgeLoc = getModelEdgeLocation(edgeLoc);
            edgeLoc = edgeLoc.getNormalizedLocation();
            final Edge edge = edges.get(edgeLoc);
            final Road road = new Road(playerIndex);
            edge.setRoad(road);
            ArrayList<Edge> roads = this.roads.get(playerIndex);
            if(roads == null) {
                roads = new ArrayList<>();
                roads.add(edge);
                this.roads.put(playerIndex, roads);
            } else {
                roads.add(edge);
            }
        }
    }

    private void makeSettlements(final JsonArray jsonArray) {
        assert jsonArray != null;

        for(final JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final int playerIndex = json.get("owner").getAsInt();
            VertexLocation vertexLoc = new VertexLocation(json.get("location").getAsJsonObject());
            vertexLoc = getModelVertexLocation(vertexLoc);
            vertexLoc = vertexLoc.getNormalizedLocation();
            final Vertex vertex = vertices.get(vertexLoc);
            final Settlement settlement = new Settlement(playerIndex);
            vertex.buildSettlement(settlement);
            if(vertex.hasPort()) {
                addPort(playerIndex, vertex);
            }
            ArrayList<Vertex> settlements = this.settlements.get(playerIndex);
            if(settlements == null) {
                settlements = new ArrayList<>();
                settlements.add(vertex);
                this.settlements.put(playerIndex, settlements);
            } else {
                settlements.add(vertex);
            }
        }
    }

    private void makeCities(final JsonArray jsonArray) {
        assert jsonArray != null;

        for(final JsonElement jsonElem : jsonArray) {
            final JsonObject json = jsonElem.getAsJsonObject();
            final int playerIndex = json.get("owner").getAsInt();
            VertexLocation vertexLoc = new VertexLocation(json.get("location").getAsJsonObject());
            vertexLoc = getModelVertexLocation(vertexLoc);
            vertexLoc = vertexLoc.getNormalizedLocation();
            final Vertex vertex = vertices.get(vertexLoc);
            final Settlement settlement = new Settlement(playerIndex);
            vertex.buildSettlement(settlement);
            if(vertex.hasPort()) {
                addPort(playerIndex, vertex);
            }
            final City city = new City(playerIndex);
            vertex.buildCity(city);
            ArrayList<Vertex> cities = this.cities.get(playerIndex);
            if(cities == null) {
                cities = new ArrayList<>();
                cities.add(vertex);
                this.cities.put(playerIndex, cities);
            } else {
                cities.add(vertex);
            }
        }
    }

    /*===========================================
                   Constructor Methods
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

    /*===========================================
                   Algorithms
     ============================================*/

    private void getResourcesFromBuilding(java.util.Map<Integer, List<ResourceType>> resourceMap, HexLocation hexLoc,
                                          VertexDirection vertexDir) {
        final ResourceType resourceType = getResourceType(hexLoc);
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding()) {
            final int playerIndex = vertex.getPlayerIndex();
            List<ResourceType> resources = resourceMap.get(playerIndex);
            if(resources == null) {
                resources = new ArrayList<>();
                resources.add(resourceType);
                if(vertex.hasCity()) {
                    resources.add(resourceType);
                }
                resourceMap.put(playerIndex, resources);
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

    private void addPort(int playerIndex, Vertex vertex) {
        assert playerIndex >= 0 && playerIndex <= 3;

        if(vertex.hasPort()) {
            ArrayList<Port> ports = this.ports.get(playerIndex);
            if(ports == null) {
                ports = new ArrayList<>();
                ports.add(vertex.getPort());
                this.ports.put(playerIndex, ports);
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

    private boolean vertexHasConnectingRoad(int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            return vertexHasConnectingRoadNorthWest(playerIndex, vertexLoc);
        } else {
            return vertexHasConnectingRoadNorthEast(playerIndex, vertexLoc);
        }
    }

    private boolean vertexHasConnectingRoadNorthWest(int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;

        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        final Edge upperLeft = edges.get(upperLeftEdgeLoc);
        if(upperLeft != null && upperLeft.hasRoad() && upperLeft.getRoad().getPlayerIndex() == playerIndex) {
            return true;
        }
        final EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        final Edge lowerLeft = edges.get(lowerLeftEdgeLoc);
        if(lowerLeft != null && lowerLeft.hasRoad() && lowerLeft.getRoad().getPlayerIndex() == playerIndex) {
            return true;
        }
        final EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge right = edges.get(rightEdgeLoc);
        return right != null && right.hasRoad() && right.getRoad().getPlayerIndex() == playerIndex;
    }

    private boolean vertexHasConnectingRoadNorthEast(int playerIndex, VertexLocation vertexLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;

        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        final Edge upperRight = edges.get(upperRightEdgeLoc);
        if(upperRight != null && upperRight.hasRoad() && upperRight.getRoad().getPlayerIndex() == playerIndex) {
            return true;
        }
        final EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        final Edge lowerRight = edges.get(lowerRightEdgeLoc);
        if(lowerRight != null && lowerRight.hasRoad() && lowerRight.getRoad().getPlayerIndex() == playerIndex) {
            return true;
        }
        final EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge left = edges.get(leftEdgeLoc);
        return left != null && left.hasRoad() && left.getRoad().getPlayerIndex() == playerIndex;
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

    private boolean edgeHasConnectingRoad(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;

        edgeLoc = edgeLoc.getNormalizedLocation();
        if(edgeLoc.getDir() == EdgeDirection.NorthWest) {
            return edgeHasConnectingRoadNorthWest(playerIndex, edgeLoc);
        } else if(edgeLoc.getDir() == EdgeDirection.North) {
            return edgeHasConnectingRoadNorth(playerIndex, edgeLoc);
        } else {
            return edgeHasConnectingRoadNorthEast(playerIndex, edgeLoc);
        }
    }

    private boolean edgeHasConnectingRoadNorthWest(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;

        final HexLocation lowerLeftHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        final VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        final VertexLocation upperRightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        return edgeHasConnectingRoad(playerIndex, lowerLeftVertexLoc, upperRightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorth(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;

        final VertexLocation leftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        final VertexLocation rightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerIndex, leftVertexLoc, rightVertexLoc);
    }

    private boolean edgeHasConnectingRoadNorthEast(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;

        final HexLocation lowerRightHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        final VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        final VertexLocation upperLeftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        return edgeHasConnectingRoad(playerIndex, lowerRightVertexLoc, upperLeftVertexLoc);
    }

    private boolean edgeHasConnectingRoad(int playerIndex, VertexLocation vertexLocOne, VertexLocation vertexLocTwo) {
        assert playerIndex >= 0 && playerIndex <= 3;

        if(vertexHasConnectingRoad(playerIndex, vertexLocOne)) {
            final Vertex vertex = vertices.get(vertexLocOne);
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerIndex() == playerIndex) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        if(vertexHasConnectingRoad(playerIndex, vertexLocTwo)) {
            final Vertex vertex = vertices.get(vertexLocTwo);
            if(vertex != null && vertex.hasBuilding() && vertex.getPlayerIndex() == playerIndex) {
                return true;
            } else if(vertex != null && !vertex.hasBuilding()) {
                return true;
            }
        }
        return false;
    }

    private HashSet<Integer> getPlayers(int playerIndex, HexLocation hexLoc) {
        assert hexLoc != null;

        final HashSet<Integer> players = new HashSet<>();
        getPlayers(playerIndex, players, hexLoc, VertexDirection.NorthWest);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.NorthEast);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.East);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.SouthEast);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.SouthWest);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.West);
        return players;
    }

    private void getPlayers(int playerIndex, HashSet<Integer> players, HexLocation hexLoc, VertexDirection vertexDir) {
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = vertices.get(vertexLoc);
        if(vertex.hasBuilding() && vertex.getPlayerIndex() != playerIndex) {
            players.add(vertex.getPlayerIndex());
        }
    }

    private void refreshRoads(ArrayList<Edge> roads) {
        assert roads != null;
        for(Edge edge: roads) {
            edge.getRoad().setVisited(false);
        }
    }

    private int getLongestRoadSize(int size, int playerIndex, EdgeLocation edgeLoc, ArrayList<EdgeLocation> oldConnectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert oldConnectingRoads != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        final Edge edge = edges.get(edgeLoc);
        if(edge.getRoad().isVisited()) {
            return size;
        } else {
            edge.getRoad().setVisited(true);
            size++;
            final ArrayList<EdgeLocation> connectingRoads = getConnectingRoads(playerIndex, edgeLoc);
            int oldSize = size;
            for(EdgeLocation road : connectingRoads) {
                if(!oldConnectedToNew(oldConnectingRoads, road)) {
                    int newSize = getLongestRoadSize(oldSize, playerIndex, road, connectingRoads);
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

    private ArrayList<EdgeLocation> getConnectingRoads(int playerIndex, EdgeLocation edgeLoc) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        ArrayList<EdgeLocation> connectingRoads = new ArrayList<>();
        connectingRoads = getConnectingRoadsForEdge(playerIndex, edgeLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdge(int playerIndex, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;
        assert connectingRoads != null;

        edgeLoc = edgeLoc.getNormalizedLocation();
        if(edgeLoc.getDir() == EdgeDirection.NorthWest) {
            connectingRoads = getConnectingRoadsForEdgeNorthWest(playerIndex, edgeLoc, connectingRoads);
        } else if(edgeLoc.getDir() == EdgeDirection.North) {
            connectingRoads = getConnectingRoadsForEdgeNorth(playerIndex, edgeLoc, connectingRoads);
        } else {
            connectingRoads = getConnectingRoadsForEdgeNorthEast(playerIndex, edgeLoc, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorthWest(int playerIndex, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;

        final HexLocation lowerLeftHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest);
        final VertexLocation lowerLeftVertexLoc = new VertexLocation(lowerLeftHexLoc, VertexDirection.NorthEast);
        final VertexLocation upperRightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        connectingRoads = getConnectingRoadsForEdge(playerIndex, lowerLeftVertexLoc, upperRightVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorth(int playerIndex, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;

        final VertexLocation leftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthWest);
        final VertexLocation rightVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        connectingRoads = getConnectingRoadsForEdge(playerIndex, leftVertexLoc, rightVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdgeNorthEast(int playerIndex, EdgeLocation edgeLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert edgeLoc != null;

        final HexLocation lowerRightHexLoc = edgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast);
        final VertexLocation lowerRightVertexLoc = new VertexLocation(lowerRightHexLoc, VertexDirection.NorthWest);
        final VertexLocation upperLeftVertexLoc = new VertexLocation(edgeLoc.getHexLoc(), VertexDirection.NorthEast);
        connectingRoads =  getConnectingRoadsForEdge(playerIndex, lowerRightVertexLoc, upperLeftVertexLoc, connectingRoads);
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForEdge(int playerIndex, VertexLocation vertexLocOne, VertexLocation vertexLocTwo, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLocOne != null;
        assert vertexLocTwo != null;
        assert connectingRoads != null;
        assert !vertexLocOne.equals(vertexLocTwo);

        Vertex vertex = vertices.get(vertexLocOne);
        if(vertex != null && vertex.hasBuilding() && vertex.getPlayerIndex() == playerIndex) {
            connectingRoads = getConnectingRoadsForVertex(playerIndex, vertexLocOne, connectingRoads);
        } else if(vertex != null && !vertex.hasBuilding()) {
            connectingRoads = getConnectingRoadsForVertex(playerIndex, vertexLocOne, connectingRoads);
        }
        vertex = vertices.get(vertexLocTwo);
        if(vertex != null && vertex.hasBuilding() && vertex.getPlayerIndex() == playerIndex) {
            connectingRoads = getConnectingRoadsForVertex(playerIndex, vertexLocTwo, connectingRoads);
        } else if(vertex != null && !vertex.hasBuilding()) {
            connectingRoads = getConnectingRoadsForVertex(playerIndex, vertexLocTwo, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertex(int playerIndex, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        vertexLoc = vertexLoc.getNormalizedLocation();
        if(vertexLoc.getDir() == VertexDirection.NorthWest) {
            connectingRoads = getConnectingRoadsForVertexNorthWest(playerIndex, vertexLoc, connectingRoads);
        } else {
            connectingRoads = getConnectingRoadsForVertexNorthEast(playerIndex, vertexLoc, connectingRoads);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertexNorthWest(int playerIndex, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        final HexLocation upperLeftHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
        final EdgeLocation upperLeftEdgeLoc = new EdgeLocation(upperLeftHexLoc, EdgeDirection.NorthEast);
        final Edge upperLeft = edges.get(upperLeftEdgeLoc);
        if(upperLeft != null && upperLeft.hasRoad() && upperLeft.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(upperLeftEdgeLoc);
        }
        final EdgeLocation lowerLeftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthWest);
        final Edge lowerLeft = edges.get(lowerLeftEdgeLoc);
        if(lowerLeft != null && lowerLeft.hasRoad() && lowerLeft.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(lowerLeftEdgeLoc);
        }
        final EdgeLocation rightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge right = edges.get(rightEdgeLoc);
        if(right != null && right.hasRoad() && right.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(rightEdgeLoc);
        }
        return connectingRoads;
    }

    private ArrayList<EdgeLocation> getConnectingRoadsForVertexNorthEast(int playerIndex, VertexLocation vertexLoc, ArrayList<EdgeLocation> connectingRoads) {
        assert playerIndex >= 0 && playerIndex <= 3;
        assert vertexLoc != null;
        assert connectingRoads != null;

        final HexLocation upperRightHexLoc = vertexLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
        final EdgeLocation upperRightEdgeLoc = new EdgeLocation(upperRightHexLoc, EdgeDirection.NorthWest);
        final Edge upperRight = edges.get(upperRightEdgeLoc);
        if(upperRight != null && upperRight.hasRoad() && upperRight.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(upperRightEdgeLoc);
        }
        final EdgeLocation lowerRightEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.NorthEast);
        final Edge lowerRight = edges.get(lowerRightEdgeLoc);
        if(lowerRight != null && lowerRight.hasRoad() && lowerRight.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(lowerRightEdgeLoc);
        }
        final EdgeLocation leftEdgeLoc = new EdgeLocation(vertexLoc.getHexLoc(), EdgeDirection.North);
        final Edge left = edges.get(leftEdgeLoc);
        if(left != null && left.hasRoad() && left.getRoad().getPlayerIndex() == playerIndex) {
            connectingRoads.add(leftEdgeLoc);
        }
        return connectingRoads;
    }

    /*===========================================
                   Getter Methods
     ============================================*/

    public java.util.Map<HexLocation, Hex> getHexes() {
        return hexes;
    }

    public java.util.Map<Integer, ArrayList<HexLocation>> getChits() {
        return chits;
    }

    public java.util.Map<VertexLocation, Vertex> getVertices() {
        return vertices;
    }

    public java.util.Map<Integer, ArrayList<Edge>> getRoads() {
        return roads;
    }

    public java.util.Map<Integer, ArrayList<Vertex>> getSettlements() {
        return settlements;
    }

    public java.util.Map<Integer, ArrayList<Vertex>> getCities() {
        return cities;
    }

    public Robber getRobber() {
        return robber;
    }
}
