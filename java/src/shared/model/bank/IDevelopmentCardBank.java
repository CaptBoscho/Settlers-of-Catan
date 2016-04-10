package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.exceptions.BadCallerException;
import shared.model.cards.devcards.DevelopmentCard;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public interface IDevelopmentCardBank extends Serializable {

    DevelopmentCard draw() throws Exception;

    void addDevCard(DevelopmentCard cardToAdd) throws InvalidTypeException;

    int size();

    boolean canUseYearOfPlenty();

    DevelopmentCard useYearOfPlenty();

    boolean canUseSoldier();

    DevelopmentCard useSoldier();

    boolean canUseMonopoly();

    DevelopmentCard useMonopoly();

    boolean canUseMonument();

    DevelopmentCard useMonument();

    boolean canUseRoadBuild();

    DevelopmentCard useRoadBuild();

    void addDevCards(JsonObject DevCards) throws BadCallerException;

    void moveNewToOld() throws BadCallerException;

    JsonObject toJSON();

    JsonObject newCardsToJSON();

    DevelopmentCard discard(DevCardType type) throws InvalidTypeException;

    void empty();

    int getNumberOfDevCardsByType(DevCardType type);
}
