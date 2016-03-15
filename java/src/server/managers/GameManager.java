package server.managers;

import server.exceptions.*;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.game.Game;

import java.util.List;

/**
 * This class maintains multiple games. Anytime any game-specific info or action is required, the game
 * is looked up so that the server can maintain multiple games.
 * @author Derek Argueta
 */
public class GameManager {

    private List<Game> games;

    //TODO: fix this later fool
    private Game game;

    private HexLocation getModelHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()+hexLoc.getX());
    }

    private EdgeLocation getModelEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getModelHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    private VertexLocation getModelVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getModelHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
    }

    public void buildSettlement(int playerIndex, VertexLocation location) throws BuildSettlementException {
        location = getModelVertexLocation(location);
        try {
            if(game.canInitiateSettlement(playerIndex, location)) {
                game.initiateSettlement(playerIndex, location);
            } else if(game.canBuildSettlement(playerIndex, location)) {
                game.buildSettlement(playerIndex, location);
            }
        } catch (InvalidLocationException | InvalidPlayerException | StructureException | PlayerExistsException e) {
            throw new BuildSettlementException(e.getMessage());
        }
    }

    public void buildRoad(int playerIndex, EdgeLocation location) throws BuildRoadException {
        location = getModelEdgeLocation(location);
        try {
            if(game.canInitiateRoad(playerIndex, location)) {
                game.initiateRoad(playerIndex, location);
            } else if(game.canBuildRoad(playerIndex, location)) {
                game.buildRoad(playerIndex, location);
            }
        } catch (InvalidLocationException | InvalidPlayerException | StructureException | PlayerExistsException e) {
            throw new BuildRoadException(e.getMessage());
        }
    }

    public void buildCity(int playerIndex, VertexLocation location) throws BuildCityException {
        location = getModelVertexLocation(location);
        try {
            if(game.canBuildCity(playerIndex, location)) {
                game.buildCity(playerIndex, location);
            }
        } catch (InvalidLocationException | InvalidPlayerException | StructureException | PlayerExistsException e) {
            throw new BuildCityException(e.getMessage());
        }
    }
}
