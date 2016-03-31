package shared.model.ai.aitypes;

import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.locations.*;
import shared.model.ai.AIPlayer;
import shared.model.ai.AIType;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Dice;
import shared.model.game.Game;
import shared.model.game.MessageLine;
import shared.model.map.Edge;
import shared.model.map.Vertex;
import shared.model.map.hex.Hex;
import shared.model.player.PlayerType;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * @author Joel Bradley
 */
public class LongestRoadAI extends AIPlayer {

    private Game game;
    private ArrayList<HexLocation> roadHexes;
    private ArrayList<VertexLocation> roadVertices;
    private ArrayList<EdgeLocation> roadEdges;
    private boolean isTrading;

    public LongestRoadAI(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name, type);
        super.setPlayerType(PlayerType.AI);
    }

    private void setRoadLocations() {
        roadHexes = new ArrayList<>();
        roadVertices = new ArrayList<>();
        roadEdges = new ArrayList<>();
        java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
        for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
            if(entry.getValue().getType() == HexType.WOOD || entry.getValue().getType() == HexType.BRICK) {
                roadHexes.add(entry.getKey());
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.NorthWest));
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.NorthEast));
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.East));
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.SouthEast));
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.SouthWest));
                roadVertices.add(new VertexLocation(entry.getKey(), VertexDirection.West));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.NorthWest));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.North));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.NorthEast));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.SouthEast));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.South));
                roadEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.SouthWest));
            }
        }
    }

    private void initiateSettlement() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        //TODO: this could potentially have a bug if all the sheep vertices are taken. fix this later
        for(VertexLocation vertex : roadVertices) {
            if(game.canInitiateSettlement(getPlayerIndex(), vertex)) {
                game.initiateSettlement(getPlayerIndex(), vertex);
                return;
            }
        }
    }

    private void initiateRoad() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        //TODO: this could potentially have a bug if all the sheep edges are taken. fix this later
        for(EdgeLocation edge : roadEdges) {
            if(game.canInitiateRoad(getPlayerIndex(), edge)) {
                game.initiateRoad(getPlayerIndex(), edge);
                return;
            }
        }
    }

    private void playSettlement() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(VertexLocation vertex : roadVertices) {
            if(game.canBuildSettlement(getPlayerIndex(), vertex)) {
                game.buildSettlement(getPlayerIndex(), vertex);
                return;
            }
        }
        java.util.Map<VertexLocation, Vertex> vertices = game.getMap().getVertices();
        for(java.util.Map.Entry<VertexLocation, Vertex> entry : vertices.entrySet()) {
            if(game.canBuildSettlement(getPlayerIndex(), entry.getKey())) {
                game.buildSettlement(getPlayerIndex(), entry.getKey());
                return;
            }
        }
    }

    private void playRoad() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(EdgeLocation edge : roadEdges) {
            if(game.canBuildRoad(getPlayerIndex(), edge)) {
                game.buildRoad(getPlayerIndex(), edge);
                return;
            }
        }
        java.util.Map<EdgeLocation, Edge> edges = game.getMap().getEdges();
        for(java.util.Map.Entry<EdgeLocation, Edge> entry : edges.entrySet()) {
            if(game.canBuildRoad(getPlayerIndex(), entry.getKey())) {
                game.buildRoad(getPlayerIndex(), entry.getKey());
                return;
            }
        }
    }

    private void playCity() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(VertexLocation vertex : roadVertices) {
            if(game.canBuildCity(getPlayerIndex(), vertex)) {
                game.buildCity(getPlayerIndex(), vertex);
                return;
            }
        }
        java.util.Map<VertexLocation, Vertex> vertices = game.getMap().getVertices();
        for(java.util.Map.Entry<VertexLocation, Vertex> entry : vertices.entrySet()) {
            if(game.canBuildCity(getPlayerIndex(), entry.getKey())) {
                game.buildCity(getPlayerIndex(), entry.getKey());
                return;
            }
        }
    }

    private void maritimeTrade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException, InvalidPlayerException {
        int oreRatio = 4;
        int sheepRatio = 4;
        int wheatRatio = 4;
        Set<PortType> ports = this.game.getMap().getPortTypes(getPlayerIndex());
        if(ports.contains(PortType.THREE)) {
            oreRatio = 3;
            sheepRatio = 3;
            wheatRatio = 3;
        }
        if(ports.contains(PortType.ORE)) {
            oreRatio = 2;
        }
        if(ports.contains(PortType.SHEEP)) {
            sheepRatio = 2;
        }
        if(ports.contains(PortType.WHEAT)) {
            wheatRatio = 2;
        }
        ResourceType wantedResource;
        if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= this.game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
            wantedResource = ResourceType.BRICK;
        } else {
            wantedResource = ResourceType.WOOD;
        }
        switch(new Random().nextInt(3)) {
            case 0:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.ORE) >= oreRatio && this.game.getBankResources().get(wantedResource) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), oreRatio, ResourceType.ORE, wantedResource);
                }
                break;
            case 1:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.SHEEP) >= sheepRatio && this.game.getBankResources().get(wantedResource) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), sheepRatio, ResourceType.SHEEP, wantedResource);
                }
                break;
            case 2:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WHEAT) >= wheatRatio && this.game.getBankResources().get(wantedResource) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), wheatRatio, ResourceType.WHEAT, wantedResource);
                }
                break;
            default:
                break;
        }
    }

    private void trade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        //TODO: fill in once you can get trading for sheep to work
    }

    private ArrayList<Integer> getPlayers(int playerIndex, HexLocation hexLoc) {
        final ArrayList<Integer> players = new ArrayList<>();
        getPlayers(playerIndex, players, hexLoc, VertexDirection.NorthWest);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.NorthEast);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.East);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.SouthEast);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.SouthWest);
        getPlayers(playerIndex, players, hexLoc, VertexDirection.West);
        return players;
    }

    private void getPlayers(int playerIndex, ArrayList<Integer> players, HexLocation hexLoc, VertexDirection vertexDir) {
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = this.game.getMap().getVertices().get(vertexLoc);
        if(vertex.hasBuilding()) {
            players.add(vertex.getPlayerIndex());
        }
    }

    private void sendChat() throws PlayerExistsException {
        String playerName = game.getPlayerNameByIndex(getPlayerIndex());
        String message = "I love roads";
        MessageLine line = new MessageLine(playerName, message);
        game.getChat().addMessage(line);
    }

    private void getDevCard() throws Exception {
        if(canBuyDevCard()) {
            this.game.buyDevelopmentCard(getPlayerIndex());
        }
    }

    private void playSoldier() {
        //try and place robber on sheep hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation roadHex : roadHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), roadHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(roadHex)) {
                    this.game.useSoldier(getPlayerIndex(), potentialVictims.get(0), roadHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single sheep hex.  Now it will just pick a random hex where it doesn't have a building
            java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), entry.getKey());
                if(potentialVictims.size() != 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                    //this robs a random person on a random hex where the aiPlayer doesn't have a building there
                    int victim = potentialVictims.remove(0);
                    this.game.useSoldier(getPlayerIndex(), victim, entry.getKey());
                    return;
                }
            }
            //this is the aiPlayer has a building on every hex that has a building.  Now it will just not rob anyone
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(!this.game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                    this.game.useSoldier(getPlayerIndex(), getPlayerIndex(), entry.getKey());
                    return;
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    private void playDevCard() throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException, DevCardException, InvalidLocationException, InvalidPlayerException, StructureException {
        if(canUseSoldier()) {
            playSoldier();
        }
        if(canUseMonopoly()) {
            ResourceType wantedResource;
            if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= this.game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
                wantedResource = ResourceType.BRICK;
            } else {
                wantedResource = ResourceType.WOOD;
            }
            this.game.useMonopoly(getPlayerIndex(), wantedResource);
        }
        if(canUseYearOfPlenty()) {
            this.game.useYearOfPlenty(getPlayerIndex(), ResourceType.WOOD, ResourceType.BRICK);
        }
        if(this.game.canUseRoadBuilding(getPlayerIndex())) {
            EdgeLocation firstRoad = null;
            EdgeLocation secondRoad = null;
            for(EdgeLocation edge : roadEdges) {
                if(this.game.canPlaceRoadBuildingCard(getPlayerIndex(), edge)) {
                    if(firstRoad == null && secondRoad == null) {
                        firstRoad = edge;
                    } else if(firstRoad != null && secondRoad == null) {
                        secondRoad = edge;
                        this.game.useRoadBuilder(getPlayerIndex(), firstRoad, secondRoad);
                        return;
                    }
                }
            }
            java.util.Map<EdgeLocation, Edge> edges = game.getMap().getEdges();
            for(java.util.Map.Entry<EdgeLocation, Edge> entry : edges.entrySet()) {
                if(this.game.canPlaceRoadBuildingCard(getPlayerIndex(), entry.getKey())) {
                    if(firstRoad == null && secondRoad == null) {
                        firstRoad = entry.getKey();
                    } else if(firstRoad != null && secondRoad == null) {
                        secondRoad = entry.getKey();
                        this.game.useRoadBuilder(getPlayerIndex(), firstRoad, secondRoad);
                        return;
                    }
                }
            }
        }
        if(canUseMonument()) {
            this.game.useMonument(getPlayerIndex());
        }
    }

    @Override
    public void acceptTrade(Game game) {
        //TODO: fill this in later once you get trading to work in sheep
    }

    @Override
    public void setUpOne(Game game) {
        this.game = game;
        setRoadLocations();
        try {
            initiateSettlement();
            initiateRoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpTwo(Game game) {
        this.game = game;
        setRoadLocations();
        try {
            initiateSettlement();
            initiateRoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rolling(Game game) {
        this.game = game;
        setRoadLocations();
        try {
            Dice roller = new Dice(2);
            int roll = roller.roll();
            game.rollNumber(roll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void discarding(Game game) {
        try {
            if (!hasDiscarded()) {
                ArrayList<ResourceType> unwanted = new ArrayList<>();
                unwanted.add(ResourceType.ORE);
                unwanted.add(ResourceType.SHEEP);
                unwanted.add(ResourceType.WHEAT);
                ArrayList<ResourceType> resources = new ArrayList<>();
                int totalToDiscard = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberResourceCards() / 2;
                int brick = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.BRICK);
                int ore = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
                int wheat = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
                int wood = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WOOD);
                int sheep = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.SHEEP);
                while(totalToDiscard != 0 && unwanted.size() != 0) {
                    ResourceType random = unwanted.remove(new Random().nextInt(unwanted.size()));
                    switch(random) {
                        case ORE:
                            while(ore != 0) {
                                resources.add(random);
                                ore--;
                                totalToDiscard--;
                            }
                            break;
                        case SHEEP:
                            while(sheep != 0) {
                                resources.add(random);
                                sheep--;
                                totalToDiscard--;
                            }
                            break;
                        case WHEAT:
                            while(wheat != 0) {
                                resources.add(random);
                                wheat--;
                                totalToDiscard--;
                            }
                            break;
                        default:
                            break;
                    }
                }
                while(wood != 0 && brick != 0 && totalToDiscard != 0) {
                    if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= this.game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
                        resources.add(ResourceType.WOOD);
                        wood--;
                        totalToDiscard--;
                    } else {
                        resources.add(ResourceType.BRICK);
                        brick--;
                        totalToDiscard--;
                    }
                }
                this.game.discardCards(getPlayerIndex(), resources);
                this.game.incrementVersion();
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void robbing(Game game) {
        this.game = game;
        setRoadLocations();
        //try and place robber on sheep hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation roadHex : roadHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), roadHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(roadHex)) {
                    this.game.rob(getPlayerIndex(), potentialVictims.get(0), roadHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single sheep hex.  Now it will just pick a random hex where it doesn't have a building
            java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), entry.getKey());
                if(potentialVictims.size() != 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                    //this robs a random person on a random hex where the aiPlayer doesn't have a building there
                    int victim = potentialVictims.remove(0);
                    this.game.rob(getPlayerIndex(), victim, entry.getKey());
                    return;
                }
            }
            //this is if the aiPlayer has a building on every hex that has a building.  Now it will just not rob anyone
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(!this.game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                    this.game.rob(getPlayerIndex(), getPlayerIndex(), entry.getKey());
                    return;
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playing(Game game) {
        this.game = game;
        setRoadLocations();
        try {
            if(!isTrading) {
                playRoad();
                getDevCard();
                playDevCard();
                maritimeTrade();
                playSettlement();
                playCity();
                //trade();
                isTrading = true;
                sendChat();
            }
        } catch (Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTrading(boolean isTrading) {
        this.isTrading = isTrading;
    }

    @Override
    public boolean isTrading() {
        return isTrading;
    }
}
