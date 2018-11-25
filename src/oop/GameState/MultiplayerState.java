package oop.GameState;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Setting.Size;
import oop.Setting.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MultiplayerState extends GameState {
    private int choice = 0;
    private final int totalChoice = 2;
    private boolean[] keys = Bomberman.inputHandle.keys;
    private boolean[] keyRelease = Bomberman.inputHandle.keyRelease;
    private String hostName;

    public MultiplayerState() {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        Arrays.fill(keyRelease, false);
        while (Bomberman.gameStateManager.getCurrentState() == GameState.MULTI_STATE) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bomberman.graphics.drawImage(background, 0, 0, Size.SCREEN_WIDTH , Size.SCREEN_HEIGHT, null);
            if (keyRelease[Value.UP]) {
                AudioManager.pressUpDown.play();
                choice = (choice - 1 + totalChoice) % totalChoice;
                keyRelease[Value.UP] = false;
            }

            if (keyRelease[Value.DOWN]) {
                AudioManager.pressUpDown.play();
                choice = (choice + 1) % totalChoice;
                keyRelease[Value.DOWN] = false;
            }

            if (keys[Value.QUIT]) {
                AudioManager.pressUpDown.play();
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
            }
            String serverName = Bomberman.inputHandle.stringBuilder.toString();

            switch (choice) {
                case 0:
                    Bomberman.graphics.setColor(Color.BLUE);
                    Bomberman.graphics.drawString("Host - " + hostName, Size.SCREEN_WIDTH / 4, Size.SCREEN_HEIGHT / 2);
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Join - " + serverName, Size.SCREEN_WIDTH / 4, Size.SCREEN_HEIGHT / 10 * 6);
                    break;
                case 1:
                    Bomberman.graphics.setColor(Color.BLACK);
                    Bomberman.graphics.drawString("Host - " + hostName, Size.SCREEN_WIDTH / 4, Size.SCREEN_HEIGHT / 2);
                    Bomberman.graphics.setColor(Color.BLUE);
                    Bomberman.graphics.drawString("Join - " + serverName, Size.SCREEN_WIDTH / 4, Size.SCREEN_HEIGHT / 10 * 6);
                    break;
            }

            if (keyRelease[Value.PLANT]) {
                keyRelease[Value.PLANT] = false;
                AudioManager.pressUpDown.play();
                switch (choice) {
                    case 0:
                        Bomberman.gameStateManager.changeState(GameState.SERVER_STATE);
                        break;
                    case 1:
                        Bomberman.gameStateManager.changeState(GameState.CLIENT_STATE);
                        break;
                }
            }

            Bomberman.bufferStrategy.show();
        }
    }
}
