package client.services;

import shared.dto.AuthDTO;

/**
 * A playground area to manually test service code
 *
 * @author Derek Argueta
 */
public class ServerRunner {

    public static void main(String[] args) {
        AuthDTO dto = new AuthDTO("totallyuniquserhere", "yee");
        IServer server = new ServerProxy("localhost", 8081);
        System.out.println(server.authenticateUser(dto));
    }
}
