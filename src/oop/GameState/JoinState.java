package oop.GameState;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import oop.Audio.Audio;
import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.GameController.Board;
import oop.GameController.ClientController;
import oop.GameState.GameState;
import oop.Net.Packet;
import oop.Setting.Size;
import oop.Setting.Value;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
public class JoinState extends GameState {

    private ClientController controller = new ClientController();
    public static int id = -1;
    public static Client client = new Client(1000000, 1000000);
    public static BufferedImage image;
    public static String command = "wait";

    public JoinState() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void disconnected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof byte[]) {
                    try {
                        Bomberman.board = (Board) Bomberman.deserialize((byte[]) o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (o instanceof String) {
                    command = (String) o;
                } else if (o instanceof Integer) {
                    id = (int) o;
                    System.out.println(id);
                }
            }
        });
        Kryo kryo = client.getKryo();
        kryo.register(byte[].class);
        kryo.register(boolean[].class);
        kryo.register(String.class);
        kryo.register(Packet.class);
    }

    @Override
    public void start() {
        client.start();
        try {
            client.connect(5000, Bomberman.inputHandle.stringBuilder.toString(), 6969, 9696);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.reconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
                client.stop();
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                return;
            }
        }
        AudioManager.menu.stop();
        while (client.isConnected() && Bomberman.gameStateManager.getCurrentState() == GameState.CLIENT_STATE) {
            System.out.println(command);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (command.equals("wait")) {
                if (Bomberman.inputHandle.keys[Value.QUIT]) {
                    Bomberman.inputHandle.reset();
                    Bomberman.gameStateManager.changeState(GameState.MULTI_STATE);
                    break;
                }
            } else if (command.contains("nextlevel")) {
                Bomberman.graphics.setColor(Color.BLACK);
                Bomberman.graphics.fillRect(0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT);
                Bomberman.graphics.setColor(Color.WHITE);
                Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
                Bomberman.graphics.drawString("Level " + command.substring(9), Size.SCREEN_WIDTH / 5 * 2, Size.SCREEN_HEIGHT / 5 * 2);
                Bomberman.bufferStrategy.show();
                Bomberman.bufferStrategy.show();
                command = "wait";
            } else if (command.equals("start")) {
                AudioManager.play.play();
                controller.start();
                AudioManager.play.stop();
            } else if (command.equals("win")) {
                Bomberman.graphics.setColor(Color.BLACK);
                Bomberman.graphics.fillRect(0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT);
                Bomberman.graphics.setColor(Color.WHITE);
                Bomberman.graphics.setFont(Bomberman.font.deriveFont(Font.PLAIN, 40f));
                Bomberman.graphics.drawString("YOU WIN", Size.SCREEN_WIDTH / 5 * 2, Size.SCREEN_HEIGHT / 5 * 2);
                Bomberman.graphics.drawString("CONGRATULATION", Size.SCREEN_WIDTH / 24 * 7, Size.SCREEN_HEIGHT / 2);
                Bomberman.bufferStrategy.show();
                Bomberman.bufferStrategy.show();
                command = "wait";
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
            } else if (command.equals("lose")) {
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
            }
        }
        client.stop();
        command = "wait";
    }
}
