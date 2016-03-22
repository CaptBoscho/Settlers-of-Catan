package shared.model.bank;

import shared.definitions.PortType;
import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 * @author Danny Harding
 */
public interface IResourceCardBank {

    void addResource(ResourceCard cardToAdd);

    ResourceCard draw(ResourceType type) throws InvalidTypeException, Exception;

    ResourceCard draw() throws Exception, InvalidTypeException;

    int size();

    List<ResourceCard> discard(ResourceType rt, int amount) throws InvalidTypeException, InsufficientResourcesException;

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

    JsonObject toJSON();

    boolean canBeRobbed();

    ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException;

    int getNumberOfBrick();

    int getNumberOfOre();

    int getNumberOfSheep();

    int getNumberOfWheat();

    int getNumberOfWood();

    Integer getNumberOfType(ResourceType type);
}
