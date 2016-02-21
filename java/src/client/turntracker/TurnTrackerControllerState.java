package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.facade.ModelPlayerInfo;
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
    private List<PlayerInfo> players;

    /**
     * Constructor
     */
    public TurnTrackerControllerState(ITurnTrackerView view){
        this.view = view;
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
        players = facade.getPlayers();
    }

    public void endTurn() {
        int id = userCookie.getPlayerId();
        boolean canFinishTurn = facade.canFinishTurn(id);
        if(canFinishTurn){
            facade.finishTurn(id);
        }
    }

    public void initFromModel(){}

    public void update() {
        //Update Controller
        for(PlayerInfo playerInfo : players){
            view.updatePlayer(playerInfo.getPlayerIndex(), playerInfo.getVictoryPoints(),
                    facade.getCurrentTurn() == playerInfo.getPlayerIndex(),
                    playerInfo.hasLargestArmy(),
                    playerInfo.hasLongestRoad());
        }
    }
}
