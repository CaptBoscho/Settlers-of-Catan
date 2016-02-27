package client.maritime;

import client.facade.Facade;
import client.services.IServer;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import client.services.UserCookie;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.dto.MaritimeTradeDTO;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kyle 'TMD' Cornelison on 2/20/2016.
 *
 * Base class for MaritimeTrade Controller State
 */
public class MaritimeTradeControllerState {
    private final static int RESOURCE_PORT_TRADE = 2;
    private final static int GENERIC_PORT_TRADE = 3;
    private final static int DEFAULT_TRADE = 4;

    private IMaritimeTradeView view;
    private IMaritimeTradeOverlay overlay;
    private ResourceType getResource;
    private ResourceType giveResource;
    private IServer server;
    private Facade facade;
    private UserCookie userCookie;

    /**
     * Constructor
     * @param overlay Overlay for maritime trading
     */
    public MaritimeTradeControllerState(IMaritimeTradeView view, IMaritimeTradeOverlay overlay){
        this.view = view;
        this.overlay = overlay;

        this.userCookie = UserCookie.getInstance();
        this.server = ServerProxy.getInstance();
        this.facade = Facade.getInstance();
        this.getResource = null;
        this.giveResource = null;
    }

    public void initFromModel(){
        int index = userCookie.getPlayerIndex();

        //Enable/Disable button based on user's turn

        if(facade.getCurrentTurn() == index){
            view.enableMaritimeTrade(true);
        }
        else{
            view.enableMaritimeTrade(false);
        }
    }

    /**
     * Start a new maritime trade
     */
    public void startTrade() {
        int pIndex = userCookie.getPlayerIndex();

        //Setting up give options
        Map<ResourceType, Integer> give = null;
        try {
            give = facade.getPlayerResources(pIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        List<ResourceType> giveOp = new ArrayList<>();

        for(Map.Entry<ResourceType, Integer> entry : give.entrySet()){
            int ratio = 0;

            try {
                ratio = getTradeRatio(entry.getKey());
            } catch (InvalidPlayerException e) {
                ratio = DEFAULT_TRADE;
            }
            System.out.println("count: " + entry.getValue() + " ratio: " + ratio);
            if(entry.getValue() >= ratio){
                giveOp.add(entry.getKey());
            }
        }

        ResourceType[] giveOptions = giveOp.toArray(new ResourceType[giveOp.size()]);

        overlay.setTradeEnabled(false);
        overlay.setCancelEnabled(true);
        overlay.showGiveOptions(giveOptions);
        overlay.showModal();
    }

    /**
     * Make (finalize) an existing maritime trade
     */
    public void makeTrade() {
        int index = userCookie.getPlayerIndex();

        try {
            server.maritimeTrade(new MaritimeTradeDTO(index, getTradeRatio(),
                    getResource.toString(), giveResource.toString()));
        } catch (MissingUserCookieException e) {
            this.overlay.setStateMessage("Trade failed");
        } catch (InvalidPlayerException e) {
            this.overlay.setStateMessage("Trade failed");
        }

        overlay.closeModal();
    }

    /**
     * Cancel an existing maritime trade
     */
    public void cancelTrade() {
        unsetGetValue();
        unsetGiveValue();
        overlay.reset();
        overlay.closeModal();
    }

    /**
     * Sets the resource type to get
     * @param resource Type of resource to trade for
     */
    public void setGetResource(ResourceType resource) {
        this.getResource = resource;
        overlay.selectGetOption(resource, 1);
        overlay.setTradeEnabled(true);
    }

    /**
     * Sets the resource type to give
     * @param resource Type of resource to trade away
     */
    public void setGiveResource(ResourceType resource) {
        this.giveResource = resource;
        try {
            overlay.selectGiveOption(resource, getTradeRatio());
        } catch (InvalidPlayerException e) {
            overlay.selectGetOption(resource, DEFAULT_TRADE);
        }

        //Setting up get options
        Map<ResourceType, Integer> get = facade.getBankResources();

        List<ResourceType> getOp = new ArrayList<>();

        for(Map.Entry<ResourceType, Integer> entry : get.entrySet()){
            if(entry.getValue() > 0){
                getOp.add(entry.getKey());
            }
        }

        ResourceType[] getOptions = getOp.toArray(new ResourceType[getOp.size()]);

        overlay.showGetOptions(getOptions);
    }

    /**
     * Unset the get resource type
     */
    public void unsetGetValue() {
        this.getResource = null;
        overlay.reset();
    }

    /**
     * Unset the give resource type
     */
    public void unsetGiveValue() {
        this.giveResource = null;
        overlay.reset();
    }

    /**
     * Update - used when controller is notified of model changes
     */
    public void update() {
        initFromModel();
    }

    //Helper Methods
    private int getTradeRatio(ResourceType type) throws InvalidPlayerException {
        int index = userCookie.getPlayerIndex();
        Set<PortType> ports = facade.maritimeTradeOptions(index);

        //Check for Resource Ports
        for (PortType port : ports) {
            if(port.toString().equals(type.toString())){
                return RESOURCE_PORT_TRADE;
            }
        }

        //Check for 3-1 Ports
        for (PortType port : ports) {
            if(port == PortType.THREE){
                return GENERIC_PORT_TRADE;
            }
        }

        //Last use default of 4-1
        return DEFAULT_TRADE;
    }

    private int getTradeRatio() throws InvalidPlayerException {
        return getTradeRatio(giveResource);
    }
}
