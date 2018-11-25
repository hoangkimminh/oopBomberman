package oop.Audio;

public class AudioManager {

    private static AudioManager audioManager = new AudioManager();

    //system
    public static Audio play;
    public static Audio menu;
    public static Audio blueBomberman;
    public static Audio redBomberman;
    public static Audio pressUpDown;
    public static Audio lose;
    public static Audio victory;

    //player
    public static Audio playerDead;

    //bomb
    public static Audio plantBomb;
    public static Audio explosion;
    public static Audio kickBomb;

    //enemy

    //item
    public static Audio itemActivate;

    private AudioManager() {
        play = new Audio("/Audio/PLAY.wav", true);
        menu = new Audio("/Audio/MENU.wav", true);
        lose = new Audio("/Audio/LOSE.wav", false);
        victory = new Audio("/Audio/VICTORY.wav", false);

        blueBomberman = new Audio("/Audio/BLUE_BOMBERMAN.wav", false);
        redBomberman = new Audio("/Audio/RED_BOMBERMAN.wav", false);

        pressUpDown = new Audio("/Audio/UP_DOWN.wav", false);

        plantBomb = new Audio("/Audio/BOM_SET.wav", false);
        explosion = new Audio("/Audio/BOM_EXPLOSION_M.wav", false);
        kickBomb = new Audio("/Audio/BOM_KICK.wav", false);
        itemActivate = new Audio("/Audio/ITEM_GET.wav", false);

        playerDead = new Audio("/Audio/PLAYER_OUT.wav", false);
    }


}
