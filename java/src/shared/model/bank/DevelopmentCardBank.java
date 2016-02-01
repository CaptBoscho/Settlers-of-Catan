package shared.model.bank;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.devcards.*;

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
                    soldierCards.add((SoldierCard) cardToAdd);
                    break;
                case MONUMENT:
                    monumentCards.add((MonumentCard) cardToAdd);
                    break;
                case MONOPOLY:
                    monopolyCards.add((MonopolyCard) cardToAdd);
                    break;
                case YEAR_OF_PLENTY:
                    yearOfPlentyCards.add((YearOfPlentyCard) cardToAdd);
                    break;
                case ROAD_BUILD:
                    roadBuildCards.add((RoadBuildCard) cardToAdd);
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
    public void useYearOfPlenty() {
        yearOfPlentyCards.remove(0);
    }

    @Override
    public boolean canUseSoldier() {
        return (soldierCards.size() > 0);
    }

    @Override
    public void useSoldier() {
        soldierCards.remove(0);
    }

    @Override
    public boolean canUseMonopoly() {
        return (monopolyCards.size() > 0);
    }

    @Override
    public void useMonopoly() {
        monopolyCards.remove(0);
    }

    @Override
    public boolean canUseMonument() {
        return (monumentCards.size() > 0);
    }

    @Override
    public void useMonument() {
        monumentCards.remove(0);
    }

    @Override
    public boolean canUseRoadBuild() {
        return (roadBuildCards.size() > 0);
    }

    @Override
    public void useRoadBuild() {
        roadBuildCards.remove(0);
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

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
