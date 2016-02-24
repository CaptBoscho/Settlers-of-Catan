package client.resources;

import java.util.*;

import client.base.*;
import client.facade.Facade;
import client.services.UserCookie;
import shared.definitions.ResourceType;
import shared.exceptions.PlayerExistsException;
import shared.model.game.Game;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
    private Game game = null;
    private Facade facade = Facade.getInstance();

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
            int ID = UserCookie.getInstance().getPlayerInfo().getPlayerIndex();
            boolean enableRoad = facade.ableToBuildRoad(ID);
            boolean enableSettlement = facade.ableToBuildSettlement(ID);
            boolean enableCity = facade.ableToBuildCity(ID);
			boolean enableBuyDevCard = facade.canBuyDC(ID);
            boolean enablePlayCard = facade.canPlayDC(ID);

            int roadCount = facade.getAvailableRoads(ID);
            int settlementCount = facade.getAvailableSettlements(ID);
            int cityCount = facade.getAvailableCities(ID);
            int brickCount = facade.getAmountOfResource(ID, ResourceType.BRICK);
            int woodCount = facade.getAmountOfResource(ID, ResourceType.WOOD);
            int wheatCount = facade.getAmountOfResource(ID, ResourceType.WHEAT);
            int sheepCount = facade.getAmountOfResource(ID, ResourceType.SHEEP);
            int oreCount = facade.getAmountOfResource(ID, ResourceType.ORE);

            ResourceBarElement road = ResourceBarElement.ROAD;
            ResourceBarElement settle = ResourceBarElement.SETTLEMENT;
            ResourceBarElement city = ResourceBarElement.CITY;
			ResourceBarElement buyCard = ResourceBarElement.BUY_CARD;
            ResourceBarElement playCard = ResourceBarElement.PLAY_CARD;

            getView().setElementEnabled(road, enableRoad);
            getView().setElementEnabled(settle, enableSettlement);
            getView().setElementEnabled(city, enableCity);
			getView().setElementEnabled(buyCard, enableBuyDevCard);
            getView().setElementEnabled(playCard, enablePlayCard);

            getView().setElementAmount(road, roadCount);
            getView().setElementAmount(settle, settlementCount);
            getView().setElementAmount(city, cityCount);
            getView().setElementAmount(ResourceBarElement.WOOD, woodCount);
            getView().setElementAmount(ResourceBarElement.BRICK, brickCount);
            getView().setElementAmount(ResourceBarElement.WHEAT, wheatCount);
            getView().setElementAmount(ResourceBarElement.SHEEP, sheepCount);
            getView().setElementAmount(ResourceBarElement.ORE, oreCount);

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
            if (facade.ableToBuildRoad(UserCookie.getInstance().getPlayerId())) {
                executeElementAction(ResourceBarElement.ROAD);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buildSettlement() {
        try {
            if (facade.ableToBuildSettlement(UserCookie.getInstance().getPlayerInfo().getPlayerIndex())) {
                executeElementAction(ResourceBarElement.SETTLEMENT);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buildCity() {
        try {
            if (facade.ableToBuildCity(UserCookie.getInstance().getPlayerInfo().getPlayerIndex())) {
                executeElementAction(ResourceBarElement.CITY);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buyCard() {
        if(facade.ableToBuyDevCard(UserCookie.getInstance().getPlayerInfo().getPlayerIndex())) {
            executeElementAction(ResourceBarElement.BUY_CARD);
        }
	}

	@Override
	public void playCard() {

        if (facade.canPlayDC(UserCookie.getInstance().getPlayerInfo().getPlayerIndex())) {
            executeElementAction(ResourceBarElement.PLAY_CARD);
        }

	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			IAction action = elementActions.get(element);
			action.execute();
		}
	}
}

