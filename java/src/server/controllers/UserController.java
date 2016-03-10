package server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Derek Argueta
 */
@RestController
public class UserController {
    // -- TODO

    @RequestMapping(method=RequestMethod.POST, path="/user/login")
    public void login() {

    }

    @RequestMapping("/user/register")
    public void register() {

    }
}
