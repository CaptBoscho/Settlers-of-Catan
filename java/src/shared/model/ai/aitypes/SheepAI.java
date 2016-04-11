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
import shared.model.ai.SheepFacts;
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
public class SheepAI extends AIPlayer {

    private Game game;
    private ArrayList<HexLocation> sheepHexes;
    private ArrayList<VertexLocation> sheepVertices;
    private ArrayList<EdgeLocation> sheepEdges;
    private SheepFacts sheepFacts;

    public SheepAI(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name, type);
        super.setPlayerType(PlayerType.AI);
    }

    public SheepAI(JsonObject blob) {
        super(blob);
    }

    private void setSheepLocations() {
        sheepHexes = new ArrayList<>();
        sheepVertices = new ArrayList<>();
        sheepEdges = new ArrayList<>();
        java.util.Map<HexLocation, Hex> hexes = game.getMap().getHexes();
        for(java.util.Map.Entry<HexLocation, Hex> entry : hexes.entrySet()) {
            if(entry.getValue().getType() == HexType.SHEEP) {
                sheepHexes.add(entry.getKey());
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.NorthWest));
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.NorthEast));
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.East));
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.SouthEast));
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.SouthWest));
                sheepVertices.add(new VertexLocation(entry.getKey(), VertexDirection.West));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.NorthWest));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.North));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.NorthEast));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.SouthEast));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.South));
                sheepEdges.add(new EdgeLocation(entry.getKey(), EdgeDirection.SouthWest));
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
        for(VertexLocation vertex : sheepVertices) {
            if(game.canInitiateSettlement(getPlayerIndex(), vertex)) {
                game.initiateSettlement(getPlayerIndex(), vertex);
                return;
            }
        }
    }

    private void initiateRoad() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(EdgeLocation edge : sheepEdges) {
            if(game.canInitiateRoad(getPlayerIndex(), edge)) {
                game.initiateRoad(getPlayerIndex(), edge);
                return;
            }
        }
    }

    private void playSettlement() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        for(VertexLocation vertex : sheepVertices) {
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
        for(EdgeLocation edge : sheepEdges) {
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
        for(VertexLocation vertex : sheepVertices) {
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
        String playerName = game.getPlayerNameByIndex(getPlayerIndex());
        String message = sheepFacts.getFact();
        MessageLine line = new MessageLine(playerName, message);
        game.getChat().addMessage(line);
    }

    private void getDevCard() throws Exception {
        if(game.canBuyDevelopmentCard(getPlayerIndex())) {
            game.buyDevelopmentCard(getPlayerIndex());
        }
    }

    private void playSoldier() {
        //try and place robber on sheep hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation sheepHex : sheepHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(sheepHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(sheepHex)) {
                    game.useSoldier(getPlayerIndex(), potentialVictims.get(0), sheepHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single sheep hex.  Now it will just pick a random hex where it doesn't have a building
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
        game.useMonopoly(getPlayerIndex(), ResourceType.SHEEP);
    }

    private void playYearOfPlenty() throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException, DevCardException {
        game.useYearOfPlenty(getPlayerIndex(), ResourceType.SHEEP, ResourceType.SHEEP);
    }

    private void playRoadBuilder() throws InvalidPlayerException, PlayerExistsException, InvalidLocationException, StructureException, DevCardException {
        EdgeLocation firstRoad = null;
        EdgeLocation secondRoad = null;
        for(EdgeLocation edge : sheepEdges) {
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
        int brickRatio = 4;
        int wheatRatio = 4;
        int woodRatio = 4;
        Set<PortType> ports = game.getMap().getPortTypes(getPlayerIndex());
        if(ports.contains(PortType.THREE)) {
            oreRatio = 3;
            brickRatio = 3;
            wheatRatio = 3;
            woodRatio = 3;
        }
        if(ports.contains(PortType.ORE)) {
            oreRatio = 2;
        }
        if(ports.contains(PortType.BRICK)) {
            brickRatio = 2;
        }
        if(ports.contains(PortType.WHEAT)) {
            wheatRatio = 2;
        }
        if(ports.contains(PortType.WOOD)) {
            woodRatio = 2;
        }
        switch(new Random().nextInt(4)) {
            case 0:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.ORE) >= oreRatio && game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    game.maritimeTrade(getPlayerIndex(), oreRatio, ResourceType.ORE, ResourceType.SHEEP);
                }
                break;
            case 1:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK) >= brickRatio && game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    game.maritimeTrade(getPlayerIndex(), brickRatio, ResourceType.BRICK, ResourceType.SHEEP);
                }
                break;
            case 2:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WHEAT) >= wheatRatio && game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    game.maritimeTrade(getPlayerIndex(), wheatRatio, ResourceType.WHEAT, ResourceType.SHEEP);
                }
                break;
            case 3:
                if(game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= woodRatio && game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    game.maritimeTrade(getPlayerIndex(), woodRatio, ResourceType.WOOD, ResourceType.SHEEP);
                }
                break;
            default:
                break;
        }
    }

    private void trade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        ArrayList<ResourceType> offers = new ArrayList<>();
        int brick = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.BRICK);
        int ore = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
        int wheat = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
        int wood = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WOOD);
        if(brick != 0) {
            offers.add(ResourceType.BRICK);
        }
        if(ore != 0) {
            offers.add(ResourceType.ORE);
        }
        if(wheat != 0) {
            offers.add(ResourceType.WHEAT);
        }
        if(wood != 0) {
            offers.add(ResourceType.WOOD);
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
        wanted.add(ResourceType.SHEEP);
        TradePackage two = new TradePackage(otherPlayer, wanted);
        game.offerTrade(one, two);
    }

    @Override
    public void acceptTrade() {
        try {
            if (game.isTradeActive()) {
                Trade currentOffer = game.getCurrentOffer();
                if (currentOffer.getPackage1().getResources().contains(ResourceType.SHEEP) &&
                        !currentOffer.getPackage2().getResources().contains(ResourceType.SHEEP)) {
                    List<ResourceType> sending = currentOffer.getPackage2().getResources();
                    final int ownedOre = getNumberOfType(ResourceType.ORE);
                    final int ownedBrick = getNumberOfType(ResourceType.BRICK);
                    final int ownedWheat = getNumberOfType(ResourceType.WHEAT);
                    final int ownedWood = getNumberOfType(ResourceType.WOOD);
                    int oreAsked = 0;
                    int brickAsked = 0;
                    int wheatAsked = 0;
                    int woodAsked = 0;
                    for(ResourceType resource : sending) {
                        switch(resource) {
                            case ORE:
                                oreAsked++;
                                if(oreAsked > ownedOre) {
                                    game.acceptTrade(getPlayerIndex(), false);
                                    return;
                                }
                                break;
                            case BRICK:
                                brickAsked++;
                                if(brickAsked > ownedBrick) {
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
                            case WOOD:
                                woodAsked++;
                                if(woodAsked > ownedWood) {
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
                unwanted.add(ResourceType.BRICK);
                unwanted.add(ResourceType.ORE);
                unwanted.add(ResourceType.WOOD);
                unwanted.add(ResourceType.WHEAT);
                ArrayList<ResourceType> resources = new ArrayList<>();
                int totalToDiscard = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberResourceCards() / 2;
                int brick = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.BRICK);
                int ore = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
                int wheat = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
                int wood = game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WOOD);
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
                        case WOOD:
                            while(wood != 0 && totalToDiscard != 0) {
                                resources.add(random);
                                wood--;
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
                        case BRICK:
                            while(brick != 0 && totalToDiscard != 0) {
                                resources.add(random);
                                brick--;
                                totalToDiscard--;
                            }
                            break;
                        default:
                            break;
                    }
                }
                while(totalToDiscard != 0) {
                    resources.add(ResourceType.SHEEP);
                    totalToDiscard--;
                }
                game.discardCards(getPlayerIndex(), resources);
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rob() {
        //try and place robber on sheep hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation sheepHex : sheepHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(sheepHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !game.getMap().getRobber().getLocation().equals(sheepHex)) {
                    game.rob(getPlayerIndex(), potentialVictims.get(0), sheepHex);
                    return;
                }
            }
            //this is if the aiPlayer has a building on every single sheep hex.  Now it will just pick a random hex where it doesn't have a building
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
            ArrayList<Integer> choices = new ArrayList<>();
            for(int i=0; i<6; i++) {
                choices.add(i);
            }
            while(choices.size() > 0) {
                int random = choices.remove(new Random().nextInt(choices.size()));
                switch(random) {
                    case 0:
                        playRoad();
                        break;
                    case 1:
                        playSettlement();
                        break;
                    case 2:
                        playCity();
                        break;
                    case 3:
                        getDevCard();
                        break;
                    case 4:
                        playDevCard();
                        break;
                    case 5:
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
        setSheepLocations();
        sheepFacts = new SheepFacts();
    }
}
