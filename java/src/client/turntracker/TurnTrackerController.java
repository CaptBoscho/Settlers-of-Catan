package client.turntracker;

import client.data.PlayerInfo;
import client.facade.Facade;
import client.facade.ModelPlayerInfo;
import client.services.Poller;
import client.services.ServerProxy;
import client.services.UserCookie;
import client.turntracker.states.*;
import client.base.*;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;
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
    private List<PlayerInfo> players;
    private TurnTrackerControllerState state;

	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
		facade = Facade.getInstance();
        facade.addObserver(this);
        userCookie = UserCookie.getInstance();
	}
	
	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		state.endTurn();
	}
	
	public void initFromModel() {
        players = facade.getPlayers();
        //Set the local player color
        try {
            int id = userCookie.getPlayerId();
            CatanColor locPColor = facade.getPlayerColorByID(id);
            getView().setLocalPlayerColor(locPColor);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }

        //Init Players
        for(PlayerInfo playerInfo : players) {
            getView().initializePlayer(playerInfo.getPlayerIndex(), playerInfo.getName(), playerInfo.getColor());
        }

        //Create the current state
        createState(facade.getPhase());

        //Call the state's init function
        state.initFromModel();

        new Poller(ServerProxy.getInstance()).start();
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
        //Update the state
        createState(facade.getPhase());
		//Call the state's update function
        state.update();
	}

    /**
     * Creates a new state to model the current game state
     * @param phase
     */
    private void createState(TurnTracker.Phase phase){
        switch (phase) {
            case SETUPONE:  state = new SetupOneState(getView());
                break;
            case SETUPTWO:  state = new SetupTwoState(getView());
                break;
            case ROLLING:  state = new RollingState(getView());
                break;
            case PLAYING:  state = new PlayingState(getView());
                break;
            case ROBBING:  state = new RobbingState(getView());
                break;
            case DISCARDING:  state = new DiscardingState(getView());
                break;
            case GAMEFINISHED:  state = new GameFinishedState(getView());
                break;
            default: state = new PlayingState(getView());
                break;
        }
    }
}
