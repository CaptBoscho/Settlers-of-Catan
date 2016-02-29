package client.maritime.states;

import client.facade.Facade;
import client.maritime.IMaritimeTradeOverlay;
import client.maritime.IMaritimeTradeView;
import client.maritime.MaritimeTradeControllerState;
import client.services.UserCookie;
import shared.definitions.ResourceType;

/**
 * Created by Kyle 'TMD' Cornelison on 2/18/2016.
 */
public class SetupTwoState extends MaritimeTradeControllerState {
    private Facade facade;
    private UserCookie userCookie;
    private IMaritimeTradeView view;
    private IMaritimeTradeOverlay overlay;

    /**
     * Constructor
     */
    public SetupTwoState(IMaritimeTradeView view, IMaritimeTradeOverlay overlay){
        super(view, overlay);
        this.view = view;
        this.overlay = overlay;
        this.facade = Facade.getInstance();
        this.userCookie = UserCookie.getInstance();
    }

    @Override
    public void initFromModel(){
        view.enableMaritimeTrade(false);
    }

    @Override
    public void setGetResource(ResourceType resource) {
        super.setGetResource(resource);
    }

    @Override
    public void setGiveResource(ResourceType resource) {
        super.setGiveResource(resource);
    }

    @Override
    public void unsetGetValue() {
        super.unsetGetValue();
    }

    @Override
    public void unsetGiveValue() {
        super.unsetGiveValue();
    }

    @Override
    public void update() {
        initFromModel();
    }
}
