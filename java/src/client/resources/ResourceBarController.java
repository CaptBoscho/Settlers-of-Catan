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
		elementActions = new HashMap<>();
        facade.addObserver(this);
	}

    /**
     * this is where my code goes that updates the ResourceElements
     * @param obs
     * @param obj
     */
	public void update(Observable obs, Object obj){
        try {
            int playerIndex = UserCookie.getInstance().getPlayerIndex();
            boolean enableRoad = facade.ableToBuildRoad(playerIndex);
            boolean enableSettlement = facade.ableToBuildSettlement(playerIndex);
            boolean enableCity = facade.ableToBuildCity(playerIndex);
			boolean enableBuyDevCard = facade.canBuyDC(playerIndex);
            boolean enablePlayDevCard = facade.canPlayDC(playerIndex);

            int roadCount = facade.getAvailableRoads(playerIndex);
            int settlementCount = facade.getAvailableSettlements(playerIndex);
            int cityCount = facade.getAvailableCities(playerIndex);
            int brickCount = facade.getAmountOfResource(playerIndex, ResourceType.BRICK);
            int woodCount = facade.getAmountOfResource(playerIndex, ResourceType.WOOD);
            int wheatCount = facade.getAmountOfResource(playerIndex, ResourceType.WHEAT);
            int sheepCount = facade.getAmountOfResource(playerIndex, ResourceType.SHEEP);
            int oreCount = facade.getAmountOfResource(playerIndex, ResourceType.ORE);
            int soldierCount = facade.getNumberOfSoldiers(playerIndex);

            ResourceBarElement road = ResourceBarElement.ROAD;
            ResourceBarElement settle = ResourceBarElement.SETTLEMENT;
            ResourceBarElement city = ResourceBarElement.CITY;
			ResourceBarElement buyCard = ResourceBarElement.BUY_CARD;
            ResourceBarElement playCard = ResourceBarElement.PLAY_CARD;

            getView().setElementEnabled(road, enableRoad);
            getView().setElementEnabled(settle, enableSettlement);
            getView().setElementEnabled(city, enableCity);
			getView().setElementEnabled(buyCard, enableBuyDevCard);
            getView().setElementEnabled(playCard, enablePlayDevCard);

            getView().setElementAmount(road, roadCount);
            getView().setElementAmount(settle, settlementCount);
            getView().setElementAmount(city, cityCount);
            getView().setElementAmount(ResourceBarElement.WOOD, woodCount);
            getView().setElementAmount(ResourceBarElement.BRICK, brickCount);
            getView().setElementAmount(ResourceBarElement.WHEAT, wheatCount);
            getView().setElementAmount(ResourceBarElement.SHEEP, sheepCount);
            getView().setElementAmount(ResourceBarElement.ORE, oreCount);

            getView().setElementAmount(ResourceBarElement.SOLDIERS, soldierCount);

            if (facade.getWinnerId() != -1) {
                getView().setElementEnabled(road, false);
                getView().setElementEnabled(settle, false);
                getView().setElementEnabled(city, false);
                getView().setElementEnabled(buyCard, false);
                getView().setElementEnabled(playCard, false);
            }

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
            if (facade.ableToBuildRoad(UserCookie.getInstance().getPlayerIndex())) {
                executeElementAction(ResourceBarElement.ROAD);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buildSettlement() {
        try {
            if (facade.ableToBuildSettlement(UserCookie.getInstance().getPlayerIndex())) {
                executeElementAction(ResourceBarElement.SETTLEMENT);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buildCity() {
        try {
            if (facade.ableToBuildCity(UserCookie.getInstance().getPlayerIndex())) {
                executeElementAction(ResourceBarElement.CITY);
            }
        } catch(PlayerExistsException e){
            e.printStackTrace();
        }
	}

	@Override
	public void buyCard() {
        if(facade.ableToBuyDevCard(UserCookie.getInstance().getPlayerIndex())) {
            executeElementAction(ResourceBarElement.BUY_CARD);
        }
	}

	@Override
	public void playCard() {

        if (facade.canPlayDC(UserCookie.getInstance().getPlayerIndex())) {
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

