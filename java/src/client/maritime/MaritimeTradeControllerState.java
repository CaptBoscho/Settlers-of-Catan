package client.maritime;

import client.facade.Facade;
import client.services.IServer;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import client.services.UserCookie;
import shared.definitions.ResourceType;
import shared.dto.MaritimeTradeDTO;

/**
 * Created by Kyle 'TMD' Cornelison on 2/20/2016.
 *
 * Base class for MaritimeTrade Controller State
 */
public class MaritimeTradeControllerState {
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
        if(facade.getCurrentTurn()== userCookie.getPlayerId()){
            view.enableMaritimeTrade(true);
        }
        else{
            view.enableMaritimeTrade(false);
        }
    }

    /**
     * Start a new maritime trade
     */
    public void startTrade() {}

    /**
     * Make (finalize) an existing maritime trade
     */
    public void makeTrade() {
        try {
            server.maritimeTrade(new MaritimeTradeDTO(userCookie.getPlayerId(), 4,
                    getResource.toString(), giveResource.toString()));
        } catch (MissingUserCookieException e) {
            this.overlay.setStateMessage("Trade failed");
        }
    }

    /**
     * Cancel an existing maritime trade
     */
    public void cancelTrade() {
        unsetGetValue();
        unsetGiveValue();
    }

    /**
     * Sets the resource type to get
     * @param resource Type of resource to trade for
     */
    public void setGetResource(ResourceType resource) {
        this.getResource = resource;
    }

    /**
     * Sets the resource type to give
     * @param resource Type of resource to trade away
     */
    public void setGiveResource(ResourceType resource) {
        this.giveResource = resource;
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
}
