package client.services;

import shared.definitions.CatanColor;
import shared.dto.*;

/**
 * A playground area to manually test service code
 *
 * @author Derek Argueta
 */
public class ServerRunner {

    public static void main(String[] args) {
//        AuthDTO dto = new AuthDTO("totallyuniquserhere", "yee");
        IServer server = new ServerProxy("localhost", 8081);
//        System.out.println(server.getAllGames());

//        CreateGameDTO dto = new CreateGameDTO(false, false, false, "my tet game yooo");
//        System.out.println(server.createNewGame(dto));

//        JoinGameDTO dto = new JoinGameDTO(4, CatanColor.WHITE);
//        server.joinGame(dto);

//        SaveGameDTO dto = new SaveGameDTO(3, "lol");
//        server.saveGame(dto);

        LoadGameDTO dto = new LoadGameDTO("lol");
        System.out.println(server.loadGame(dto));
    }
}
