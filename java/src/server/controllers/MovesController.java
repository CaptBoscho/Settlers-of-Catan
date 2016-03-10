package server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Derek Argueta
 */
@RestController
public class MovesController {

    @RequestMapping(method=RequestMethod.POST, path="/moves/sendChat")
    public void sendChat() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/rollNumber")
    public void rollNumber() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/robPlayer")
    public void robPlayer() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/finishTurn")
    public void finishTurn() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/buyDevCard")
    public void buyDevCard() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/Year_of_Plenty")
    public void yearOfPlenty() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/Road_Building")
    public void roadBuilding() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/Soldier")
    public void soldier() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/Monopoly")
    public void monopoly() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/Monument")
    public void monument() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/buildRoad")
    public void buildRoad() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/buildSettlement")
    public void buildSettlement() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/buildCity")
    public void buildCity() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/offerTrade")
    public void offerTrade() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/acceptTrade")
    public void acceptTrade() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/maritimeTrade")
    public void maritimeTrade() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/moves/discardCards")
    public void discardCards() {
        
    }
}
