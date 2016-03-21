package client.main;

import javax.swing.*;

import client.services.ServerProxy;

/**
 * Main entry point for the Catan program
 */
@SuppressWarnings("serial")
public class Catan extends JFrame {

	public Catan() {
		client.base.OverlayView.setWindow(this);
		
		this.setTitle("Settlers of Catan");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final CatanPanel catanPanel = new CatanPanel();
		this.setContentPane(catanPanel);
		
		display();
	}
	
	private void display() {
		pack();
		setVisible(true);
	}
	
	//
	// Main
	//
	
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

        if(args.length == 2) {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            ServerProxy.getInstance().configure(host, port);
        }
		
		SwingUtilities.invokeLater(() -> {
            new Catan();

            final PlayerWaitingView playerWaitingView = new PlayerWaitingView();
            final PlayerWaitingController playerWaitingController = new PlayerWaitingController(playerWaitingView);
            playerWaitingView.setController(playerWaitingController);

            final JoinGameView joinView = new JoinGameView();
            final NewGameView newGameView = new NewGameView();
            final SelectColorView selectColorView = new SelectColorView();
            MessageView joinMessageView = new MessageView();
            final JoinGameController joinController = new JoinGameController(
                                                                             joinView,
                                                                             newGameView,
                                                                             selectColorView,
                                                                             joinMessageView);
            joinController.setJoinAction(playerWaitingController::start);
            joinView.setController(joinController);
            newGameView.setController(joinController);
            selectColorView.setController(joinController);
            joinMessageView.setController(joinController);

            final LoginView loginView = new LoginView();
            final MessageView loginMessageView = new MessageView();
            final LoginController loginController = new LoginController(loginView, loginMessageView);
            loginController.setLoginAction(joinController::start);
            loginView.setController(loginController);

            loginController.start();
        });
	}
}
