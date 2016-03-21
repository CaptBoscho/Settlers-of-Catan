package com.dargueta.shared.model.bank;

import com.google.gson.JsonObject;
import com.dargueta.shared.definitions.DevCardType;
import com.dargueta.shared.exceptions.BadCallerException;
import com.dargueta.shared.model.cards.devcards.DevelopmentCard;

/**
 * @author Derek Argueta
 */
public interface IDevelopmentCardBank {

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
