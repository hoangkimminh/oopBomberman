package oop.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private List<GameState> stateList = new ArrayList<>();
    private int currentState = GameState.MENU_STATE;

    public GameStateManager() {
        stateList.add(new MenuState());
        stateList.add(new SinglePlayerState());
        stateList.add(new HostState());
        stateList.add(new JoinState());
        stateList.add(new MultiplayerState());
    }

    public void start() {
        stateList.get(currentState).start();
    }

    public void changeState(int state) {
        currentState = state;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void nextLevel() {
        if (currentState == GameState.SERVER_STATE) {
            ((HostState) stateList.get(GameState.SERVER_STATE)).nextLevel();
        } else {
            ((SinglePlayerState) stateList.get(GameState.SINGLE_PLAYER_STATE)).nextLevel();
        }
    }
}
