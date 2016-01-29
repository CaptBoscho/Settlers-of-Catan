package shared.model.bank;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.model.JsonSerializable;
import shared.model.devcards.*;
import shared.model.game.Game;
import shared.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A bank owned by either a Player or a game which holds all the owners DevelopmentCards
 *
 * @author Danny Harding
 */
public class DevelopmentCardBank implements JsonSerializable {
    static final int SOLDIER_CARDS = 14;
    static final int MONUMENT_CARDS = 5;
    static final int MONOPOLY_CARDS = 2;
    static final int YEAR_OF_PLENTY_CARDS = 2;
    static final int ROAD_BUILD_CARDS = 2;

    final Object OWNER;
    private int numberSoldierCards;

    private ArrayList<DevelopmentCard> developmentCards;

    /**
     * Creates a full DevelopmentCardBank, shuffled
     *
     * @param game The object that contains the DevelopmentCardBank
     */
    public DevelopmentCardBank(Game game) {
        OWNER = game;
        addSoldierCards();
        addMonumentCards();
        addMonopolyCards();
        addYearOfPlentyCards();
        addRoadBuildCards();
        Collections.shuffle(developmentCards);
    }

    /**
     * Creates an empty DevelopmentCardBank
     * @param player The object that contains the DevelopmentCardBank
     */
    public DevelopmentCardBank(Player player) {
        OWNER = player;
        numberSoldierCards = 0;
    }

    /**
     * Construct a DevelopmentCardBank object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public DevelopmentCardBank(JsonObject json) {
        //deserialize blah blah blah

        OWNER = null;
    }

    private void addSoldierCards() {
        numberSoldierCards = 0;
        for (int i = 0; i < SOLDIER_CARDS; i++) {
            this.addDevCard(new SoldierCard());
        }
    }

    private void addMonumentCards() {
        for (int i = 0; i < MONUMENT_CARDS; i++) {
            this.addDevCard(new MonumentCard());
        }
    }

    private void addMonopolyCards() {
        for (int i = 0; i < MONOPOLY_CARDS; i++) {
            this.addDevCard(new MonopolyCard());
        }
    }

    private void addYearOfPlentyCards() {
        for (int i = 0; i < YEAR_OF_PLENTY_CARDS; i++) {
            this.addDevCard(new YearOfPlentyCard());
        }
    }

    private void addRoadBuildCards() {
        for (int i = 0; i < ROAD_BUILD_CARDS; i++) {
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
    public void addDevCard(DevelopmentCard cardToAdd) {
        developmentCards.add(cardToAdd);
        if (cardToAdd.getType() == DevCardType.SOLDIER) {
            numberSoldierCards++;
        }
    }

    public int getNumberSoldierCards() {
        return numberSoldierCards;
    }

    /**
     * @return the number of developmentCards in the bank
     */
    public int size() {
        return developmentCards.size();
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
    public DevelopmentCard draw() throws Exception {
        if (!(OWNER instanceof Game)) {
            throw new Exception("Can't call draw on DevelopmentCardBank owned by Player!");
        } else {
            DevelopmentCard developmentCard = developmentCards.remove(0);

            if (developmentCard.getType() == DevCardType.SOLDIER) {
                numberSoldierCards--;
            }
            return developmentCard;
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
