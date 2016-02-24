package client.devcards;

import client.facade.Facade;
import client.services.MissingUserCookieException;
import client.services.UserCookie;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import client.base.*;

import java.util.Observable;
import java.util.Observer;


/**
 * "Dev card" controller implementation
 */
public final class DevCardController extends Controller implements IDevCardController, Observer {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
		Facade.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		Facade facade = Facade.getInstance();
		int localPlayerID = UserCookie.getInstance().getPlayerId();

		// enable buttons to play specific devCard types
		getPlayCardView().setCardEnabled(DevCardType.MONOPOLY, facade.canUseMonopoly(localPlayerID));
		getPlayCardView().setCardEnabled(DevCardType.MONUMENT, facade.canUseMonument(localPlayerID));
		getPlayCardView().setCardEnabled(DevCardType.ROAD_BUILD, facade.canUseRoadBuilder(localPlayerID));
		getPlayCardView().setCardEnabled(DevCardType.SOLDIER, facade.canUseSoldier(localPlayerID));
		getPlayCardView().setCardEnabled(DevCardType.YEAR_OF_PLENTY, facade.canUseYearOfPlenty(localPlayerID));

		// set amounts of each dev card type in the playCardView
		getPlayCardView().setCardAmount(DevCardType.MONOPOLY, facade.getNumberDevCards(DevCardType.MONOPOLY, localPlayerID));
		getPlayCardView().setCardAmount(DevCardType.MONUMENT, facade.getNumberDevCards(DevCardType.MONUMENT, localPlayerID));
		getPlayCardView().setCardAmount(DevCardType.ROAD_BUILD, facade.getNumberDevCards(DevCardType.ROAD_BUILD, localPlayerID));
		getPlayCardView().setCardAmount(DevCardType.SOLDIER, facade.getNumberDevCards(DevCardType.SOLDIER, localPlayerID));
		getPlayCardView().setCardAmount(DevCardType.YEAR_OF_PLENTY, facade.getNumberDevCards(DevCardType.YEAR_OF_PLENTY, localPlayerID));
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		try {
			Facade.getInstance().buyDC(UserCookie.getInstance().getPlayerId());
			getBuyCardView().closeModal();
		} catch (MissingUserCookieException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startPlayCard() {
		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {
		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		Facade.getInstance().playMonopolyCard(resource);
	}

	@Override
	public void playMonumentCard() {
		Facade.getInstance().playMonumentCard();
	}

	@Override
	public void playRoadBuildCard() {
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {
		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		Facade.getInstance().playYearOfPlentyCard(resource1, resource2);
	}
}
