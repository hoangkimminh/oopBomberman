package oop.Setting;

public abstract class Value {

    public static final int         UP = 0;
    public static final int         RIGHT = 1;
    public static final int         DOWN = 2;
    public static final int         LEFT = 3;
    public static final int         PLANT = 4;
    public static final int         DETONATE = 5;
    public static final int         QUIT = 6;
    public static final int         PAUSE = 6;

    public static final int         END_UP = -1;
    public static final int         END_RIGHT = -2;
    public static final int         END_DOWN = -3;
    public static final int         END_LEFT = -4;
    public static final int         MIDDLE = -5;

    public static final int         GRASS = 0;
    public static final int         WALL = 1;
    public static final int         BRICK = 2;
    public static final int         EXPLOSION = 3;
    public static final int         EXPLOSION_BRICK = 4;

    public static final int         BOMB_ITEM = 0;
    public static final int         FIRE_ITEM = 1;
    public static final int         SPEED_ITEM = 2;
    public static final int         BRICK_PASS_ITEM = 3;
    public static final int         BOMB_PASS_ITEM = 4;
    public static final int         KICK_ITEM = 5;
    public static final int         REMOTE_BOMB_ITEM = 6;
    public static final int         SUPER_BOMB_ITEM = 7;

    public static final char        BALLOM_ENEMY = '0';
    public static final char        ONEAL_ENEMY = '1';
    public static final char        BOYON_ENEMY = '2';
    public static final char        PASS_ENEMY = '3';
    public static final char        DORIA_ENEMY = '4';

    public static final char        BOSS = 'B';
    public static final char        DRAGON = 'D';

    public static final int[]       SCORE = {100, 200, 400, 800, 2000};
}
