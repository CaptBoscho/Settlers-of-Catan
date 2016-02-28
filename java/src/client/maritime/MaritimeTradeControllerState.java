package client.maritime;

import client.facade.BuildException;
import client.facade.Facade;
import client.services.IServer;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import client.services.UserCookie;
import org.apache.http.client.cache.Resource;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.dto.MaritimeTradeDTO;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;
import shared.model.bank.InvalidTypeException;

import javax.naming.InsufficientResourcesException;
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
        ResourceType[] giveOptions = buildGiveOptions();

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
            facade.maritimeTrade(index, getTradeRatio(), getResource, giveResource);
        } catch (InvalidPlayerException e) {
            e.printStackTrace();
        }

        overlay.closeModal();
        overlay.reset();
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

        ResourceType[] getOptions = buildGetOptions();
        overlay.showGetOptions(getOptions);
    }

    /**
     * Unset the get resource type
     */
    public void unsetGetValue() {
        this.getResource = null;
        ResourceType[] getOptions = buildGetOptions();

        overlay.showGetOptions(getOptions);
        overlay.setTradeEnabled(false);
    }

    /**
     * Unset the give resource type
     */
    public void unsetGiveValue() {
        this.giveResource = null;
        ResourceType[] giveOptions = buildGiveOptions();

        overlay.hideGetOptions();
        overlay.showGiveOptions(giveOptions);
        overlay.setTradeEnabled(false);

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

    private ResourceType[] buildGiveOptions(){
        //Get the player index
        int pIndex = userCookie.getPlayerIndex();

        //Get the player's resources (Type and Count)
        Map<ResourceType, Integer> give = null;
        try {
            give = facade.getPlayerResources(pIndex);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }

        //List to hold resource types that can be traded away
        List<ResourceType> giveOptions = new ArrayList<>();
        //Calculate which resources can be traded away
        for(Map.Entry<ResourceType, Integer> entry : give.entrySet()){
            //Init ratio
            int ratio = 0;

            //Calculate the trade ratio
            try {
                ratio = getTradeRatio(entry.getKey());
            } catch (InvalidPlayerException e) {
                ratio = DEFAULT_TRADE;
            }

            //If the number of resources (for the type) is >= trade ratio
            //then add the resource type to the list of resources that can be traded away
            if(entry.getValue() >= ratio){
                giveOptions.add(entry.getKey());
            }
        }

        //Convert the list to an array to be sent to the overlay
        return giveOptions.toArray(new ResourceType[giveOptions.size()]);
    }

    private ResourceType[] buildGetOptions(){
        //Get the bank's resources (Type and Count)
        Map<ResourceType, Integer> get = facade.getBankResources();

        //List to hold the resource types that can be traded for
        List<ResourceType> getOptions = new ArrayList<>();
        //Calculate which resources can be traded for
        for(Map.Entry<ResourceType, Integer> entry : get.entrySet()){
            if(entry.getValue() > 0){
                getOptions.add(entry.getKey());
            }
        }

        //Convert the list to an array to be sent to the overlay
        return getOptions.toArray(new ResourceType[getOptions.size()]);
    }
}
