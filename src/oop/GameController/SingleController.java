package oop.GameController;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Portal;
import oop.GameState.GameState;
import oop.Setting.Gameplay;
import oop.Setting.Size;
import oop.Setting.Value;

import java.awt.*;

public class SingleController extends Controller {

    public SingleController() {
        running = true;
    }

    @Override
    public void start() {
        running = true;
        boolean pause = false;
        Bomberman.inputHandle.reset();
        Bomberman.board.time = 150;
        Bomberman.render.redrawBackground();
        Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 20));
        long now = System.nanoTime();
        long last = System.currentTimeMillis();
        double delta = 0;

        int tickCount = 0;
        int frameCount = 0;

        while (running) {
            try {
                Thread.sleep(Gameplay.LOOP_DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Bomberman.inputHandle.keyRelease[Value.PAUSE] || pause) {
                if (Bomberman.inputHandle.keyRelease[Value.PAUSE]) {
                    Bomberman.inputHandle.keyRelease[Value.PAUSE] = false;
                    pause = !pause;
                    if (!pause) {
                        now = System.nanoTime();
                        last = System.currentTimeMillis();
                    }
                }
                if (Bomberman.inputHandle.keys[Value.QUIT]) {
                    Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                    break;
                }
                continue;
            }

            delta -= (now - (now = System.nanoTime())) / Gameplay.NS_PER_TICK;

            while (delta > 1) {
                delta -= 1;
                update();
                tickCount++;
                if (Bomberman.board.portals.isEmpty() && Bomberman.board.enemies.isEmpty()) {
                    Bomberman.board.portals.add(new Portal(1, 1));
                }
            }

            Bomberman.render.render(0);

            if (checkLose()) {
                AudioManager.play.stop();
                AudioManager.lose.play();
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                Bomberman.graphics.setColor(Color.BLACK);
                Bomberman.graphics.fillRect(0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT);
                Bomberman.graphics.setColor(Color.WHITE);
                Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
                Bomberman.graphics.drawString("GAMEOVER", Size.SCREEN_WIDTH / 5 * 2, Size.SCREEN_HEIGHT / 5 * 2);
                Bomberman.bufferStrategy.show();
                Bomberman.bufferStrategy.show();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            frameCount++;
            if (System.currentTimeMillis() - last >= 1000) {
                Bomberman.board.printInfo();
                last += 1000;
                System.out.println(tickCount + " ticks, " + frameCount + " frames, " + Bomberman.board.enemies.size() + " enemies");
                tickCount = 0;
                frameCount = 0;
                if (Bomberman.board.time > 0) {
                    Bomberman.board.time--;
                }
            }
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < Bomberman.board.explosions.size(); i++) {
            Bomberman.board.explosions.get(i).update();
            if (Bomberman.board.explosions.get(i).isRemove()) {
                Bomberman.board.explosions.remove(i);
                i--;
            }
        }
        for (int i = 0; i < Bomberman.board.bombs.size(); i++) {
            Bomberman.board.bombs.get(i).update();
            if (Bomberman.board.bombs.get(i).isRemove()) {
                Bomberman.board.bombs.remove(i);
                i--;
            }
        }
        for (int i = 0; i < Bomberman.board.bricks.size(); i++) {
            Bomberman.board.bricks.get(i).update();
            if (Bomberman.board.bricks.get(i).isRemove()) {
                Bomberman.board.bricks.remove(i);
                i--;
            }
        }
        for (int i = 0; i < Bomberman.board.items.size(); i++) {
            Bomberman.board.items.get(i).update();
            if (Bomberman.board.items.get(i).isRemove()) {
                Bomberman.board.items.remove(i);
                i--;
            }
        }
        for (int i = 0; i < Bomberman.board.enemies.size(); i++) {
            Bomberman.board.enemies.get(i).update();
            if (Bomberman.board.enemies.get(i).isRemove()) {
                Bomberman.board.enemies.remove(i);
                i--;
            }
        }
        for (int i = 0; i < Bomberman.board.portals.size(); i++) {
            Bomberman.board.portals.get(i).update();
        }
        for (Player player : Bomberman.board.players) {
            player.update();
        }
    }
}
