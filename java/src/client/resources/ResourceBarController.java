package client.resources;

import java.util.*;

import client.base.*;
import client.facade.Facade;
import client.services.UserCookie;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
    private Game game = null;
    private Facade facade = Facade.getInstance();
    private int ID;
	
	public ResourceBarController(IResourceBarView view) {
		super(view);
		elementActions = new HashMap<ResourceBarElement, IAction>();
        facade.addObserver(this);

	}

    /**
     * this is where my code goes that updates the ResourceElements
     * @param obs
     * @param obj
     */
	public void update(Observable obs, Object obj){
        try {
            ID  = UserCookie.getInstance().getPlayerId();
            boolean enableRoad = facade.ableToBuildRoad(ID);
            boolean enableSettlement = facade.ableToBuildSettlement(ID);
            boolean enableCity = facade.ableToBuildCity(ID);

            int roadcount = facade.getAvailableRoads(ID);
            int settlementcount = facade.getAvailableSettlements(ID);
            int citycount = facade.getAvailableCities(ID);

            ResourceBarElement road = ResourceBarElement.ROAD;
            ResourceBarElement settle = ResourceBarElement.SETTLEMENT;
            ResourceBarElement city = ResourceBarElement.CITY;

            getView().setElementEnabled(road, enableRoad);
            getView().setElementEnabled(settle, enableSettlement);
            getView().setElementEnabled(city, enableCity);

            getView().setElementAmount(road, roadcount);
            getView().setElementAmount(settle, settlementcount);
            getView().setElementAmount(city, citycount);
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
    }

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

        elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
        try {
            if (facade.ableToBuildRoad(UserCookie.getInstance().getPlayerId()) == true) {
                executeElementAction(ResourceBarElement.ROAD);
            }
        } catch(PlayerExistsException e){}
	}

	@Override
	public void buildSettlement() {
        try {
            if (facade.ableToBuildRoad(UserCookie.getInstance().getPlayerId()) == true) {
                executeElementAction(ResourceBarElement.SETTLEMENT);
            }
        } catch(PlayerExistsException e){}
	}

	@Override
	public void buildCity() {
        try {
            if (facade.ableToBuildRoad(UserCookie.getInstance().getPlayerId()) == true) {
                executeElementAction(ResourceBarElement.CITY);
            }
        } catch(PlayerExistsException e){}
	}

	@Override
	public void buyCard() {
        if(facade.ableToBuyDevCard(UserCookie.getInstance().getPlayerId()) == true) {
            executeElementAction(ResourceBarElement.BUY_CARD);
        }
	}

	@Override
	public void playCard() {
        try {
            if (facade.canPlayDC(UserCookie.getInstance().getPlayerId()) == true) {
                executeElementAction(ResourceBarElement.PLAY_CARD);
            }
        } catch(PlayerExistsException e){}
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			IAction action = elementActions.get(element);
			action.execute();
		}
	}
}

