package shared.model.bank;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.game.trade.TradeType;
import shared.model.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;

/**
 * Created by Danny on 2/1/16.
 */
public interface IResourceCardBank {

    void addResource(ResourceCard cardToAdd);

    ResourceCard draw(ResourceType type) throws InvalidTypeException, Exception;

    ResourceCard draw() throws Exception;

    int size();

    ResourceCard discard(ResourceType type) throws InsufficientResourcesException, InvalidTypeException;

    boolean canOfferTrade();

    boolean canMaritimeTrade(PortType type) throws InsufficientResourcesException, InvalidTypeException;

    boolean canBuyDevCard();

    void buyDevCard();

    boolean canBuildRoad();

    void buildRoad() throws InsufficientResourcesException;

    boolean canBuildSettlement();

    void buildSettlement() throws InsufficientResourcesException;

    boolean canBuildCity();

    void buildCity() throws InsufficientResourcesException;

    boolean canDiscardCards();
}
