package com.dargueta.shared.model.bank;

import com.google.gson.JsonObject;
import com.dargueta.shared.definitions.DevCardType;
import com.dargueta.shared.exceptions.BadCallerException;
import com.dargueta.shared.model.JsonSerializable;

import java.util.*;


/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public final class DevelopmentCardBank implements JsonSerializable, IDevelopmentCardBank {
    static final int MAX_SOLDIER_CARDS = 14;
    static final int MAX_MONUMENT_CARDS = 5;
    static final int MAX_MONOPOLY_CARDS = 2;
    static final int MAX_YEAR_OF_PLENTY_CARDS = 2;
    static final int MAX_ROAD_BUILD_CARDS = 2;

    private boolean ownedByGame;

    private ArrayList<DevelopmentCard> developmentCards;

    private ArrayList<SoldierCard> soldierCards;
    private ArrayList<MonumentCard> monumentCards;
    private ArrayList<MonopolyCard> monopolyCards;
    private ArrayList<YearOfPlentyCard> yearOfPlentyCards;
    private ArrayList<RoadBuildCard> roadBuildCards;

    private ArrayList<SoldierCard> newSoldierCards;
    private ArrayList<MonumentCard> newMonumentCards;
    private ArrayList<MonopolyCard> newMonopolyCards;
    private ArrayList<YearOfPlentyCard> newYearOfPlentyCards;
    private ArrayList<RoadBuildCard> newRoadBuildCards;

    /**
     * Creates a DevelopmentCardBank
     *
     * @param ownedByGame True if the DevelopmentCardBank is owned by the Game, else False.
     */
    public DevelopmentCardBank(boolean ownedByGame) {
        this.ownedByGame = ownedByGame;

        if (ownedByGame) {
            developmentCards = new ArrayList<>();
        } else {
            soldierCards = new ArrayList<>();
            monumentCards = new ArrayList<>();
            monopolyCards = new ArrayList<>();
            yearOfPlentyCards = new ArrayList<>();
            roadBuildCards = new ArrayList<>();

            newSoldierCards = new ArrayList<>();
            newMonumentCards = new ArrayList<>();
            newMonopolyCards = new ArrayList<>();
            newYearOfPlentyCards = new ArrayList<>();
            newRoadBuildCards = new ArrayList<>();
        }

        if (ownedByGame) {
            try {
                fillSoldierCards();
                fillMonumentCards();
                fillMonopolyCards();
                fillYearOfPlentyCards();
                fillRoadBuildCards();
                Collections.shuffle(developmentCards);
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Construct a DevelopmentCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public DevelopmentCardBank(JsonObject json, boolean ownedByGame) {
        assert json != null;

        this.ownedByGame = ownedByGame;
        if (ownedByGame) {
            developmentCards = new ArrayList<>();
        } else {
            soldierCards = new ArrayList<>();
            monumentCards = new ArrayList<>();
            monopolyCards = new ArrayList<>();
            yearOfPlentyCards = new ArrayList<>();
            roadBuildCards = new ArrayList<>();

            newSoldierCards = new ArrayList<>();
            newMonumentCards = new ArrayList<>();
            newMonopolyCards = new ArrayList<>();
            newYearOfPlentyCards = new ArrayList<>();
            newRoadBuildCards = new ArrayList<>();
        }

        try {
            for (int i = 0; i < json.get("yearOfPlenty").getAsInt(); i++) {
                addDevCard(new YearOfPlentyCard());
            }
            for (int i = 0; i < json.get("monopoly").getAsInt(); i++) {
                addDevCard(new MonopolyCard());
            }
            for (int i = 0; i < json.get("monument").getAsInt(); i++) {
                addDevCard(new MonumentCard());
            }
            for (int i = 0; i < json.get("soldier").getAsInt(); i++) {
                addDevCard(new SoldierCard());
            }
            for (int i = 0; i < json.get("roadBuilding").getAsInt(); i++) {
                addDevCard(new RoadBuildCard());
            }
            if (!ownedByGame) {
                moveNewToOld();
            }
        } catch (InvalidTypeException | BadCallerException e) {
            e.printStackTrace();
        }
    }

    private void fillSoldierCards() throws InvalidTypeException {
        for (int i = 0; i < MAX_SOLDIER_CARDS; i++) {
            this.addDevCard(new SoldierCard());
        }
    }

    private void fillMonumentCards() throws InvalidTypeException {
        for (int i = 0; i < MAX_MONUMENT_CARDS; i++) {
            this.addDevCard(new MonumentCard());
        }
    }

    private void fillMonopolyCards() throws InvalidTypeException {
        for (int i = 0; i < MAX_MONOPOLY_CARDS; i++) {
            this.addDevCard(new MonopolyCard());
        }
    }

    private void fillYearOfPlentyCards() throws InvalidTypeException {
        for (int i = 0; i < MAX_YEAR_OF_PLENTY_CARDS; i++) {
            this.addDevCard(new YearOfPlentyCard());
        }
    }

    private void fillRoadBuildCards() throws InvalidTypeException {
        for (int i = 0; i < MAX_ROAD_BUILD_CARDS; i++) {
            this.addDevCard(new RoadBuildCard());
        }
    }

    /**
     * Adds a DevelopmentCard to the bank.
     *
     * (@pre) none
     * (@post) developmentCards.length() == old.length() + 1
     * (@post) cardToAdd is now in developmentCards
     *
     * @param cardToAdd Development card to add to the bank
     */
    @Override
    public void addDevCard(final DevelopmentCard cardToAdd) throws InvalidTypeException {
        assert cardToAdd != null;

        if (ownedByGame) {
            developmentCards.add(cardToAdd);
        } else {

            switch (cardToAdd.getType()) {
                case SOLDIER:
                    newSoldierCards.add((SoldierCard) cardToAdd);
                    break;
                case MONUMENT:
                    newMonumentCards.add((MonumentCard) cardToAdd);
                    break;
                case MONOPOLY:
                    newMonopolyCards.add((MonopolyCard) cardToAdd);
                    break;
                case YEAR_OF_PLENTY:
                    newYearOfPlentyCards.add((YearOfPlentyCard) cardToAdd);
                    break;
                case ROAD_BUILD:
                    newRoadBuildCards.add((RoadBuildCard) cardToAdd);
                    break;
                default:
                    throw new InvalidTypeException("The given card has an invalid type");
            }
        }
    }

    /**
     * @return the number of developmentCards in the bank
     */
    @Override
    public int size() {
        if (ownedByGame) {
            return developmentCards.size();
        } else {
            return soldierCards.size() + monumentCards.size() + monopolyCards.size() + yearOfPlentyCards.size() + roadBuildCards.size() +
                    newSoldierCards.size() + newMonumentCards.size() + newMonopolyCards.size() + newYearOfPlentyCards.size() + newRoadBuildCards.size();
        }
    }

    @Override
    public boolean canUseYearOfPlenty() {
        return !ownedByGame && yearOfPlentyCards.size() > 0;
    }

    @Override
    public DevelopmentCard useYearOfPlenty() {
        try {
            return discard(DevCardType.YEAR_OF_PLENTY);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canUseSoldier() {
        return !ownedByGame && (soldierCards.size() > 0);
    }

    @Override
    public DevelopmentCard useSoldier() {
        try {
            return discard(DevCardType.SOLDIER);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canUseMonopoly() {
        return !ownedByGame && (monopolyCards.size() > 0);
    }

    /**
     * Removes the Monopoly card from the user's bank
     *
     * @return
     */
    @Override
    public DevelopmentCard useMonopoly() {
        try {
            return discard(DevCardType.MONOPOLY);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canUseMonument() {
        return !ownedByGame && (monumentCards.size() > 0);
    }

    @Override
    public DevelopmentCard useMonument() {
        try {
            return discard(DevCardType.MONUMENT);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canUseRoadBuild() {
        return !ownedByGame && (roadBuildCards.size() > 0);
    }

    @Override
    public DevelopmentCard useRoadBuild() {
        try {
            return discard(DevCardType.ROAD_BUILD);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addDevCards(JsonObject DevCards) throws BadCallerException {
        if (ownedByGame) {
            throw new BadCallerException("Can't call this method on DevelopmentCardBank owned by game");
        } else {
            try {
                for (int i = 0; i < DevCards.get("monopoly").getAsInt(); i++) {
                    addDevCard(new MonopolyCard());
                }
                for (int i = 0; i < DevCards.get("monument").getAsInt(); i++) {
                    addDevCard(new MonumentCard());
                }
                for (int i = 0; i < DevCards.get("roadBuilding").getAsInt(); i++) {
                    addDevCard(new RoadBuildCard());
                }
                for (int i = 0; i < DevCards.get("soldier").getAsInt(); i++) {
                    addDevCard(new SoldierCard());
                }
                for (int i = 0; i < DevCards.get("yearOfPlenty").getAsInt(); i++) {
                    addDevCard(new YearOfPlentyCard());
                }
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes one DevelopmentCard from the DevelopmentCardBank and returns it
     * (@pre) getOwner() instanceof Game, not Player
     * (@pre) size() gt 0
     *
     * (@post) returned DevelopmentCard is no longer in bank
     * (@post) new size() == old size() - 1
     *
     * @return A DevelopmentCard from the DevelopmentCardBank
     */
    @Override
    public DevelopmentCard draw() throws Exception {
        if (!ownedByGame) {
            throw new Exception("Can't call draw on DevelopmentCardBank owned by Player!");
        } else {
            if (size() > 0) {
                return developmentCards.remove(0);
            } else {
                throw new Exception("No more available Development Cards in the bank.");
            }
        }
    }

    private int getNumberOfMonopolies() {
        if (ownedByGame) {
            int counter = 0;
            for (DevelopmentCard card : developmentCards) {
                if (card.getType() == DevCardType.MONOPOLY) {
                    counter++;
                }
            }
            return counter;
        } else {
            return monopolyCards.size();
        }
    }

    private int getNumberOfMonuments() {
        if (ownedByGame) {
            int counter = 0;
            for (DevelopmentCard card : developmentCards) {
                if (card.getType() == DevCardType.MONUMENT) {
                    counter++;
                }
            }
            return counter;
        } else {
            return monumentCards.size();
        }
    }

    private int getNumberOfRoadBuilds() {
        if (ownedByGame) {
            int counter = 0;
            for (DevelopmentCard card : developmentCards) {
                if (card.getType() == DevCardType.ROAD_BUILD) {
                    counter++;
                }
            }
            return counter;
        } else {
            return roadBuildCards.size();
        }
    }

    private int getNumberOfSoldiers() {
        if (ownedByGame) {
            int counter = 0;
            for (DevelopmentCard card : developmentCards) {
                if (card.getType() == DevCardType.SOLDIER) {
                    counter++;
                }
            }
            return counter;
        } else {
            return soldierCards.size();
        }
    }

    private int getNumberOfYearOfPlenty() {
        if (ownedByGame) {
            int counter = 0;
            for (DevelopmentCard card : developmentCards) {
                if (card.getType() == DevCardType.YEAR_OF_PLENTY) {
                    counter++;
                }
            }
            return counter;
        } else {
            return yearOfPlentyCards.size();
        }
    }

    @Override
    public DevelopmentCard discard(DevCardType type) throws InvalidTypeException {
        assert type != null;

        switch (type) {
            case SOLDIER:
                return soldierCards.remove(0);
            case MONUMENT:
                return monumentCards.remove(0);
            case MONOPOLY:
                return monopolyCards.remove(0);
            case YEAR_OF_PLENTY:
                return yearOfPlentyCards.remove(0);
            case ROAD_BUILD:
                return roadBuildCards.remove(0);
            default:
                throw new InvalidTypeException("The given card type is invalid");
        }
    }

    @Override
    public void empty() {
        if (ownedByGame) {
            while (!developmentCards.isEmpty()) {
                developmentCards.remove(0);
            }
        }
    }

    @Override
    public int getNumberOfDevCardsByType(DevCardType type) {
        switch (type) {
            case MONOPOLY:
                return newMonopolyCards.size() + monopolyCards.size();
            case MONUMENT:
                return newMonumentCards.size() + monumentCards.size();
            case ROAD_BUILD:
                return newRoadBuildCards.size() + roadBuildCards.size();
            case SOLDIER:
                return newSoldierCards.size() + soldierCards.size();
            case YEAR_OF_PLENTY:
                return  newYearOfPlentyCards.size() + yearOfPlentyCards.size();
        }
        return -1;
    }

    @Override
    public void moveNewToOld() throws BadCallerException {
        if (!ownedByGame) {
            while (newSoldierCards.size() > 0) {
                soldierCards.add(newSoldierCards.remove(0));
            }
            while (newMonumentCards.size() > 0) {
                monumentCards.add(newMonumentCards.remove(0));
            }
            while (newMonopolyCards.size() > 0) {
                monopolyCards.add(newMonopolyCards.remove(0));
            }
            while (newYearOfPlentyCards.size() > 0) {
                yearOfPlentyCards.add(newYearOfPlentyCards.remove(0));
            }
            while (newRoadBuildCards.size() > 0) {
                roadBuildCards.add(newRoadBuildCards.remove(0));
            }
        } else {
            throw new BadCallerException("Can't call moveNewToOld() on bank owned by game");
        }
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty("monopoly", getNumberOfMonopolies());
        json.addProperty("monument", getNumberOfMonuments());
        json.addProperty("roadBuilding", getNumberOfRoadBuilds());
        json.addProperty("soldier", getNumberOfSoldiers());
        json.addProperty("yearOfPlenty", getNumberOfYearOfPlenty());
        return json;
    }

    @Override
    public JsonObject newCardsToJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty("monopoly", newMonopolyCards.size());
        json.addProperty("monument", newMonumentCards.size());
        json.addProperty("roadBuilding", newRoadBuildCards.size());
        json.addProperty("soldier", newSoldierCards.size());
        json.addProperty("yearOfPlenty", newYearOfPlentyCards.size());
        return json;
    }
}
