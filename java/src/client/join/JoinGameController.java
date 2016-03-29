package client.join;

import client.facade.Facade;
import client.services.ServerProxy;
import client.services.UserCookie;
import shared.definitions.CatanColor;
import client.base.*;
import client.data.*;
import client.misc.*;
import shared.dto.CreateGameDTO;
import shared.dto.JoinGameDTO;

import java.util.List;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private GameInfo currentGame;
	
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
							  ISelectColorView selectColorView,
							  IMessageView messageView) {
		super(view);

		this.setNewGameView(newGameView);
		this.setSelectColorView(selectColorView);
		this.setMessageView(messageView);
	}
	
	private IJoinGameView getJoinGameView() {
		return (IJoinGameView)super.getView();
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(final IAction value) {
		joinAction = value;
	}
	
	private INewGameView getNewGameView() {
		return newGameView;
	}

	private void setNewGameView(INewGameView newGameView) {
		this.newGameView = newGameView;
	}
	
	private ISelectColorView getSelectColorView() {
		return selectColorView;
	}

	private void setSelectColorView(ISelectColorView selectColorView) {
		this.selectColorView = selectColorView;
	}

	private void setMessageView(IMessageView messageView) {
		this.messageView = messageView;
	}

	@Override
	public void start() {
		List<GameInfo> allGames = ServerProxy.getInstance().getAllGames();
		GameInfo[] gamesArray = new GameInfo[allGames.size()];
		allGames.toArray(gamesArray);
		this.getJoinGameView().setGames(gamesArray, UserCookie.getInstance().getPlayerInfo());
		this.setNewGameView(newGameView);

		this.getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		getNewGameView().closeModal();
	}

	@Override
	public void createNewGame() {
        final boolean randomHexes = getNewGameView().getRandomlyPlaceHexes();
        final boolean randomNumbers = getNewGameView().getRandomlyPlaceNumbers();
        final boolean randomPorts = getNewGameView().getUseRandomPorts();
        final String gameName = getNewGameView().getTitle();
        final CreateGameDTO createGameDTO = new CreateGameDTO(randomHexes, randomNumbers, randomPorts, gameName);
        final GameInfo newGame = ServerProxy.getInstance().createNewGame(createGameDTO);
		this.getNewGameView().closeModal();

		JoinGameDTO joinGameDTO = new JoinGameDTO(newGame.getId(), CatanColor.WHITE);
		ServerProxy.getInstance().joinGame(joinGameDTO);

        this.start();
	}

	@Override
	public void startJoinGame(GameInfo game) {
		assert game != null;

		game.getPlayers().stream().filter(p -> p.getId() != UserCookie.getInstance().getPlayerId()).forEach(p -> {
			this.getSelectColorView().setColorEnabled(p.getColor(), false);
		});
		currentGame = game;
        Facade.getInstance().setGameInfo(game);
        this.getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		assert color != null;

		// make sure color hasn't been taken
		boolean colorTaken = false;
		final List<GameInfo> games = ServerProxy.getInstance().getAllGames();
		for (final GameInfo game : games) {
			if (game.getId() == currentGame.getId()) {
				for (final PlayerInfo player : game.getPlayers()) {
					if (player.getId() != UserCookie.getInstance().getPlayerId() && player.getColor().equals(color)) {
						game.getPlayers().stream().filter(p -> p.getId() != UserCookie.getInstance().getPlayerId()).forEach(p -> {
							this.getSelectColorView().setColorEnabled(p.getColor(), false);
						});
						final MessageView msg = new MessageView();
						msg.setTitle("Bad Color");
						msg.setMessage("Color has already been chosen by another player.");
						msg.showModal();
						colorTaken = true;
					}
				}
			}
		}

		if(colorTaken) return;

        final JoinGameDTO dto = new JoinGameDTO(Facade.getInstance().getGameId(), color);
		ServerProxy.getInstance().joinGame(dto);

        // TODO - create/update game instance

		// If join succeeded
		this.getSelectColorView().closeModal();
		this.getJoinGameView().closeModal();
		joinAction.execute();
	}
}
