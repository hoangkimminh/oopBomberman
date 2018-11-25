package oop.GameController;

import oop.Bomberman;
import oop.Entity.Movable.Player.Player;

public abstract class Controller {

    protected boolean running;

    public abstract void start();
    public abstract void update();

    public void stop() {
        running = false;
    }

    public boolean checkLose() {
        if (Bomberman.board.time == 0) {
            return true;
        }
        for (Player player : Bomberman.board.players) {
            if (player.getImage() != null) {
                return false;
            }
        }
        return true;
    }
}
