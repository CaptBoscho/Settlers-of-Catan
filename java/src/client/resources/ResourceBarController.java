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
            System.out.println("update function in ResourceBarController");
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
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			IAction action = elementActions.get(element);
			action.execute();
		}
	}
}

