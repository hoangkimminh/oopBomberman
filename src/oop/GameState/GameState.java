package oop.GameState;

import java.awt.image.BufferedImage;

public abstract class GameState {

    public static int MENU_STATE = 0;
    public static int SINGLE_PLAYER_STATE = 1;
    public static int SERVER_STATE = 2;
    public static int CLIENT_STATE = 3;
    public static int MULTI_STATE = 4;

    public static BufferedImage background;

    public abstract void start();
}
