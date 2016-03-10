package server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Derek Argueta
 */
@RestController
public class GamesController {

    @RequestMapping("/games/list")
    public void listGames() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/games/create")
    public void createGame() {

    }

    @RequestMapping(method=RequestMethod.POST, path="/games/join")
    public void joinGame() {

    }
}
