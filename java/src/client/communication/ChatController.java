package client.communication;

import client.base.*;
import client.facade.Facade;
import client.services.UserCookie;
import shared.definitions.CatanColor;
import shared.model.game.MessageLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {

    private Facade facade;
    private UserCookie userCookie;

	public ChatController(IChatView view) {
		super(view);
		facade = Facade.getInstance();
        userCookie = UserCookie.getInstance();
        facade.addObserver(this);
		initFromModel();
	}

    private void initFromModel() {
        List<MessageLine> chat = facade.getChat().getMessages();
        List<LogEntry> entries = new ArrayList<>();
        for (MessageLine line : chat) {
            CatanColor color = facade.getPlayerColorByName(line.getPlayer());
            entries.add(new LogEntry(color, line.getMessage()));
        }
        getView().setEntries(entries);
    }

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
        if (message != null) {
            facade.sendChat(userCookie.getPlayerIndex(), message);
        }
	}

	@Override
	public void update(Observable o, Object arg) {
		initFromModel();
	}
}
