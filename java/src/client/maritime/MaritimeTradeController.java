package client.maritime;

import client.facade.Facade;
import client.maritime.states.*;
import shared.definitions.*;
import client.base.*;
import shared.model.game.TurnTracker;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController, Observer {

	private Facade facade;
	private MaritimeTradeControllerState state;
	private IMaritimeTradeOverlay tradeOverlay;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		super(tradeView);
		setTradeOverlay(tradeOverlay);
        tradeView.enableMaritimeTrade(false);

		this.facade = Facade.getInstance();
	}
	
	public IMaritimeTradeView getTradeView() {
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		state.startTrade();
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {
		state.makeTrade();
		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {
		state.cancelTrade();
		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
		state.setGetResource(resource);
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		state.setGiveResource(resource);
	}

	@Override
	public void unsetGetValue() {
		state.unsetGetValue();
	}

	@Override
	public void unsetGiveValue() {
		state.unsetGiveValue();
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
		//Update the state
		createState(facade.getPhase());
		//Initialize the state
		state.initFromModel();
	}

	/**
	 * Creates the controller's state
	 * @param phase Current game phase/state
     */
	private void createState(TurnTracker.Phase phase){
		switch (phase) {
			case SETUPONE:  state = new SetupOneState(getTradeView(), getTradeOverlay());
				break;
			case SETUPTWO:  state = new SetupTwoState(getTradeView(), getTradeOverlay());
				break;
			case ROLLING:  state = new RollingState(getTradeView(), getTradeOverlay());
				break;
			case PLAYING:  state = new PlayingState(getTradeView(), getTradeOverlay());
				break;
			case ROBBING:  state = new RobbingState(getTradeView(), getTradeOverlay());
				break;
			case DISCARDING:  state = new DiscardingState(getTradeView(), getTradeOverlay());
				break;
			case GAMEFINISHED:  state = new GameFinishedState(getTradeView(), getTradeOverlay());
				break;
			default: state = new PlayingState(getTradeView(), getTradeOverlay());
				break;
		}
	}
}
