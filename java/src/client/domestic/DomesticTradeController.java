package client.domestic;

import client.facade.Facade;
import client.services.UserCookie;
import shared.definitions.*;
import client.base.*;
import client.misc.*;

import java.util.ArrayList;
import java.util.List;
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

	private int receivewood = 0;
	private int receivebrick = 0;
	private int receivesheep = 0;
	private int receivewheat = 0;
	private int receiveore = 0;

	//False means sending, true means receiving
	private boolean woodStatus = false;
	private boolean brickStatus = false;
	private boolean sheepStatus = false;
	private boolean wheatStatus = false;
	private boolean oreStatus = false;

	private int tradePartner = -1;


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
		getTradeView().enableDomesticTrade(facade.canTrade(UserCookie.getInstance().getPlayerIndex()));
		woodcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerIndex(), ResourceType.WOOD);
		brickcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerIndex(), ResourceType.BRICK);
		sheepcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerIndex(), ResourceType.SHEEP);
		wheatcount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerIndex(), ResourceType.WHEAT);
		orecount = facade.getAmountOfResource(UserCookie.getInstance().getPlayerIndex(), ResourceType.ORE);

		sendwood =0;
		sendbrick = 0;
		sendsheep = 0;
		sendwheat = 0;
		sendore = 0;

		receivewood = 0;
		receivebrick = 0;
		receivesheep = 0;
		receivewheat = 0;
		receiveore = 0;

		woodStatus = false;
		brickStatus = false;
		sheepStatus = false;
		wheatStatus = false;
		oreStatus = false;

		tradePartner = -1;

		getTradeOverlay().setTradeEnabled(false);

				

		if(facade.isTradeActive()){
			if(facade.getTradeSender() == UserCookie.getInstance().getPlayerIndex()){
				getWaitOverlay().showModal();
			}else if(facade.getTradeReceiver() == UserCookie.getInstance().getPlayerIndex()){
				createAccept();
			}
		}
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

	public void createAccept(){
		int brick = facade.getTradeBrick();
		int wood = facade.getTradeWood();
		int sheep = facade.getTradeSheep();
		int wheat = facade.getTradeWheat();
		int ore = facade.getTradeOre();

		if(brick > 0){
			getAcceptOverlay().addGetResource(ResourceType.BRICK, brick);
		}else if(brick < 0){
			brick = brick * -1;
			getAcceptOverlay().addGiveResource(ResourceType.BRICK, brick);
		}

		if(wood > 0){
			getAcceptOverlay().addGetResource(ResourceType.WOOD, wood);
		}else if(wood < 0){
			wood = wood * -1;
			getAcceptOverlay().addGiveResource(ResourceType.WOOD, wood);
		}

		if(sheep > 0){
			getAcceptOverlay().addGetResource(ResourceType.SHEEP, sheep);
		}else if(sheep < 0){
			sheep = sheep * -1;
			getAcceptOverlay().addGiveResource(ResourceType.SHEEP, sheep);
		}

		if(wheat > 0){
			getAcceptOverlay().addGetResource(ResourceType.WHEAT, wheat);
		}else if(wheat < 0){
			wheat = wheat * -1;
			getAcceptOverlay().addGiveResource(ResourceType.WHEAT, wheat);
		}

		if(ore > 0){
			getAcceptOverlay().addGetResource(ResourceType.ORE, ore);
		}else if(ore < 0){
			ore = ore * -1;
			getAcceptOverlay().addGiveResource(ResourceType.ORE, ore);
		}

		getAcceptOverlay().showModal();
	}

	@Override
	public void startTrade() {

		getTradeOverlay().setPlayers(facade.getOtherPlayers(UserCookie.getInstance().getPlayerIndex()));
		getTradeOverlay().showModal();
		getTradeOverlay().setPlayerSelectionEnabled(true);
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		switch(resource){
			case BRICK:
				if(brickStatus){
					receivebrick--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, true, receivebrick > 0);
				}else {
					sendbrick--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendbrick < brickcount, sendbrick > 0);
				}
				break;
			case WOOD:
				if(woodStatus){
					receivewood--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, true, receivewood > 0);
				}else {
					sendwood--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendwood < woodcount, sendwood > 0);
				}
				break;
			case WHEAT:
				if(wheatStatus){
					receivewheat--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, receivewheat > 0);
				}else {
					sendwheat--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendwheat < wheatcount, sendwheat > 0);
				}
				break;
			case SHEEP:
				if(sheepStatus){
					receivesheep--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, receivesheep > 0);
				}else {
					sendsheep--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendsheep < sheepcount, sendsheep > 0);
				}
				break;
			case ORE:
				if(oreStatus){
					receiveore--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, true, receiveore > 0);
				} else {
					sendore--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendore < orecount, sendore > 0);
				}
		}
		if((sendbrick+sendwood+sendsheep+sendwheat+sendore > 0) && (receivebrick+receivewood+receiveore+receivesheep+receivewheat > 0) && tradePartner != -1){
			getTradeOverlay().setTradeEnabled(true);
		}else{getTradeOverlay().setTradeEnabled(false);}
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		switch(resource){
			case BRICK:
				if(brickStatus) {
					receivebrick++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, true, receivebrick > 0);
				}
				else{
					sendbrick++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendbrick < brickcount, true);
				}
				break;
			case WOOD:
				if(woodStatus){
					receivewood++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, true, receivewood > 0);
				}else {
					sendwood++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendwood < woodcount, true);
				}
				break;
			case WHEAT:
				if(wheatStatus){
					receivewheat++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, receivewheat > 0);
				}else {
					sendwheat++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendwheat < wheatcount, true);
				}
				break;
			case SHEEP:
				if(sheepStatus){
					receivesheep++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, receivesheep > 0);
				}else {
					sendsheep++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendsheep < sheepcount, true);
				}
				break;
			case ORE:
				if(oreStatus){
					receiveore++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, true, receiveore > 0);
				}else {
					sendore++;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendore < orecount, true);
				}

		}
		if((sendbrick+sendwood+sendsheep+sendwheat+sendore > 0) && (receivebrick+receivewood+receiveore+receivesheep+receivewheat > 0) && tradePartner != -1){
			getTradeOverlay().setTradeEnabled(true);
		}
	}

	///TODO create trade object
	@Override
	public void sendTradeOffer() {
		List<ResourceType> sending = new ArrayList<>();
		for(int i=0; i<sendbrick; i++){sending.add(ResourceType.BRICK);}
		for(int i=0; i<sendwood; i++){sending.add(ResourceType.WOOD);}
		for(int i=0; i<sendwheat; i++){sending.add(ResourceType.WHEAT);}
		for(int i=0; i<sendsheep; i++){sending.add(ResourceType.SHEEP);}
		for(int i=0; i<sendore; i++){sending.add(ResourceType.ORE);}

		List<ResourceType> receiving = new ArrayList<>();
		for(int i=0; i<receivebrick; i++){receiving.add(ResourceType.BRICK);}
		for(int i=0; i<receivewood; i++){receiving.add(ResourceType.WOOD);}
		for(int i=0; i<receivesheep; i++){receiving.add(ResourceType.SHEEP);}
		for(int i=0; i<receivewheat; i++){receiving.add(ResourceType.WHEAT);}
		for(int i=0; i<receiveore; i++){receiving.add(ResourceType.ORE);}

		facade.tradeWithPlayer(UserCookie.getInstance().getPlayerIndex(), tradePartner, sending, receiving);
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		tradePartner = playerIndex;
		if((sendbrick+sendwood+sendsheep+sendwheat+sendore > 0) && (receivebrick+receivewood+receiveore+receivesheep+receivewheat > 0)){
			getTradeOverlay().setTradeEnabled(true);
		}
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		switch(resource){
			case BRICK:
				brickStatus = true;
				receivebrick = 0;
				getTradeOverlay().setResourceAmount(ResourceType.BRICK,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, true, false);
				break;
			case WOOD:
				woodStatus = true;
				receivewood = 0;
				getTradeOverlay().setResourceAmount(ResourceType.WOOD,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, true, false);
				break;
			case WHEAT:
				wheatStatus = true;
				receivewheat = 0;
				getTradeOverlay().setResourceAmount(ResourceType.WHEAT,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, false);
				break;
			case SHEEP:
				sheepStatus = true;
				receivesheep = 0;
				getTradeOverlay().setResourceAmount(ResourceType.SHEEP,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, false);
				break;
			case ORE:
				oreStatus = true;
				receiveore = 0;
				getTradeOverlay().setResourceAmount(ResourceType.ORE,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, true, false);
				break;

		}
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		switch(resource){
			case BRICK:
				brickStatus = false;
				sendbrick = 0;
				getTradeOverlay().setResourceAmount(ResourceType.BRICK,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, brickcount > 0, false);
				break;
			case WOOD:
				woodStatus = false;
				sendwood = 0;
				getTradeOverlay().setResourceAmount(ResourceType.WOOD,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, woodcount > 0 , false);
				break;
			case WHEAT:
				wheatStatus = false;
				sendwheat = 0;
				getTradeOverlay().setResourceAmount(ResourceType.WHEAT,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, wheatcount > 0 , false);
				break;
			case SHEEP:
				sheepStatus = false;
				sendsheep = 0;
				getTradeOverlay().setResourceAmount(ResourceType.SHEEP,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sheepcount > 0 , false);
				break;
			case ORE:
				oreStatus = false;
				sendore = 0;
				getTradeOverlay().setResourceAmount(ResourceType.ORE,"0");
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, orecount > 0 , false);
				break;

		}
	}

	@Override
	public void unsetResource(ResourceType resource) {
		switch(resource){
			case BRICK:
				sendbrick = 0;
				receivebrick = 0;
				break;
			case WOOD:
				sendwood = 0;
				receivewood = 0;
				break;
			case SHEEP:
				sendsheep = 0;
				receivesheep = 0;
				break;
			case WHEAT:
				sendwheat = 0;
				receivewheat = 0;
				break;
			case ORE:
				sendore = 0;
				receiveore = 0;
				break;
		}
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

