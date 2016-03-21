package client.discard;

import client.facade.Facade;
import client.services.UserCookie;
import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.model.game.TurnTracker;

import java.util.Observable;
import java.util.Observer;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	private Facade facade;
    private UserCookie userCookie;

    private final ResourceType brick = ResourceType.BRICK;
    private final ResourceType ore = ResourceType.ORE;
    private final ResourceType sheep = ResourceType.SHEEP;
    private final ResourceType wheat = ResourceType.WHEAT;
    private final ResourceType wood = ResourceType.WOOD;

    private int totalRemainingCards;
    private int totalDiscardedCards;
    private int discarding;
    private int discardedBrick;
    private int discardedOre;
    private int discardedSheep;
    private int discardedWheat;
    private int discardedWood;
    private int remainingBrick;
    private int remainingOre;
    private int remainingSheep;
    private int remainingWheat;
    private int remainingWood;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting
     *                 for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		super(view);
		this.waitView = waitView;
        userCookie = UserCookie.getInstance();
		facade = Facade.getInstance();
		facade.addObserver(this);
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

    private void initFromModel() {
        if(facade.getPhase() == TurnTracker.Phase.DISCARDING) {
            totalDiscardedCards = 0;
            discardedBrick = 0;
            discardedOre = 0;
            discardedSheep = 0;
            discardedWheat = 0;
            discardedWood = 0;
            totalRemainingCards = facade.getNumberResourceCards(userCookie.getPlayerIndex());
            remainingBrick = facade.getAmountOfResource(userCookie.getPlayerIndex(), brick);
            remainingOre = facade.getAmountOfResource(userCookie.getPlayerIndex(), ore);
            remainingSheep = facade.getAmountOfResource(userCookie.getPlayerIndex(), sheep);
            remainingWheat = facade.getAmountOfResource(userCookie.getPlayerIndex(), wheat);
            remainingWood = facade.getAmountOfResource(userCookie.getPlayerIndex(), wood);
            discarding = totalRemainingCards / 2;
            if(facade.canDiscard(userCookie.getPlayerIndex()) && !facade.hasDiscarded(userCookie.getPlayerIndex())) {
                if(!getDiscardView().isModalShowing()){
                    getDiscardView().setDiscardButtonEnabled(false);

                    getDiscardView().setResourceAmountChangeEnabled(brick, remainingBrick > 0, false);
                    getDiscardView().setResourceAmountChangeEnabled(ore, remainingOre > 0, false);
                    getDiscardView().setResourceAmountChangeEnabled(sheep, remainingSheep > 0, false);
                    getDiscardView().setResourceAmountChangeEnabled(wheat, remainingWheat > 0, false);
                    getDiscardView().setResourceAmountChangeEnabled(wood, remainingWood > 0, false);

                    getDiscardView().setResourceDiscardAmount(brick, 0);
                    getDiscardView().setResourceDiscardAmount(ore, 0);
                    getDiscardView().setResourceDiscardAmount(sheep, 0);
                    getDiscardView().setResourceDiscardAmount(wheat, 0);
                    getDiscardView().setResourceDiscardAmount(wood, 0);

                    getDiscardView().setResourceMaxAmount(brick, remainingBrick);
                    getDiscardView().setResourceMaxAmount(ore, remainingOre);
                    getDiscardView().setResourceMaxAmount(sheep, remainingSheep);
                    getDiscardView().setResourceMaxAmount(wheat, remainingWheat);
                    getDiscardView().setResourceMaxAmount(wood, remainingWood);
                    getDiscardView().setStateMessage("Discard: 0/" + discarding);

                    getDiscardView().showModal();
                }
            } else {
                if(!getWaitView().isModalShowing()) {
                    getWaitView().setMessage("Chill while others discard...");
                    getWaitView().showModal();
                }
            }
        } else {
            if(getWaitView().isModalShowing()) {
                getWaitView().closeModal();
            }
        }
    }

	@Override
	public void increaseAmount(ResourceType resource) {
		switch (resource) {
            case BRICK:
                discardedBrick++;
                remainingBrick--;
                getDiscardView().setResourceDiscardAmount(resource, discardedBrick);
                break;
            case ORE:
                discardedOre++;
                remainingOre--;
                getDiscardView().setResourceDiscardAmount(resource, discardedOre);
                break;
            case SHEEP:
                discardedSheep++;
                remainingSheep--;
                getDiscardView().setResourceDiscardAmount(resource, discardedSheep);
                break;
            case WHEAT:
                discardedWheat++;
                remainingWheat--;
                getDiscardView().setResourceDiscardAmount(resource, discardedWheat);
                break;
            case WOOD:
                discardedWood++;
                remainingWood--;
                getDiscardView().setResourceDiscardAmount(resource, discardedWood);
                break;
            default:
                break;
        }
        totalDiscardedCards++;
        totalRemainingCards--;
        getDiscardView().setStateMessage(totalDiscardedCards + "/" + discarding);
        if(totalDiscardedCards == discarding) {
            getDiscardView().setDiscardButtonEnabled(true);
            getDiscardView().setResourceAmountChangeEnabled(brick, false, discardedBrick > 0);
            getDiscardView().setResourceAmountChangeEnabled(ore, false, discardedOre > 0);
            getDiscardView().setResourceAmountChangeEnabled(sheep, false, discardedSheep > 0);
            getDiscardView().setResourceAmountChangeEnabled(wheat, false, discardedWheat > 0);
            getDiscardView().setResourceAmountChangeEnabled(wood, false, discardedWood > 0);
        } else {
            getDiscardView().setResourceAmountChangeEnabled(brick, remainingBrick > 0, discardedBrick > 0);
            getDiscardView().setResourceAmountChangeEnabled(ore, remainingOre > 0, discardedOre > 0);
            getDiscardView().setResourceAmountChangeEnabled(sheep, remainingSheep > 0, discardedSheep > 0);
            getDiscardView().setResourceAmountChangeEnabled(wheat, remainingWheat > 0, discardedWheat > 0);
            getDiscardView().setResourceAmountChangeEnabled(wood, remainingWood > 0, discardedWood > 0);
        }
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
        switch (resource) {
            case BRICK:
                discardedBrick--;
                remainingBrick++;
                getDiscardView().setResourceDiscardAmount(resource, discardedBrick);
                break;
            case ORE:
                discardedOre--;
                remainingOre++;
                getDiscardView().setResourceDiscardAmount(resource, discardedOre);
                break;
            case SHEEP:
                discardedSheep--;
                remainingSheep++;
                getDiscardView().setResourceDiscardAmount(resource, discardedSheep);
                break;
            case WHEAT:
                discardedWheat--;
                remainingWheat++;
                getDiscardView().setResourceDiscardAmount(resource, discardedWheat);
                break;
            case WOOD:
                discardedWood--;
                remainingWood++;
                getDiscardView().setResourceDiscardAmount(resource, discardedWood);
                break;
            default:
                break;
        }
        totalDiscardedCards--;
        totalRemainingCards++;
        getDiscardView().setStateMessage(totalDiscardedCards + "/" + discarding);
        if(totalDiscardedCards != discarding) {
            getDiscardView().setDiscardButtonEnabled(false);
            getDiscardView().setResourceAmountChangeEnabled(brick, remainingBrick > 0, discardedBrick > 0);
            getDiscardView().setResourceAmountChangeEnabled(ore, remainingOre > 0, discardedOre > 0);
            getDiscardView().setResourceAmountChangeEnabled(sheep, remainingSheep > 0, discardedSheep > 0);
            getDiscardView().setResourceAmountChangeEnabled(wheat, remainingWheat > 0, discardedWheat > 0);
            getDiscardView().setResourceAmountChangeEnabled(wood, remainingWood > 0, discardedWood > 0);
        }
	}

	@Override
	public void discard() {
        getDiscardView().closeModal();
        facade.discard(userCookie.getPlayerIndex(),
                discardedBrick, discardedOre, discardedSheep, discardedWheat, discardedWood);
	}

	@Override
	public void update(Observable o, Object arg) {
		initFromModel();
	}
}

