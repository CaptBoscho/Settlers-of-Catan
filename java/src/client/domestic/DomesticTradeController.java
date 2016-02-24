package client.domestic;

import client.facade.Facade;
import client.services.UserCookie;
import shared.definitions.*;
import client.base.*;
import client.misc.*;

import java.util.Observable;
import java.util.Observer;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController, Observer {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private Facade facade;

	private int woodcount = 0;
	private int brickcount = 0;
	private int sheepcount = 0;
	private int wheatcount = 0;
	private int orecount = 0;

	private int sendwood = 0;
	private int sendbrick = 0;
	private int sendsheep = 0;
	private int sendwheat = 0;
	private int sendore = 0;

	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		facade = Facade.getInstance();
		facade.addObserver(this);
	}

	public void update(Observable obs, Object obj){
		getTradeView().enableDomesticTrade(facade.canTrade(UserCookie.getInstance().getPlayerInfo().getPlayerIndex()));
		woodcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerInfo().getPlayerIndex(), ResourceType.WOOD);
		brickcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerInfo().getPlayerIndex(), ResourceType.BRICK);
		sheepcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerInfo().getPlayerIndex(), ResourceType.SHEEP);
		wheatcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerInfo().getPlayerIndex(), ResourceType.WHEAT);
		orecount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerInfo().getPlayerIndex(), ResourceType.ORE);

		getTradeOverlay().setPlayers(facade.getOtherPlayers(UserCookie.getInstance().getPlayerInfo().getPlayerIndex()));
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	@Override
	public void startTrade() {

		getTradeOverlay().showModal();
		getTradeOverlay().setPlayerSelectionEnabled(true);
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {

	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		switch(resource){
			case BRICK:
				sendbrick++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendbrick < brickcount, true);
				break;
			case WOOD:
				sendwood++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendwood < woodcount, true);
				break;
			case WHEAT:
				sendwheat++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendwheat < wheatcount, true);
				break;
			case SHEEP:
				sendsheep++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendsheep < sheepcount, true);
				break;
			case ORE:
				sendore++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendore< orecount, true);

		}
	}

	@Override
	public void sendTradeOffer() {

		getTradeOverlay().closeModal();
//		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {

	}

	@Override
	public void setResourceToReceive(ResourceType resource) {

	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		switch(resource){
			case BRICK:
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, brickcount > 0, false);
				break;
			case WOOD:
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, woodcount > 0 , false);
				break;
			case WHEAT:
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, wheatcount > 0 , false);
				break;
			case SHEEP:
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sheepcount > 0 , false);
				break;
			case ORE:
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, orecount > 0 , false);
				break;

		}
	}

	@Override
	public void unsetResource(ResourceType resource) {

	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {

		getAcceptOverlay().closeModal();
	}

}

