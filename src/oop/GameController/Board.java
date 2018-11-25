package oop.GameController;

import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Enemy.Enemy;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Brick;
import oop.Entity.Static.Explosion;
import oop.Entity.Static.Item;
import oop.Entity.Static.Portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    public List<Bomb> bombs = new ArrayList<>();
    public List<Brick> bricks = new ArrayList<>();
    public List<Portal> portals = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
    public List<Explosion> explosions = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public int[][] map;
    public int time;
    public int score;

    public Board() {

    }

    public void reset() {
        bombs.clear();
        bricks.clear();
        portals.clear();
        explosions.clear();
        enemies.clear();
        items.clear();
        players.clear();
        map = null;
    }

    public void printInfo() {
        System.out.println("Board info: " + players.size() + " players, " + bombs.size() + " bombs, " + bricks.size() + " bricks, "
        + explosions.size() + " explosions, " + enemies.size() + " enemies, " + items.size() + " items, " + portals.size() + " portals");
    }
}
