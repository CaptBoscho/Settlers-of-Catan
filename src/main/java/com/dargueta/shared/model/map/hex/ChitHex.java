package com.dargueta.shared.model.map.hex;

import com.dargueta.shared.definitions.HexType;
import com.dargueta.shared.locations.HexLocation;

/**
 * A ChitHex is a Hex that has a chit
 *
 * @author Joel Bradley
 */
public final class ChitHex extends Hex {

    private int chit;

    /**
     * Default constructor for a ChitHex
     * @param hexLoc HexLocation
     * @param hexType HexType
     * @param chit int
     */
    public ChitHex(HexLocation hexLoc, HexType hexType, int chit) {
        super(hexLoc, hexType);

        assert (chit >= 2 && chit <= 6) || (chit >= 8 && chit <= 12);
        this.chit = chit;
    }

    public int getChit() {
        return chit;
    }

}
