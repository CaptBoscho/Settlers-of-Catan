package server.factories;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.commands.moves.*;
import server.facade.IFacade;
import shared.dto.*;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 */
public class MovesCommandFactory {

    private IFacade facade;
    private static MovesCommandFactory instance = null;
    private MovesCommandFactory() {

    }

    public static MovesCommandFactory getInstance() {
        if (instance == null) {
            instance = new MovesCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }

    /**
     * Creates a Moves command based on a given string
     * @return an ICommand object
     */
    public ICommand createCommand(Object obj) {
        if (obj instanceof FinishTurnDTO) {
            // do finish turn stuff
        } else if (obj instanceof SendChatDTO) {

        } else if (obj instanceof RollNumberDTO) {

        } else if (obj instanceof RobPlayerDTO) {

        } else if (obj instanceof BuyDevCardDTO) {

        } else if (obj instanceof PlayYOPCardDTO) {

        } else if (obj instanceof RoadBuildingDTO) {

        } else if (obj instanceof PlayMonopolyDTO) {

        } else if (obj instanceof PlaySoldierCardDTO) {

        } else if (obj instanceof PlayMonumentDTO) {

        } else if (obj instanceof BuildRoadDTO) {

        } else if (obj instanceof BuildCityDTO) {

        } else if (obj instanceof OfferTradeDTO) {
            //return new OfferTradeCommand(obj,facade);
        } else if (obj instanceof TradeOfferResponseDTO) {

        } else if (obj instanceof MaritimeTradeDTO) {

        } else if (obj instanceof DiscardCardsDTO) {

        }

        return null;
    }
}
