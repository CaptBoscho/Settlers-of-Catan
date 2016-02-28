package client.join;

import client.facade.Facade;
import client.services.Poller;
import client.services.ServerProxy;
import client.services.UserCookie;
import shared.definitions.CatanColor;
import client.base.*;
import client.data.*;
import client.misc.*;
import shared.dto.CreateGameDTO;
import shared.dto.JoinGameDTO;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private Poller poller;
	
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {
		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(final IAction value) {
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		return selectColorView;
	}

	public void setSelectColorView(ISelectColorView selectColorView) {
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		return messageView;
	}

	public void setMessageView(IMessageView messageView) {
		this.messageView = messageView;
	}

	@Override
	public void start() {
		List<GameInfo> allGames = ServerProxy.getInstance().getAllGames();
		GameInfo[] gamesArray = new GameInfo[allGames.size()];
		allGames.toArray(gamesArray);
		getJoinGameView().setGames(gamesArray, UserCookie.getInstance().getPlayerInfo());
		setNewGameView(newGameView);

		poller = new Poller(ServerProxy.getInstance());
		poller.setPollingFunction(() -> {
			List<GameInfo> games = ServerProxy.getInstance().getAllGames();
			GameInfo[] gamesArr = new GameInfo[games.size()];
			games.toArray(gamesArr);
			getJoinGameView().setGames(gamesArr, UserCookie.getInstance().getPlayerInfo());
			setNewGameView(newGameView);
			return null;
		});
		poller.start();

		getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {
		if(poller != null) poller.stop();
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
		getNewGameView().closeModal();

		JoinGameDTO joinGameDTO = new JoinGameDTO(newGame.getId(), CatanColor.WHITE);
		ServerProxy.getInstance().joinGame(joinGameDTO);

        start();
	}

	@Override
	public void startJoinGame(GameInfo game) {
		game.getPlayers().stream().filter(p -> p.getId() != UserCookie.getInstance().getPlayerId()).forEach(p -> {
			getSelectColorView().setColorEnabled(p.getColor(), false);
		});

        Facade.getInstance().setGameInfo(game);
        getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
		poller.stop();
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
        final JoinGameDTO dto = new JoinGameDTO(Facade.getInstance().getGameId(), color);
        ServerProxy.getInstance().joinGame(dto);

		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}
}
