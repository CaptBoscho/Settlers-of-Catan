package shared.model.bank;

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
}
