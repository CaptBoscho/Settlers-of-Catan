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

import java.util.ArrayList;
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
        overlay.showGiveOptions(null);
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
        overlay.showGetOptions(null);
    }

    /**
     * Unset the get resource type
     */
    public void unsetGetValue() {
        this.getResource = null;

    }

    /**
     * Unset the give resource type
     */
    public void unsetGiveValue() {
        this.giveResource = null;
    }

    /**
     * Update - used when controller is notified of model changes
     */
    public void update() {
        initFromModel();
    }

    //Helper Methods
    private int getTradeRatio() throws InvalidPlayerException {
        int index = userCookie.getPlayerIndex();
        Set<PortType> ports = facade.maritimeTradeOptions(index);

        //Check for Resource Ports
        for (PortType port : ports) {
            if(port.toString().equals(giveResource.toString())){
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
}
