package shared.model.ai.aitypes;

import com.google.gson.JsonObject;
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
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.map.Edge;
import shared.model.map.Vertex;
import shared.model.map.hex.Hex;
import shared.model.player.PlayerType;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;
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

    public LongestRoadAI(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name, type);
        super.setPlayerType(PlayerType.AI);
    }

    public LongestRoadAI(JsonObject blob) {
        super(blob);
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

    private ArrayList<Integer> getPlayers(HexLocation hexLoc) {
        final ArrayList<Integer> players = new ArrayList<>();
        getPlayers(players, hexLoc, VertexDirection.NorthWest);
        getPlayers(players, hexLoc, VertexDirection.NorthEast);
        getPlayers(players, hexLoc, VertexDirection.East);
        getPlayers(players, hexLoc, VertexDirection.SouthEast);
        getPlayers(players, hexLoc, VertexDirection.SouthWest);
        getPlayers(players, hexLoc, VertexDirection.West);
        return players;
    }

    private void getPlayers(ArrayList<Integer> players, HexLocation hexLoc, VertexDirection vertexDir) {
        final VertexLocation vertexLoc = new VertexLocation(hexLoc, vertexDir).getNormalizedLocation();
        final Vertex vertex = game.getMap().getVertices().get(vertexLoc);
        if(vertex.hasBuilding()) {
            players.add(vertex.getPlayerIndex());
        }
    }

    private void initiateSettlement() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(VertexLocation vertex : roadVertices) {
            if(game.canInitiateSettlement(getPlayerIndex(), vertex)) {
                game.initiateSettlement(getPlayerIndex(), vertex);
                return;
            }
        }
    }

    private void initiateRoad() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
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

    private void sendChat() throws PlayerExistsException {
        if(game.getPlayerWithLongestRoad() == getPlayerIndex()) {
            String playerName = game.getPlayerNameByIndex(getPlayerIndex());
            String message = "I got the Longest Road, losers";
            MessageLine line = new MessageLine(playerName, message);
            game.getChat().addMessage(line);
        }
    }

    private void getDevCard() throws Exception {
        if(game.canBuyDevelopmentCard(getPlayerIndex())) {
            game.buyDevelopmentCard(getPlayerIndex());
        }
    }

    private void playSoldier() {
        //try and place robber on road hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation roadHex : roadHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(roadHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(roadHex)) {
                    game.useSoldier(getPlayerIndex(), potentialVictims.get(0), roadHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single road hex.  Now it will just pick a random hex where it doesn't have a building
            java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(entry.getValue().getType() != HexType.WATER) {
                    ArrayList<Integer> potentialVictims = getPlayers(entry.getKey());
                    if (potentialVictims.size() != 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                        //this robs a random person on a random hex where the aiPlayer doesn't have a building there
                        int victim = potentialVictims.remove(0);
                        game.useSoldier(getPlayerIndex(), victim, entry.getKey());
                        return;
                    }
                }
            }
            //this is the aiPlayer has a building on every hex that has a building.  Now it will just not rob anyone
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(entry.getValue().getType() != HexType.WATER) {
                    if (!game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                        game.useSoldier(getPlayerIndex(), getPlayerIndex(), entry.getKey());
                        return;
                    }
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    private void playMonopoly() throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException, DevCardException {
        ResourceType wantedResource;
        if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
            wantedResource = ResourceType.BRICK;
        } else {
            wantedResource = ResourceType.WOOD;
        }
        game.useMonopoly(getPlayerIndex(), wantedResource);
    }

    private void playYearOfPlenty() throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException, DevCardException {
        game.useYearOfPlenty(getPlayerIndex(), ResourceType.WOOD, ResourceType.BRICK);
    }

    private void playRoadBuilder() throws InvalidPlayerException, PlayerExistsException, InvalidLocationException, StructureException, DevCardException {
        EdgeLocation firstRoad = null;
        EdgeLocation secondRoad = null;
        for(EdgeLocation edge : roadEdges) {
            if(game.canPlaceRoadBuildingCard(getPlayerIndex(), edge)) {
                if(firstRoad == null && secondRoad == null) {
                    firstRoad = edge;
                } else if(firstRoad != null && secondRoad == null) {
                    secondRoad = edge;
                    game.useRoadBuilder(getPlayerIndex(), firstRoad, secondRoad);
                    return;
                }
            }
        }
        java.util.Map<EdgeLocation, Edge> edges = game.getMap().getEdges();
        for(java.util.Map.Entry<EdgeLocation, Edge> entry : edges.entrySet()) {
            if(game.canPlaceRoadBuildingCard(getPlayerIndex(), entry.getKey())) {
                if(firstRoad == null && secondRoad == null) {
                    firstRoad = entry.getKey();
                } else if(firstRoad != null && secondRoad == null) {
                    secondRoad = entry.getKey();
                    game.useRoadBuilder(getPlayerIndex(), firstRoad, secondRoad);
                    return;
                }
            }
        }
    }

    private void playMonument() throws PlayerExistsException, DevCardException {
        game.useMonument(getPlayerIndex());
    }

    private void playDevCard() throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException, DevCardException, InvalidLocationException, InvalidPlayerException, StructureException {
        if(game.canUseSoldier(getPlayerIndex())) {
            playSoldier();
        }
        if(game.canUseMonopoly(getPlayerIndex())) {
            playMonopoly();
        }
        if(game.canUseYearOfPlenty(getPlayerIndex())) {
            playYearOfPlenty();
        }
        if(game.canUseRoadBuilding(getPlayerIndex())) {
            playRoadBuilder();
        }
        if(game.canUseMonument(getPlayerIndex())) {
            playMonument();
        }
    }

    private void maritimeTrade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException, InvalidPlayerException {
        int oreRatio = 4;
        int sheepRatio = 4;
        int wheatRatio = 4;
        Set<PortType> ports = game.getMap().getPortTypes(getPlayerIndex());
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
        if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
            wantedResource = ResourceType.BRICK;
        } else {
            wantedResource = ResourceType.WOOD;
        }
        switch(new Random().nextInt(3)) {
            case 0:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.ORE) >= oreRatio && game.getBankResources().get(wantedResource) > 0) {
                    game.maritimeTrade(getPlayerIndex(), oreRatio, ResourceType.ORE, wantedResource);
                }
                break;
            case 1:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.SHEEP) >= sheepRatio && game.getBankResources().get(wantedResource) > 0) {
                    game.maritimeTrade(getPlayerIndex(), sheepRatio, ResourceType.SHEEP, wantedResource);
                }
                break;
            case 2:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WHEAT) >= wheatRatio && game.getBankResources().get(wantedResource) > 0) {
                    game.maritimeTrade(getPlayerIndex(), wheatRatio, ResourceType.WHEAT, wantedResource);
                }
                break;
            default:
                break;
        }
    }

    private void trade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        ArrayList<ResourceType> offers = new ArrayList<>();
        int sheep = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.SHEEP);
        int ore = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
        int wheat = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
        if(sheep != 0) {
            offers.add(ResourceType.SHEEP);
        }
        if(ore != 0) {
            offers.add(ResourceType.ORE);
        }
        if(wheat != 0) {
            offers.add(ResourceType.WHEAT);
        }
        if(offers.size() == 0) {
            return;
        }
        ArrayList<ResourceType> offer = new ArrayList<>();
        offer.add(offers.get(new Random().nextInt(offers.size())));
        TradePackage one = new TradePackage(getPlayerIndex(), offer);
        int otherPlayer = new Random().nextInt(4);
        while(otherPlayer == getPlayerIndex()) {
            otherPlayer = new Random().nextInt(4);
        }
        ArrayList<ResourceType> wanted = new ArrayList<>();

        if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK)) {
            wanted.add(ResourceType.BRICK);
        } else {
            wanted.add(ResourceType.WOOD);
        }
        TradePackage two = new TradePackage(otherPlayer, wanted);
        game.offerTrade(one, two);
    }

    @Override
    public void acceptTrade() {
        try {
            if (game.isTradeActive()) {
                Trade currentOffer = game.getCurrentOffer();
                if ((currentOffer.getPackage1().getResources().contains(ResourceType.WOOD) ||
                        currentOffer.getPackage1().getResources().contains(ResourceType.BRICK)) &&
                        (!currentOffer.getPackage2().getResources().contains(ResourceType.WOOD) ||
                                !currentOffer.getPackage1().getResources().contains(ResourceType.BRICK))) {
                    List<ResourceType> sending = currentOffer.getPackage2().getResources();
                    final int ownedOre = getNumberOfType(ResourceType.ORE);
                    final int ownedSheep = getNumberOfType(ResourceType.SHEEP);
                    final int ownedWheat = getNumberOfType(ResourceType.WHEAT);
                    int oreAsked = 0;
                    int sheepAsked = 0;
                    int wheatAsked = 0;
                    for(ResourceType resource : sending) {
                        switch(resource) {
                            case ORE:
                                oreAsked++;
                                if(oreAsked > ownedOre) {
                                    game.acceptTrade(getPlayerIndex(), false);
                                    return;
                                }
                                break;
                            case SHEEP:
                                sheepAsked++;
                                if(sheepAsked > ownedSheep) {
                                    game.acceptTrade(getPlayerIndex(), false);
                                    return;
                                }
                                break;
                            case WHEAT:
                                wheatAsked++;
                                if(wheatAsked > ownedWheat) {
                                    game.acceptTrade(getPlayerIndex(), false);
                                    return;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    game.acceptTrade(getPlayerIndex(), true);
                } else {
                    game.acceptTrade(getPlayerIndex(), false);
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpOne() {
        try {
            initiateSettlement();
            initiateRoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpTwo() {
        try {
            initiateSettlement();
            initiateRoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void roll() {
        try {
            Dice roller = new Dice(2);
            int roll = roller.roll();
            game.rollNumber(roll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void discard() {
        try {
            if (!hasDiscarded()) {
                ArrayList<ResourceType> unwanted = new ArrayList<>();
                unwanted.add(ResourceType.ORE);
                unwanted.add(ResourceType.SHEEP);
                unwanted.add(ResourceType.WHEAT);
                ArrayList<ResourceType> resources = new ArrayList<>();
                int totalToDiscard = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberResourceCards() / 2;
                int ore = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
                int wheat = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
                int sheep = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.SHEEP);
                while(totalToDiscard != 0 && unwanted.size() != 0) {
                    ResourceType random = unwanted.remove(new Random().nextInt(unwanted.size()));
                    switch(random) {
                        case ORE:
                            while(ore != 0 && totalToDiscard != 0) {
                                resources.add(random);
                                ore--;
                                totalToDiscard--;
                            }
                            break;
                        case SHEEP:
                            while(sheep != 0 && totalToDiscard != 0) {
                                resources.add(random);
                                sheep--;
                                totalToDiscard--;
                            }
                            break;
                        case WHEAT:
                            while(wheat != 0 && totalToDiscard != 0) {
                                resources.add(random);
                                wheat--;
                                totalToDiscard--;
                            }
                            break;
                        default:
                            break;
                    }
                }
                int wood = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WOOD);
                int brick = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.BRICK);
                while(totalToDiscard != 0) {
                    if(wood >= brick) {
                        resources.add(ResourceType.WOOD);
                        wood--;
                        totalToDiscard--;
                    } else {
                        resources.add(ResourceType.BRICK);
                        brick--;
                        totalToDiscard--;
                    }
                }
                game.discardCards(getPlayerIndex(), resources);
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rob() {
        //try and place robber on road hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation roadHex : roadHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(roadHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(roadHex)) {
                    game.rob(getPlayerIndex(), potentialVictims.get(0), roadHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single road hex.  Now it will just pick a random hex where it doesn't have a building
            java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(entry.getValue().getType() != HexType.WATER) {
                    ArrayList<Integer> potentialVictims = getPlayers(entry.getKey());
                    if (potentialVictims.size() != 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                        //this robs a random person on a random hex where the aiPlayer doesn't have a building there
                        int victim = potentialVictims.remove(0);
                        game.rob(getPlayerIndex(), victim, entry.getKey());
                        return;
                    }
                }
            }
            //this is if the aiPlayer has a building on every hex that has a building.  Now it will just not rob anyone
            for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
                if(entry.getValue().getType() != HexType.WATER) {
                    if (!game.getMap().getRobber().getLocation().equals(entry.getKey())) {
                        game.rob(getPlayerIndex(), getPlayerIndex(), entry.getKey());
                        return;
                    }
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        try {
            playRoad();
            ArrayList<Integer> choices = new ArrayList<>();
            for(int i=0; i<5; i++) {
                choices.add(i);
            }
            while(choices.size() > 0) {
                int random = choices.remove(new Random().nextInt(choices.size()));
                switch(random) {
                    case 0:
                        playSettlement();
                        break;
                    case 1:
                        playCity();
                        break;
                    case 2:
                        getDevCard();
                        break;
                    case 3:
                        playDevCard();
                        break;
                    case 4:
                        maritimeTrade();
                        break;
                    default:
                        break;
                }
            }
            sendChat();
            trade();
        } catch (Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        setRoadLocations();
    }
}
