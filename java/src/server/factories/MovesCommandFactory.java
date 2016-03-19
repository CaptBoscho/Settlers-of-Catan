package server.factories;

import server.commands.ICommand;
import server.commands.moves.*;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.*;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 */
public class MovesCommandFactory {

    private IFacade facade;
    private static MovesCommandFactory instance = null;
    private MovesCommandFactory() {
        facade = ServerFacade.getInstance();
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
            return new FinishTurnCommand(facade, (FinishTurnDTO)dto);
        } else if (dto instanceof SendChatDTO) {
            return new SendChatCommand(facade, (SendChatDTO)dto);
        } else if (dto instanceof RollNumberDTO) {
            RollNumberDTO roll = (RollNumberDTO)dto;
            return new RollNumberCommand(facade, roll);
        } else if (dto instanceof RobPlayerDTO) {
            return new RobPlayerCommand(facade, (RobPlayerDTO)dto);
        } else if (dto instanceof BuyDevCardDTO) {
            return new BuyDevCardCommand((BuyDevCardDTO)dto, facade);
        } else if (dto instanceof PlayYOPCardDTO) {
            return new YearOfPlentyCommand((PlayYOPCardDTO)dto, facade);
        } else if (dto instanceof RoadBuildingDTO) {
            return new RoadBuildingCommand(facade, (RoadBuildingDTO)dto);
        } else if (dto instanceof PlayMonopolyDTO) {
            return new MonopolyCommand((PlayMonopolyDTO)dto, facade);
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
            return new AcceptTradeCommand((TradeOfferResponseDTO)dto, facade);
        } else if (dto instanceof MaritimeTradeDTO) {
            return new MaritimeTradeCommand((MaritimeTradeDTO)dto,facade);
        } else if (dto instanceof DiscardCardsDTO) {
            return new DiscardCardsCommand((DiscardCardsDTO)dto, facade);
        }

        return null;
    }
}
