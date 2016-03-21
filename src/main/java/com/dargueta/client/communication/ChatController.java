package com.dargueta.client.communication;

import com.dargueta.client.facade.Facade;
import com.dargueta.client.services.UserCookie;
import com.dargueta.shared.definitions.CatanColor;
import com.dargueta.shared.model.game.MessageLine;

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
        for(MessageLine line : chat) {
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
        if(!message.equals(null)) {
            facade.sendChat(userCookie.getPlayerIndex(), message);
        }
	}

	@Override
	public void update(Observable o, Object arg) {
		initFromModel();
	}
}
