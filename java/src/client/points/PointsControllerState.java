package client.points;

import client.facade.Facade;

/**
 * Created by Kyle 'TMD' Cornelison on 2/19/2016.
 *
 * Base class for PointsController States
 */
public class PointsControllerState {
    private Facade facade;

    /**
     * Constructor
     */
    public PointsControllerState(){
        facade = Facade.getInstance();
    }

    public void initFromModel(){}

    public void update(){}
}
