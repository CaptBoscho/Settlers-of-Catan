package shared.model.map;
import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 *
 * Representation of a Hex in the map. The map is comprised of hex pieces. Each hex has a chit that represents the
 * probability of that hex getting rolled. A hex can only have a resource type. All structures/players belong on
 * edge pieces, not hexes.
 */
public class Hex {

    private HexLocation loc;
    private HexType type;
    private int chit;

    public Hex(HexLocation l, HexType t, int c){
        loc = l;
        type = t;
        chit = c;
    }

    public void setType(HexType t)
    {
        type = t;
    }

    public void setChit(int c)
    {
        chit = c;
    }

    public HexLocation getLocation()
    {
        return loc;
    }

    public HexType getType()
    {
        return type;
    }

    public int getChit()
    {
        return chit;
    }
}
