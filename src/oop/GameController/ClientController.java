package oop.GameController;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.GameState.GameState;
import oop.GameState.JoinState;
import oop.Net.Packet;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.awt.*;

public class ClientController extends Controller {

    public ClientController() {

    }

    @Override
    public void start() {
        Bomberman.inputHandle.reset();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Packet packet = new Packet();
        packet.setId(JoinState.id);
        Bomberman.render.redrawBackground();
        long now = System.nanoTime();
        long last = System.currentTimeMillis();
        double delta = 0;

        int tickCount = 0;
        int frameCount = 0;

        int currentBomb = Bomberman.board.bombs.size();
        AudioManager.blueBomberman.play();
        Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 20));
        while (JoinState.command.equals("start")) {
            try {
                Thread.sleep(Gameplay.LOOP_DELAY);
                delta -= (now - (now = System.nanoTime())) / Gameplay.NS_PER_TICK;
                while (delta > 1) {
                    delta -= 1;
                    JoinState.client.sendTCP(packet);
                    Bomberman.inputHandle.keys[Value.DETONATE] = false;
                    tickCount++;
                    if (currentBomb > Bomberman.board.bombs.size()) {
                        currentBomb = Bomberman.board.bombs.size();
                        AudioManager.explosion.play();
                    } else if (currentBomb < Bomberman.board.bombs.size()) {
                        currentBomb = Bomberman.board.bombs.size();
                        AudioManager.plantBomb.play();
                    }
                }
                Bomberman.render.render(JoinState.id + 1);
                frameCount++;
                if (System.currentTimeMillis() - last >= 1000) {
                    last += 1000;
                    System.out.println(tickCount + " ticks, " + frameCount + " frames");
                    frameCount = 0;
                    tickCount = 0;
                }
                if (!JoinState.client.isConnected()) {
                    System.out.println("DISCONNECT");
                    Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("DISCONNECT");
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                break;
            }
        }
    }

    @Override
    public void update() {

    }
}
