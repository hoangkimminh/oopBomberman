package oop.GameController;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Entity.Static.Portal;
import oop.GameState.GameState;
import oop.GameState.HostState;
import oop.Graphic.Render;
import oop.Net.Packet;
import oop.Setting.Gameplay;
import oop.Setting.Size;

import java.awt.*;

public class ServerController extends Controller {


    public ServerController() {
        running = true;
    }

    @Override
    public void start() {
        running = true;
        Bomberman.inputHandle.reset();
        Bomberman.board.time = 150;
        Bomberman.render.redrawBackground();
        try {
            HostState.server.sendToAllTCP(Bomberman.serialize(Bomberman.board));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long now = System.nanoTime();
        long last = System.currentTimeMillis();
        double delta = 0;

        int tickCount = 0;
        int frameCount = 0;
        Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 20));

        Render render = Bomberman.render;

        while (running) {
            try {
                Thread.sleep(Gameplay.LOOP_DELAY);
                delta -= (now - (now = System.nanoTime())) / Gameplay.NS_PER_TICK;
                while (delta > 1) {
                    delta -= 1;
                    update();
                    tickCount++;
                    if (Bomberman.board.portals.isEmpty() && Bomberman.board.enemies.isEmpty()) {
                        Bomberman.board.portals.add(new Portal(1, 1));
                    }
                }
                render.render(0);
                HostState.server.sendToAllTCP(Bomberman.serialize(Bomberman.board));
                frameCount++;
                if (System.currentTimeMillis() - last >= 1000) {
                    last += 1000;
                    Bomberman.board.printInfo();
                    System.out.println(tickCount + " ticks, " + frameCount + " frames");
                    tickCount = 0;
                    frameCount = 0;
                    Bomberman.board.time--;
                }
                if (checkLose()) {
                    AudioManager.play.stop();
                    AudioManager.lose.play();
                    HostState.server.sendToAllTCP("lose");
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
            } catch (Exception e) {
                System.out.println("DISCONNECT");
                e.printStackTrace();
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                break;
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
        for (Portal portal : Bomberman.board.portals) {
            portal.update();
        }
        Bomberman.board.players.get(0).update();
        for (int i = 0; i < HostState.numPlayers; i++) {
            Packet packet = HostState.packets[i];
            if (packet != null) {
                Bomberman.board.players.get(i + 1).update(HostState.packets[i].getKeys());
            }
        }
    }
}
