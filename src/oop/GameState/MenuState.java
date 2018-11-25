package oop.GameState;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Setting.Gameplay;
import oop.Setting.Size;
import oop.Setting.Value;

import javax.imageio.ImageIO;
import java.awt.*;

public class MenuState extends GameState {

    private int choice = 0;
    private boolean[] keyRelease = Bomberman.inputHandle.keyRelease;

    public MenuState() {
        try {
            background = ImageIO.read(MenuState.class.getResource("/Texture/Background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        int totalChoice = 3;
        Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
        Bomberman.inputHandle.stringBuilder = new StringBuilder();
        Bomberman.inputHandle.reset();
        Gameplay.reset();
        AudioManager.menu.play();
        while (Bomberman.gameStateManager.getCurrentState() == GameState.MENU_STATE) {
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bomberman.graphics.drawImage(background, 0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT, null);
            if (keyRelease[Value.UP]) {
                choice = (choice - 1 + totalChoice) % totalChoice;
                keyRelease[Value.UP] = false;
                AudioManager.pressUpDown.play();
            }

            if (keyRelease[Value.DOWN]) {
                choice = (choice + 1) % totalChoice;
                keyRelease[Value.DOWN] = false;
                AudioManager.pressUpDown.play();
            }
            String cheatCode = Bomberman.inputHandle.stringBuilder.toString();
            switch (choice) {
                case 0:
                    Bomberman.graphics.setColor(Color.BLUE);
                    Bomberman.graphics.drawString("Start", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 4);
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Multiplayer", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 2);
                    Bomberman.graphics.drawString("Cheat: " + cheatCode, Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 6);
                    break;
                case 1:
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Start", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 4);
                    Bomberman.graphics.setColor(Color.BLUE);

                    Bomberman.graphics.drawString("Multiplayer", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 2);
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Cheat: " + cheatCode, Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 6);
                    break;
                case 2:
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Start", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 4);
                    Bomberman.graphics.drawString("Multiplayer", Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 2);
                    Bomberman.graphics.setColor(Color.BLUE);
                    Bomberman.graphics.drawString("Cheat: " + cheatCode, Size.SCREEN_WIDTH / 3 + 20, Size.SCREEN_HEIGHT / 10 * 6);
                    break;
            }

            if (keyRelease[Value.PLANT]) {
                keyRelease[Value.PLANT] = false;
                switch (choice) {
                    case 0:
                        AudioManager.pressUpDown.play();
                        Bomberman.gameStateManager.changeState(GameState.SINGLE_PLAYER_STATE);
                        break;
                    case 1:
                        AudioManager.pressUpDown.play();
                        Bomberman.gameStateManager.changeState(GameState.MULTI_STATE);
                        break;
                    case 2:
                        break;
                }
            }
            Bomberman.bufferStrategy.show();
        }
    }
}
