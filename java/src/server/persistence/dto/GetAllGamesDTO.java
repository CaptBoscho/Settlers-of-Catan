package dto;

import java.util.List;

/**
 * Created by boscho on 4/6/16.
 */
public class GetAllGamesDTO {

    public List<GameDTO> getGameStates() {
        return gameStates;
    }

    public void setGameStates(List<GameDTO> gameStates) {
        this.gameStates = gameStates;
    }

    public void addGame(GameDTO game){
        gameStates.add(game);
    }

    //Json in form of string
    List<GameDTO> gameStates;
}
