package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.exceptions.BadCallerException;
import shared.model.cards.devcards.DevelopmentCard;

/**
 * Created by Danny on 2/1/16.
 */
public interface IDevelopmentCardBank {

    public DevelopmentCard draw() throws Exception;

    public void addDevCard(DevelopmentCard cardToAdd) throws InvalidTypeException;

    public int getNumberSoldierCards();

    public int size();

    public boolean canUseYearOfPlenty();

    public DevelopmentCard useYearOfPlenty();

    public boolean canUseSoldier();

    public DevelopmentCard useSoldier();

    public boolean canUseMonopoly();

    public DevelopmentCard useMonopoly();

    public boolean canUseMonument();

    public DevelopmentCard useMonument();

    public boolean canUseRoadBuild();

    public DevelopmentCard useRoadBuild();

    public void addDevCards(JsonObject DevCards) throws BadCallerException;

    public JsonObject toJSON();

    public JsonObject newCardsToJSON();

    DevelopmentCard discard(DevCardType type) throws InvalidTypeException;
}
