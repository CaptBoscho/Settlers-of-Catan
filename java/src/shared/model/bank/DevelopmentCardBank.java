package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.exceptions.BadCallerException;
import shared.model.JsonSerializable;
import shared.model.cards.devcards.*;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public class DevelopmentCardBank implements JsonSerializable, IDevelopmentCardBank {
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
    public DevelopmentCardBank(JsonObject json) {
        //deserialize blah blah blah
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
     * @pre none
     * @post developmentCards.length() == old.length() + 1
     * @post cardToAdd is now in developmentCards
     *
     * @param cardToAdd Development card to add to the bank
     */
    @Override
    public void addDevCard(DevelopmentCard cardToAdd) throws InvalidTypeException {
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

    @Override
    public int getNumberSoldierCards() {
        return soldierCards.size();
    }

    /**
     * @return the number of developmentCards in the bank
     */
    @Override
    public int size() {
        if (ownedByGame) {
            return developmentCards.size();
        } else {
            return soldierCards.size() + monumentCards.size() + monopolyCards.size() + yearOfPlentyCards.size() + roadBuildCards.size();
        }
    }

    @Override
    public boolean canUseYearOfPlenty() {
        return (yearOfPlentyCards.size() > 0);
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
        return (soldierCards.size() > 0);
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
        return (monopolyCards.size() > 0);
    }

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
        return (monumentCards.size() > 0);
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
        return (roadBuildCards.size() > 0);
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
            for (int i = 0; i < DevCards.get("monopoly").getAsInt(); i++) {
                monopolyCards.add(new MonopolyCard());
            }
            for (int i = 0; i < DevCards.get("monument").getAsInt(); i++) {
                monumentCards.add(new MonumentCard());
            }
            for (int i = 0; i < DevCards.get("roadBuilding").getAsInt(); i++) {
                roadBuildCards.add(new RoadBuildCard());
            }
            for (int i = 0; i < DevCards.get("soldier").getAsInt(); i++) {
                soldierCards.add(new SoldierCard());
            }
            for (int i = 0; i < DevCards.get("yearOfPlenty").getAsInt(); i++) {
                yearOfPlentyCards.add(new YearOfPlentyCard());
            }
        }
    }

    /**
     * Removes one DevelopmentCard from the DevelopmentCardBank and returns it
     * @pre getOwner() instanceof Game, not Player
     * @pre size() > 0
     *
     * @post returned DevelopmentCard is no longer in bank
     * @post new size() == old size() - 1
     *
     * @return A DevelopmentCard from the DevelopmentCardBank
     */
    @Override
    public DevelopmentCard draw() throws Exception {
        if (!ownedByGame) {
            throw new Exception("Can't call draw on DevelopmentCardBank owned by Player!");
        } else {
            return developmentCards.remove(0);
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

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("monopoly", getNumberOfMonopolies());
        json.addProperty("monument", getNumberOfMonuments());
        json.addProperty("roadBuilding", getNumberOfRoadBuilds());
        json.addProperty("soldier", getNumberOfSoldiers());
        json.addProperty("yearOfPlenty", getNumberOfYearOfPlenty());
        return json;
    }

    @Override
    public JsonObject newCardsToJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("monopoly", newMonopolyCards.size());
        json.addProperty("monument", newMonumentCards.size());
        json.addProperty("roadBuilding", newRoadBuildCards.size());
        json.addProperty("soldier", newSoldierCards.size());
        json.addProperty("yearOfPlenty", newYearOfPlentyCards.size());
        return json;
    }



}
