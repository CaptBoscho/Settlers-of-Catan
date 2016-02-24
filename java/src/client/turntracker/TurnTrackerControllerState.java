package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.facade.ModelPlayerInfo;
import client.services.IServer;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import client.services.UserCookie;
import com.sun.jna.platform.win32.Netapi32Util;
import shared.definitions.CatanColor;
import shared.dto.FinishTurnDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import java.util.List;
import java.util.Observable;

/**
 * @author Kyle Cornelison
 *
 * Base class for TurnTracker Controller States
 */
public class TurnTrackerControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private ITurnTrackerView view;

    /**
     * Constructor
     */
    public TurnTrackerControllerState(ITurnTrackerView view){
        this.view = view;
        facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
    }

    public void endTurn() {
        //Inform the model
        int index = userCookie.getPlayerInfo().getPlayerIndex();
        facade.finishTurn(index);
    }

    public void initFromModel() {
    }

    public void update() {
        //Update Controller
        for(PlayerInfo playerInfo : facade.getPlayers()) {
            view.updatePlayer(playerInfo.getPlayerIndex(), playerInfo.getVictoryPoints(),
                    facade.getCurrentTurn() == playerInfo.getPlayerIndex(),
                    playerInfo.hasLargestArmy(), playerInfo.hasLongestRoad());
        }
    }
}
