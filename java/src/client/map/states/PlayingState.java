package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapComponent;
import client.services.MissingUserCookieException;
import shared.definitions.PieceType;
import shared.exceptions.InvalidLocationException;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Joel Bradley
 *
 * Represents Playing State - Default Gameplay State
 */
public class PlayingState extends MapState {

    private static PlayingState instance;

    public static PlayingState getInstance(){
        if(instance == null){
            instance = new PlayingState();
        }
        return instance;
    }

    private boolean isPlayingRoadBuildingCard;
    private EdgeLocation firstRoad;
    private HexLocation robbingLoc;

    /**
     * Constructor
     */
    private PlayingState(){
        super();
        isPlayingRoadBuildingCard = false;
        firstRoad = null;
        robbingLoc = null;
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        try {
            if(isPlayingRoadBuildingCard) {
                return facade.canUseRoadBuilder(userCookie.getPlayerIndex()) && facade.canPlaceRoadBuildingCard(userCookie.getPlayerIndex(), edgeLoc);
            }
            return facade.canBuildRoad(userCookie.getPlayerIndex(), edgeLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildSettlement(userCookie.getPlayerIndex(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildCity(userCookie.getPlayerIndex(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        hexLoc = getModelHexLocation(hexLoc);
        return facade.canMoveRobber(userCookie.getPlayerIndex(), hexLoc) && facade.canUseSoldier(userCookie.getPlayerIndex());
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        try{
            if(isPlayingRoadBuildingCard && firstRoad == null) {
                firstRoad = edgeLoc;
                facade.buildFirstRoad(userCookie.getPlayerIndex(), edgeLoc);
                mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
            } else if(isPlayingRoadBuildingCard) {
                facade.playRoadBuildingCard(userCookie.getPlayerIndex(), firstRoad, edgeLoc);
                isPlayingRoadBuildingCard = false;
                firstRoad = null;
            } else {
                facade.buildRoad(userCookie.getPlayerIndex(), edgeLoc);
            }
            mapController.getView().placeRoad(getUIEdgeLocation(edgeLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            facade.buildSettlement(userCookie.getPlayerIndex(), vertLoc);
            mapController.getView().placeSettlement(getUIVertexLocation(vertLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            facade.buildCity(userCookie.getPlayerIndex(), vertLoc);
            mapController.getView().placeCity(getUIVertexLocation(vertLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeRobber(HexLocation hexLoc){
        robbingLoc = hexLoc;
        hexLoc = getModelHexLocation(hexLoc);
        mapController.getView().placeRobber(robbingLoc);
        RobPlayerInfo[] rpi = facade.moveRobber(userCookie.getPlayerIndex(), hexLoc);
        if(rpi != null && rpi.length > 1) {
            mapController.getRobView().setPlayers(rpi);
            mapController.getRobView().showModal();
        } else {
            RobPlayerInfo victim = new RobPlayerInfo();
            if(rpi.length == 1) {
                victim.setPlayerIndex(rpi[0].getPlayerIndex());
            } else {
                victim.setPlayerIndex(userCookie.getPlayerIndex());
            }
            robPlayer(victim);
        }
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        if(pieceType == PieceType.ROBBER) {
            return;
        }
        try {
            mapController.getView().startDrop(pieceType, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelMove() {
        if(isPlayingRoadBuildingCard && firstRoad != null) {
            facade.deleteRoad(userCookie.getPlayerIndex(), firstRoad);
            try {
                mapController.getView().deleteRoad(getUIEdgeLocation(firstRoad), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
            } catch (PlayerExistsException e) {
                e.printStackTrace();
            }
            initFromModel();
            isPlayingRoadBuildingCard = false;
            firstRoad = null;
        }
    }

    @Override
    public void playSoldierCard(){
        try {
            mapController.getView().startDrop(PieceType.ROBBER, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playRoadBuildingCard() {
        isPlayingRoadBuildingCard = true;
        try {
            mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void robPlayer(RobPlayerInfo victim){
        facade.playSoldier(userCookie.getPlayerIndex(), robbingLoc, victim.getPlayerIndex());
        robbingLoc = null;
    }
}
