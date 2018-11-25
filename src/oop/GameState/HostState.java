package oop.GameState;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.GameController.ServerController;
import oop.Level.LevelManager;
import oop.Net.Packet;
import oop.Setting.Size;
import oop.Setting.Value;

import java.awt.*;
import java.io.IOException;

public class HostState extends GameState {

    private LevelManager levelManager = new LevelManager(LevelManager.LAN);
    private ServerController controller = new ServerController();
    private boolean controllerRunning = false;
    public static int numPlayers = 0;
    public static Server server = new Server(1000000, 1000000);
    public static Packet[] packets;
    private boolean nextLevel = false;

    public HostState() {
        try {
            server.addListener(new Listener() {
                @Override
                public void connected(Connection connection) {
                    numPlayers++;
                }

                @Override
                public void disconnected(Connection connection) {
                    if (!controllerRunning) {
                        numPlayers--;
                    }
                }

                @Override
                public void received(Connection connection, Object o) {
                    if (o instanceof Packet) {
                        Packet packet = (Packet) o;
                        packets[packet.getId()] = packet;
                    }
                }
            });
            Kryo kryo = server.getKryo();
            kryo.register(byte[].class);
            kryo.register(boolean[].class);
            kryo.register(String.class);
            kryo.register(Packet.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        Bomberman.inputHandle.reset();
        controllerRunning = false;
        numPlayers = 0;
        server.start();
        try {
            server.bind(6969, 9696);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            Bomberman.graphics.drawImage(GameState.background, 0, 0, Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT, null);
            Bomberman.graphics.drawString("HOST - WAITING", Size.SCREEN_WIDTH / 4, Size.SCREEN_HEIGHT / 5 * 2);
            Bomberman.graphics.drawString(Integer.toString(numPlayers) + " PLAYERS CONNECTED", Size.SCREEN_WIDTH / 5, Size.SCREEN_HEIGHT / 2);
            Bomberman.bufferStrategy.show();
            if (Bomberman.inputHandle.keys[Value.QUIT]) {
                server.stop();
                Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
                return;
            }
            if (Bomberman.inputHandle.keys[Value.PLANT]) {
                if (numPlayers != 0) {
                    break;
                }
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        controllerRunning = true;
        Connection[] connections = server.getConnections();
        packets = new Packet[connections.length];
        for (int i = 0; i < connections.length; i++) {
            connections[i].sendTCP(i);
        }
        AudioManager.menu.stop();
        levelManager.loadLevel(1);
        Bomberman.board.score = 0;
        while (Bomberman.gameStateManager.getCurrentState() == GameState.SERVER_STATE) {
            nextLevel = false;
            server.sendToAllTCP("nextlevel" + Integer.toString(levelManager.getCurrentLevel()));
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
            server.sendToAllTCP("start");
            AudioManager.play.play();
            controller.start();
            AudioManager.play.stop();
            if (nextLevel) {
                levelManager.nextLevel();
                if (Bomberman.gameStateManager.getCurrentState() == GameState.MENU_STATE) {
                    server.sendToAllTCP("win");
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
                server.sendToAllTCP("lose");
            }
        }
        server.stop();
    }

    public void nextLevel() {
        nextLevel = true;
        controller.stop();
    }
}
