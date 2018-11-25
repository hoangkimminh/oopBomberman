package oop.Level;

import oop.Bomberman;
import oop.Entity.Movable.Enemy.*;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Brick;
import oop.Entity.Static.Item;
import oop.Entity.Static.Portal;
import oop.GameState.GameState;
import oop.GameState.HostState;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    public static final int LOCAL = 0;
    public static final int LAN = 1;

    private int currentLevel = 1;
    private int mode;

    public LevelManager(int mode) {
        this.mode = mode;
    }

    public boolean loadLevel(int level) {
        currentLevel = level;
        String path = "/Level/";
        if (mode == LOCAL) {
            path += "SinglePlayer/";
        } else {
            path += "Multiplayer/";
        }
        path += "Level" + Integer.toString(currentLevel) + ".txt";
        List<String> list = LevelLoader.load(LevelManager.class.getResourceAsStream(path));
        if (list == null) {
            Bomberman.gameStateManager.changeState(GameState.MENU_STATE);
            return false;
        }
        Bomberman.board.reset();
        Bomberman.board.map = new int[list.size()][list.get(0).length()];
        for (int y = 0; y < list.size(); y++) {
            for (int x = 0; x < list.get(y).length(); x++) {
                Bomberman.board.map[y][x] = Value.GRASS;
                switch (list.get(y).charAt(x)) {
                    //brick
                    case 'b':
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    //single player
                    case 'p':
                        Bomberman.board.players.add(new Player(x, y, 0));
                        break;
                    case 'm':
                        if (mode == LAN && HostState.numPlayers >= Bomberman.board.players.size()) {
                            Bomberman.board.players.add(new Player(x, y, 1));
                        }
                        break;
                    case 'n':
                        if (mode == LAN && HostState.numPlayers >= Bomberman.board.players.size()) {
                            Bomberman.board.players.add(new Player(x, y, 2));
                        }
                        break;
                    case 'q':
                        if (mode == LAN && HostState.numPlayers >= Bomberman.board.players.size()) {
                            Bomberman.board.players.add(new Player(x, y, 3));
                        }
                        break;
                    //wall
                    case 'w':
                        Bomberman.board.map[y][x] = Value.WALL;
                        break;
                    //item
                    case '!':
                        Bomberman.board.items.add(new Item(x, y, Value.BOMB_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '@':
                        Bomberman.board.items.add(new Item(x, y, Value.FIRE_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '#':
                        Bomberman.board.items.add(new Item(x, y, Value.SPEED_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '$':
                        Bomberman.board.items.add(new Item(x, y, Value.BRICK_PASS_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '%':
                        Bomberman.board.items.add(new Item(x, y, Value.BOMB_PASS_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '^':
                        Bomberman.board.items.add(new Item(x, y, Value.KICK_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '&':
                        Bomberman.board.items.add(new Item(x, y, Value.REMOTE_BOMB_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    case '*':
                        Bomberman.board.items.add(new Item(x, y, Value.SUPER_BOMB_ITEM));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                    //enemy
                    case Value.BALLOM_ENEMY:
                        Bomberman.board.enemies.add(new Ballom(x, y));
                        break;
                    case Value.ONEAL_ENEMY:
                        Bomberman.board.enemies.add(new Oneal(x, y));
                        break;
                    case Value.BOYON_ENEMY:
                        Bomberman.board.enemies.add(new Boyon(x, y));
                        break;
                    case Value.PASS_ENEMY:
                        Bomberman.board.enemies.add(new Pass(x, y));
                        break;
                    case Value.DORIA_ENEMY:
                        Bomberman.board.enemies.add(new Doria(x, y));
                        break;
                    case Value.BOSS:
                        Bomberman.board.enemies.add(new Boss(x, y));
                        break;
                    case Value.DRAGON:
                        Bomberman.board.enemies.add(new Dragon(x, y, Gameplay.DRAGON_LENGTH));
                        break;
                    //portal
                    case 'P':
                        Bomberman.board.portals.add(new Portal(x, y));
                        Bomberman.board.bricks.add(new Brick(x, y));
                        break;
                }
            }
        }
        return true;
    }

    public void nextLevel() {
        List<Object[]> buffs = new ArrayList<>();
        for (Player player : Bomberman.board.players) {
            buffs.add(player.getBuff());
        }
        if (!loadLevel(currentLevel + 1)) {
            return;
        }
        for (int i = 0; i < buffs.size(); i++) {
            Bomberman.board.players.get(i).setBuff(buffs.get(i));
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
