package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.services.UserCookie;
import com.sun.jna.platform.win32.Netapi32Util;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import java.util.List;
import java.util.Observable;

/**
 * Created by corne on 2/16/2016.
 *
 * Base class for TurnTracker Controller States
 */
public class TurnTrackerControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;
    private PlayerManager pManager;

    /**
     * Constructor
     */
    public TurnTrackerControllerState(ITurnTrackerView view){
        view = view;
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
        pManager = facade.getPlayerManager();
    }

    public void endTurn() {
        int id = userCookie.getPlayerId();
        boolean canFinishTurn = facade.canFinishTurn(id);
        if(canFinishTurn){
            facade.finishTurn(id);
        }
    }

    public void initFromModel() {
        //Set the local player color
        try {
            int id = userCookie.getPlayerId();
            CatanColor locPColor = facade.getPlayerColorByID(id);
            view.setLocalPlayerColor(locPColor);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }

        //Init Players // TODO: 2/18/2016 Change to match the way Derek changed things 
        List<Player> players = pManager.getPlayers();
        for(Player player : players){
            view.initializePlayer(player.getPlayerIndex(), player.getName().toString(), player.getColor());
        }
    }

    public void update(Game game) {
        List<Player> players = game.getPlayerManager().getPlayers();
        for(Player player : players){
            view.updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(),
                    game.getCurrentTurn() == player.getId(),
                    game.currentLargestArmyPlayer() == player.getId(),
                    game.currentLongestRoadPlayer() == player.getId());
        }
    }
}
