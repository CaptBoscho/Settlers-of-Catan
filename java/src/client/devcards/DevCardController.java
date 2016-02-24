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
		int localPlayerIndex = UserCookie.getInstance().getPlayerIndex();

		// enable buttons to play specific devCard types
		getPlayCardView().setCardEnabled(DevCardType.MONOPOLY, facade.canUseMonopoly(localPlayerIndex));
		getPlayCardView().setCardEnabled(DevCardType.MONUMENT, facade.canUseMonument(localPlayerIndex));
		getPlayCardView().setCardEnabled(DevCardType.ROAD_BUILD, facade.canUseRoadBuilder(localPlayerIndex));
		getPlayCardView().setCardEnabled(DevCardType.SOLDIER, facade.canUseSoldier(localPlayerIndex));
		getPlayCardView().setCardEnabled(DevCardType.YEAR_OF_PLENTY, facade.canUseYearOfPlenty(localPlayerIndex));

		// set amounts of each dev card type in the playCardView
		getPlayCardView().setCardAmount(DevCardType.MONOPOLY, facade.getNumberDevCards(DevCardType.MONOPOLY, localPlayerIndex));
		getPlayCardView().setCardAmount(DevCardType.MONUMENT, facade.getNumberDevCards(DevCardType.MONUMENT, localPlayerIndex));
		getPlayCardView().setCardAmount(DevCardType.ROAD_BUILD, facade.getNumberDevCards(DevCardType.ROAD_BUILD, localPlayerIndex));
		getPlayCardView().setCardAmount(DevCardType.SOLDIER, facade.getNumberDevCards(DevCardType.SOLDIER, localPlayerIndex));
		getPlayCardView().setCardAmount(DevCardType.YEAR_OF_PLENTY, facade.getNumberDevCards(DevCardType.YEAR_OF_PLENTY, localPlayerIndex));
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
			Facade.getInstance().buyDC(UserCookie.getInstance().getPlayerIndex());
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
