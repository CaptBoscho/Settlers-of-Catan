package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.services.UserCookie;
import client.base.*;
import shared.exceptions.PlayerExistsException;
import shared.model.game.TurnTracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	private Facade facade;
    private UserCookie userCookie;
    boolean joining;

	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
		facade = Facade.getInstance();
        facade.addObserver(this);
        userCookie = UserCookie.getInstance();
        joining = true;
	}
	
	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
        facade.finishTurn(userCookie.getPlayerIndex());
	}
	
	public void initFromModel() {
        try {
            getView().setLocalPlayerColor(facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
            List<PlayerInfo> players = facade.getPlayers();
            if(players.size() == 4) {
                for (PlayerInfo player : players) {
                    if (joining) {
                        getView().initializePlayer(player.getPlayerIndex(), player.getName(), player.getColor());
                    } else {
                        getView().updatePlayer(player.getPlayerIndex(), player.getName(), player.getVictoryPoints(), player.getPlayerIndex() == facade.getCurrentTurn(), player.hasLargestArmy(), player.hasLongestRoad(), player.getColor());
                    }
                }
                joining = false;
            }
            int winner = facade.getWinnerId();
            if(winner != -1) {
                getView().updateGameState("Game Over", false);
            } else {
                TurnTracker.Phase state = facade.getPhase();
                switch (state) {
                    case SETUPONE:
                        getView().updateGameState("Setup Phase One", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    case SETUPTWO:
                        getView().updateGameState("Setup Phase Two", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    case ROLLING:
                        getView().updateGameState("Rolling", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    case ROBBING:
                        getView().updateGameState("Robbing", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    case PLAYING:
                        getView().updateGameState("Playing", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    case DISCARDING:
                        getView().updateGameState("Discarding", facade.getCurrentTurn() == userCookie.getPlayerIndex());
                        break;
                    default:
                        break;
                }
            }
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		initFromModel();
	}
}
