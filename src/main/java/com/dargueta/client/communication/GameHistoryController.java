package com.dargueta.client.communication;

import java.util.*;
import java.util.List;

import com.dargueta.client.facade.Facade;
import com.dargueta.shared.model.game.MessageLine;


/**
 * game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {

	private Facade facade;

	public GameHistoryController(final IGameHistoryView view) {
		super(view);
        facade = Facade.getInstance();
        facade.addObserver(this);
		initFromModel();
	}
	
	@Override
	public IGameHistoryView getView() {
		return (IGameHistoryView)super.getView();
	}
	
	private void initFromModel() {
        List<MessageLine> log = facade.getLog().getMessages();
		List<LogEntry> entries = new ArrayList<>();
        for(MessageLine line : log) {
            CatanColor color = facade.getPlayerColorByName(line.getPlayer());
            entries.add(new LogEntry(color, line.getMessage()));
        }
		getView().setEntries(entries);
	}

	@Override
	public void update(Observable o, Object arg) {
		initFromModel();
	}
}
