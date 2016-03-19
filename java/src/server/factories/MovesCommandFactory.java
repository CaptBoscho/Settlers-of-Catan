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

    public ICommand createCommand(IDTO dto) {
        if (dto instanceof FinishTurnDTO) {
            // do finish turn stuff
        } else if (dto instanceof SendChatDTO) {

        } else if (dto instanceof RollNumberDTO) {
            RollNumberDTO roll = (RollNumberDTO)dto;
            return new RollNumberCommand(facade, roll);
        } else if (dto instanceof RobPlayerDTO) {

        } else if (dto instanceof BuyDevCardDTO) {

        } else if (dto instanceof PlayYOPCardDTO) {

        } else if (dto instanceof RoadBuildingDTO) {

        } else if (dto instanceof PlayMonopolyDTO) {

        } else if (dto instanceof PlaySoldierCardDTO) {

        } else if (dto instanceof PlayMonumentDTO) {

        } else if (dto instanceof BuildRoadDTO) {

        } else if (dto instanceof BuildCityDTO) {

        } else if (dto instanceof OfferTradeDTO) {
            return new OfferTradeCommand((OfferTradeDTO)dto,facade);
        } else if (dto instanceof TradeOfferResponseDTO) {

        } else if (dto instanceof MaritimeTradeDTO) {
            return new MaritimeTradeCommand((MaritimeTradeDTO)dto,facade);
        } else if (dto instanceof DiscardCardsDTO) {

        }

        return null;
    }
}
