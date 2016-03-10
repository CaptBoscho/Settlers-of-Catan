package server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Derek Argueta
 */
@RestController
public class GameController {

    @RequestMapping("/game/model")
    public void getModel() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/game/addAI")
    public void addAI() {

    }

    @RequestMapping("game/listAI")
    public void listAI() {

    }
}
