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
    private boolean isTrading;

    public SheepAI(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name, type);
        super.setPlayerType(PlayerType.AI);
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

    private void initiateSettlement() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        //TODO: this could potentially have a bug if all the sheep vertices are taken. fix this later
        for(VertexLocation vertex : sheepVertices) {
            if(game.canInitiateSettlement(getPlayerIndex(), vertex)) {
                game.initiateSettlement(getPlayerIndex(), vertex);
                return;
            }
        }
    }

    private void initiateRoad() throws InvalidLocationException, InvalidPlayerException, PlayerExistsException, StructureException {
        //TODO: this could potentially have a bug if all the sheep edges are taken. fix this later
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

    private void maritimeTrade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException, InvalidPlayerException {
        int oreRatio = 4;
        int brickRatio = 4;
        int wheatRatio = 4;
        int woodRatio = 4;
        Set<PortType> ports = this.game.getMap().getPortTypes(getPlayerIndex());
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
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.ORE) >= oreRatio && this.game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), oreRatio, ResourceType.ORE, ResourceType.SHEEP);
                }
                break;
            case 1:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.BRICK) >= brickRatio && this.game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), brickRatio, ResourceType.BRICK, ResourceType.SHEEP);
                }
                break;
            case 2:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WHEAT) >= wheatRatio && this.game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), wheatRatio, ResourceType.WHEAT, ResourceType.SHEEP);
                }
                break;
            case 3:
                if(this.game.amountOwnedResource(getPlayerIndex(), ResourceType.WOOD) >= woodRatio && this.game.getBankResources().get(ResourceType.SHEEP) > 0) {
                    this.game.maritimeTrade(getPlayerIndex(), woodRatio, ResourceType.WOOD, ResourceType.SHEEP);
                }
                break;
            default:
                break;
        }
    }

    private void trade() throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        ArrayList<ResourceType> offers = new ArrayList<>();
        int brick = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.BRICK);
        int ore = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.ORE);
        int wheat = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WHEAT);
        int wood = this.game.getPlayerManager().getPlayerByIndex(getPlayerIndex()).getNumberOfType(ResourceType.WOOD);
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
        ArrayList<ResourceType> offeree = new ArrayList<>();
        offeree.add(ResourceType.SHEEP);
        TradePackage two = new TradePackage(otherPlayer, offeree);
        this.game.offerTrade(one, two);
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
        String message = "I love sheep";
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
            for (HexLocation sheepHex : sheepHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), sheepHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(sheepHex)) {
                    this.game.useSoldier(getPlayerIndex(), potentialVictims.get(0), sheepHex);
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
            this.game.useMonopoly(getPlayerIndex(), ResourceType.SHEEP);
        }
        if(canUseYearOfPlenty()) {
            this.game.useYearOfPlenty(getPlayerIndex(), ResourceType.SHEEP, ResourceType.SHEEP);
        }
        if(this.game.canUseRoadBuilding(getPlayerIndex())) {
            EdgeLocation firstRoad = null;
            EdgeLocation secondRoad = null;
            for(EdgeLocation edge : sheepEdges) {
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
        this.game = game;
        try {
            if (this.game.isTradeActive()) {
                Trade currentOffer = this.game.getCurrentOffer();
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
                                    this.game.acceptTrade(getPlayerIndex(), false);
                                }
                                break;
                            case BRICK:
                                brickAsked++;
                                if(brickAsked > ownedBrick) {
                                    this.game.acceptTrade(getPlayerIndex(), false);
                                }
                                break;
                            case WHEAT:
                                wheatAsked++;
                                if(wheatAsked > ownedWheat) {
                                    this.game.acceptTrade(getPlayerIndex(), false);
                                }
                                break;
                            case WOOD:
                                woodAsked++;
                                if(woodAsked > ownedWood) {
                                    this.game.acceptTrade(getPlayerIndex(), false);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    this.game.acceptTrade(getPlayerIndex(), true);
                } else {
                    this.game.acceptTrade(getPlayerIndex(), false);
                }
            }
        } catch(Exception | InvalidTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpOne(Game game) {
        this.game = game;
        setSheepLocations();
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
        setSheepLocations();
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
        setSheepLocations();
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
                unwanted.add(ResourceType.BRICK);
                unwanted.add(ResourceType.ORE);
                unwanted.add(ResourceType.WOOD);
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
                        case WOOD:
                            while(wood != 0) {
                                resources.add(random);
                                wood--;
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
                        case BRICK:
                            while(brick != 0) {
                                resources.add(random);
                                brick--;
                                totalToDiscard--;
                            }
                            break;
                        default:
                            break;
                    }
                }
                while(sheep != 0 && totalToDiscard != 0) {
                    resources.add(ResourceType.SHEEP);
                    sheep--;
                    totalToDiscard--;
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
        setSheepLocations();
        //try and place robber on sheep hex where aiPlayer doesn't have a building itself
        try {
            for (HexLocation sheepHex : sheepHexes) {
                ArrayList<Integer> potentialVictims = getPlayers(getPlayerIndex(), sheepHex);
                if (potentialVictims.size() > 0 && !potentialVictims.contains(getPlayerIndex()) && !this.game.getMap().getRobber().getLocation().equals(sheepHex)) {
                    this.game.rob(getPlayerIndex(), potentialVictims.get(0), sheepHex);
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
        setSheepLocations();
        try {
            if(!isTrading) {
                playRoad();
                playSettlement();
                playCity();
                getDevCard();
                playDevCard();
                maritimeTrade();
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
