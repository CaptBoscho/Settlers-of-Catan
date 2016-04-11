package client.maritime.states;

import client.maritime.IMaritimeTradeOverlay;
import client.maritime.IMaritimeTradeView;
import client.maritime.MaritimeTradeControllerState;
import shared.definitions.ResourceType;

/**
 * Created by Kyle 'TMD' Cornelison on 2/18/2016.
 */
public final class PlayingState extends MaritimeTradeControllerState {

    /**
     * Constructor
     */
    public PlayingState(IMaritimeTradeView view, IMaritimeTradeOverlay overlay){
        super(view, overlay);
    }

    @Override
    public void initFromModel(){
        super.initFromModel();
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
        super.update();
    }
}
