package oop.Setting;

public abstract class Gameplay {

    public static final int         TICKS_PER_SECOND = 60;
    public static final double      NS_PER_TICK = 1000000000.0 / TICKS_PER_SECOND;
    public static final int         LOOP_DELAY = 2;

    public static int               DRAGON_LENGTH = 6;

    public static final double      PLAYER_SPEED = 0.04;
    public static final double      BOMB_SPEED = 0.08;

    public static final double      BALLOM_SPEED = 0.024;
    public static final double      ONEAL_SPEED = 0.028;
    public static final double      BOYON_SPEED = 0.024;
    public static final double      PASS_SPEED = 0.06;
    public static final double      DORIA_SPEED = 0.02;
    public static final double      MINIBOSS_SPEED = 0.028;
    public static final double      BOSS_SPEED = 0.02;
    public static final double      DRAGON_SPEED = 0.032;

    public static boolean           GOD_MODE = false;
    public static boolean           FULL_ABILITY = false;

    public static void reset() {
        GOD_MODE = false;
        FULL_ABILITY = false;
        DRAGON_LENGTH = 6;
    }
}
