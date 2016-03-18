package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.OfferTradeException;
import server.facade.IFacade;
import shared.definitions.ResourceType;
import shared.dto.IDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * A command object that offers a trade
 *
 * @author Joel Bradley
 */
public class OfferTradeCommand implements ICommand {
    private int sender;
    private int receiver;
    private int brick;
    private int wood;
    private int sheep;
    private int wheat;
    private int ore;
    private IFacade facade;
    /**
     * Constructor
     */
    public OfferTradeCommand(int sender, int receiver, int brick, int wood, int sheep, int wheat, int ore, IFacade fac) {
        this.sender = sender;
        this.receiver = receiver;
        this.brick = brick;
        this.wood = wood;
        this.sheep = sheep;
        this.wheat = wheat;
        this.ore = ore;
        facade = fac;
    }

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        List<ResourceType> send = new ArrayList<>();
        List<ResourceType> receive = new ArrayList<>();
        if(brick>=0){
            for(int i=0; i<brick; i++){
                send.add(ResourceType.BRICK);
            }
        }else{
            brick = -brick;
            for(int i=0; i<brick; i++){
                receive.add(ResourceType.BRICK);
            }
        }

        if(wood>=0){
            for(int i=0; i<wood; i++){
                send.add(ResourceType.WOOD);
            }
        }else{
            wood = -wood;
            for(int i=0; i<wood; i++){
                receive.add(ResourceType.WOOD);
            }
        }

        if(sheep>=0){
            for(int i=0; i<sheep; i++){
                send.add(ResourceType.SHEEP);
            }
        }else{
            sheep = -sheep;
            for(int i=0; i<sheep; i++){
                receive.add(ResourceType.SHEEP);
            }
        }

        if(wheat>=0){
            for(int i=0; i<wheat; i++){
                send.add(ResourceType.WHEAT);
            }
        }else{
            wheat = -wheat;
            for(int i=0; i<wheat; i++){
                receive.add(ResourceType.WHEAT);
            }
        }

        if(ore>=0){
            for(int i=0; i<ore; i++){
                send.add(ResourceType.ORE);
            }
        }else{
            ore = -ore;
            for(int i=0; i<ore; i++){
                receive.add(ResourceType.ORE);
            }
        }

        try {
            facade.offerTrade(1, sender, receiver, send, receive);
        }catch(OfferTradeException e){

        }
        return null;
    }

}
