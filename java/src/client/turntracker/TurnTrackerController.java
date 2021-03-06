package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.services.UserCookie;
import client.base.*;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.model.game.TurnTracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the turn tracker controller
 */
public final class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	private Facade facade;
    private UserCookie userCookie;
    private boolean joining;

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
            CatanColor color = facade.getPlayerColorByIndex(userCookie.getPlayerIndex());
            getView().setLocalPlayerColor(color);
            List<PlayerInfo> players = facade.getPlayers();
            if(players.size() == 4) {
                for (PlayerInfo player : players) {
                    if (joining) {
                        getView().initializePlayer(player.getPlayerIndex(), player.getName(), player.getColor());

                    }
                    getView().updatePlayer(player.getPlayerIndex(), player.getName(), player.getVictoryPoints(),
                            player.getPlayerIndex() == facade.getCurrentTurn(), player.hasLargestArmy(),
                            player.hasLongestRoad(), player.getColor());

                }
                joining = false;
            }
            int winner = facade.getWinnerId();
            if (winner != -1) {
                getView().updateGameState("Game Over", false, color.getJavaColor());
            } else {
                TurnTracker.Phase state = facade.getPhase();
                switch (state) {
                    case SETUPONE:
                        getView().updateGameState("Setup Phase One", false, color.getJavaColor());
                        break;
                    case SETUPTWO:
                        getView().updateGameState("Setup Phase Two", false, color.getJavaColor());
                        break;
                    case ROLLING:
                        getView().updateGameState("Rolling", false, color.getJavaColor());
                        break;
                    case ROBBING:
                        getView().updateGameState("Robbing", false, color.getJavaColor());
                        break;
                    case PLAYING:
                        boolean isPlaying = facade.getCurrentTurn() == userCookie.getPlayerIndex();
                        if(isPlaying) {
                            getView().updateGameState("End Turn", isPlaying, color.getJavaColor());
                        } else {
                            getView().updateGameState("Just Chill Fool", isPlaying, color.getJavaColor());
                        }
                        break;
                    case DISCARDING:
                        getView().updateGameState("Discarding", false, color.getJavaColor());
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
