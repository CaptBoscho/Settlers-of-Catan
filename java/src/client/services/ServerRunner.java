package client.services;

import shared.definitions.CatanColor;
import shared.dto.*;

/**
 * A playground area to manually test service code
 *
 * @author Derek Argueta
 */
public class ServerRunner {

    public static void main(String[] args) throws MissingUserCookieException {
//        AuthDTO dto = new AuthDTO("totallyuniquserhere", "yee");
        IServer server = new ServerProxy("localhost", 8081);
//        System.out.println(server.getAllGames());

        System.out.println("------------------------------------ AUTH ------------------------------------");
        AuthDTO dto = new AuthDTO("Sam", "sam");
        System.out.println(server.authenticateUser(dto));

        System.out.println("------------------------------------ CREATE GAME ------------------------------------");
        CreateGameDTO cdto = new CreateGameDTO(false, false, false, "my test game yooo");
        System.out.println(server.createNewGame(cdto));

        System.out.println("------------------------------------ GET GAMES ------------------------------------");
        System.out.println(server.getAllGames());

        System.out.println("------------------------------------ JOIN GAME ------------------------------------");
        JoinGameDTO jdto = new JoinGameDTO(4, CatanColor.WHITE);
        System.out.println(server.joinGame(jdto));

        System.out.println("------------------------------------ SEND CHAT ------------------------------------");
        SendChatDTO sdto = new SendChatDTO(0, "hello world");
        server.sendChat(sdto);



//        SaveGameDTO dto = new SaveGameDTO(3, "lol");
//        server.saveGame(dto);

//        LoadGameDTO dto = new LoadGameDTO("lol");
//        System.out.println(server.loadGame(dto));


//        JoinGameDTO dto = new JoinGameDTO(4, CatanColor.BROWN);
//        server.joinGame(dto);

    }
}