package oop.GameState;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.GameController.SingleController;
import oop.Level.LevelManager;
import oop.Setting.Gameplay;
import oop.Setting.Size;

import java.awt.*;

public class SinglePlayerState extends GameState {

    private LevelManager levelManager = new LevelManager(LevelManager.LOCAL);
    private SingleController controller = new SingleController();
    private boolean nextLevel = false;

    @Override
    public void start() {
        String string = Bomberman.inputHandle.stringBuilder.toString();
        Bomberman.board.score = 0;
        if (string.contains("GM")) {
            Gameplay.GOD_MODE = true;
        }
        if (string.contains("FA")) {
            Gameplay.FULL_ABILITY = true;
        }
        if (string.contains("L2")) {
            levelManager.loadLevel(2);
        } else if (string.contains("L3")) {
            levelManager.loadLevel(3);
        } else if (string.contains("L4")) {
            levelManager.loadLevel(4);
        } else if (string.contains("L5")) {
            levelManager.loadLevel(5);
        } else if (string.contains("ZOO")) {
            levelManager.loadLevel(-1);
        } else {
            levelManager.loadLevel(1);
        }
        AudioManager.menu.stop();
        while (Bomberman.gameStateManager.getCurrentState() == GameState.SINGLE_PLAYER_STATE) {
            nextLevel = false;
            Bomberman.graphics.setColor(Color.BLACK);
            Bomberman.graphics.fillRect(0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT);
            Bomberman.graphics.setColor(Color.WHITE);
            Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
            Bomberman.graphics.drawString("Level " + Integer.toString(levelManager.getCurrentLevel()), Size.SCREEN_WIDTH / 5 * 2, Size.SCREEN_HEIGHT / 5 * 2);
            Bomberman.bufferStrategy.show();
            Bomberman.bufferStrategy.show();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AudioManager.play.play();
            AudioManager.blueBomberman.play();
            controller.start();
            AudioManager.play.stop();
            if (nextLevel) {
                levelManager.nextLevel();
                if (Bomberman.gameStateManager.getCurrentState() == GameState.MENU_STATE) {
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.fillRect(0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT);
                    Bomberman.graphics.setColor(Color.WHITE);
                    Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
                    Bomberman.graphics.drawString("YOU WIN", Size.SCREEN_WIDTH / 5 * 2, Size.SCREEN_HEIGHT / 5 * 2);
                    Bomberman.graphics.drawString("CONGRATULATION", Size.SCREEN_WIDTH / 24 * 7, Size.SCREEN_HEIGHT / 2);
                    Bomberman.bufferStrategy.show();
                    Bomberman.bufferStrategy.show();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
            }
        }
    }

    public void nextLevel() {
        nextLevel = true;
        controller.stop();
    }
}
