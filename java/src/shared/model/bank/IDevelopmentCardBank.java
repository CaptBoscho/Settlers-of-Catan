package shared.model.bank;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import shared.exceptions.BadCallerException;
import shared.model.devcards.DevelopmentCard;

/**
 * Created by Danny on 2/1/16.
 */
public interface IDevelopmentCardBank {

    public DevelopmentCard draw() throws Exception;

    public void addDevCard(DevelopmentCard cardToAdd) throws InvalidTypeException;

    public int getNumberSoldierCards();

    public int size();

    public boolean canUseYearOfPlenty();

    public void useYearOfPlenty();

    public boolean canUseSoldier();

    public void useSoldier();

    public boolean canUseMonopoly();

    public void useMonopoly();

    public boolean canUseMonument();

    public void useMonument();

    public boolean canUseRoadBuild();

    public void useRoadBuild();

    public void addDevCards(JsonObject DevCards) throws BadCallerException;

    public JsonObject toJSON();

    public JsonObject newCardsToJSON();
}
