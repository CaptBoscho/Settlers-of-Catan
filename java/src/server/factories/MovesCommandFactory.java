package server.factories;

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

    public void bind(IFacade newFacade){
        facade = newFacade;
    }

    /**
     * Creates a Moves command based on a given string
     * @return an ICommand object
     */

    public ICommand createCommand(IDTO dto) {
        if (dto instanceof FinishTurnDTO) {

        } else if (dto instanceof SendChatDTO) {

        } else if (dto instanceof RollNumberDTO) {

        } else if (dto instanceof RobPlayerDTO) {
            return new RobPlayerCommand(facade, (RobPlayerDTO)dto);
        } else if (dto instanceof BuyDevCardDTO) {
            return new BuyDevCardCommand((BuyDevCardDTO)dto, facade);
        } else if (dto instanceof PlayYOPCardDTO) {

        } else if (dto instanceof RoadBuildingDTO) {
            return new RoadBuildingCommand(facade, (RoadBuildingDTO)dto);
        } else if (dto instanceof PlayMonopolyDTO) {

        } else if (dto instanceof PlaySoldierCardDTO) {
            return new SoldierCommand(facade, (PlaySoldierCardDTO) dto);
        } else if (dto instanceof PlayMonumentDTO) {
            return new MonumentCommand((PlayMonumentDTO)dto, facade);
        } else if (dto instanceof BuildRoadDTO) {
            return new BuildRoadCommand(facade, (BuildRoadDTO)dto);
        } else if(dto instanceof BuildSettlementDTO) {
            return new BuildSettlementCommand(facade, (BuildSettlementDTO)dto);
        } else if (dto instanceof BuildCityDTO) {
            return new BuildCityCommand(facade, (BuildCityDTO)dto);
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
