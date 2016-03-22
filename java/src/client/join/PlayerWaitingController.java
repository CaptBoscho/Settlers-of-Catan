package client.join;

import client.base.*;
import client.data.PlayerInfo;
import client.facade.Facade;
import client.misc.MessageView;
import client.services.MissingUserCookieException;
import client.services.Poller;
import client.services.ServerProxy;
import shared.dto.AddAIDTO;
import shared.model.ai.AIType;
import shared.model.game.Game;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
        Facade.getInstance().addObserver(this);

        // START GAME
        try {
            ServerProxy.getInstance().getCurrentModel(Facade.getInstance().getGame().getVersion());
        } catch (MissingUserCookieException e) {
            e.printStackTrace();
        }

        Poller p = Poller.getInstance();

        // show waiting screen if there are not 4 players joined
        if(Facade.getInstance().getPlayers().size() < 4) {
            PlayerInfo[] infoArr = new PlayerInfo[Facade.getInstance().getPlayers().size()];
            Facade.getInstance().getPlayers().toArray(infoArr);
            getView().setPlayers(Facade.getInstance().getPlayers().toArray(infoArr));
            List<String> availableAIs = ServerProxy.getInstance().getAITypes();
            getView().setAIChoices(availableAIs.toArray(new String[availableAIs.size()]));
            if(!getView().isModalShowing()) {
                getView().showModal();
            }
            p.setPlayerWaitingPolling();
        } else {
            p.setModelPolling();
            if(getView().isModalShowing()) {
                getView().closeModal();
            }
        }
        p.start();
	}

	@Override
	public void addAI() {
        //Build the AddAIDTO
        String aiType = getView().getSelectedAI();
        AIType ai = AIType.valueOf(aiType);
        AddAIDTO dto = new AddAIDTO(ai);

        //Add the AI to the game
        ServerProxy.getInstance().addAI(dto);
        
//        //// TODO: 3/7/16 implement add AI
//        MessageView messageView = new MessageView();
//        messageView.setTitle("Broken Button");
//        messageView.setMessage("This button doesn't actually do anything.  Wait for 4 human players to join.");
//        messageView.showModal();
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
        if(getView().isModalShowing()) {
            PlayerInfo[] infoArr = new PlayerInfo[Facade.getInstance().getPlayers().size()];
            Facade.getInstance().getPlayers().toArray(infoArr);
            getView().closeModal();
            if(infoArr.length < 4) {
                getView().setPlayers(Facade.getInstance().getPlayers().toArray(infoArr));
                getView().showModal();
            } else {
                Poller.getInstance().setModelPolling();
//                if(getView().isModalShowing())
                    getView().closeModal();
//                }
            }
        }
    }
}
