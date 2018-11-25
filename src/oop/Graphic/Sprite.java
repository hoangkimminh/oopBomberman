package oop.Graphic;

import oop.Bomberman;
import oop.Setting.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;

public class Sprite {

    private static final int TILE_SIZE = 16;
    private static final int PLAYER_WIDTH = 17;
    private static final int PLAYER_HEIGHT = 21;
    private static final int PLAYER_DEAD = 23;

    private static BufferedImage enemy;
    private static BufferedImage bomber;
    private static BufferedImage object;
    private static Sprite sprite = new Sprite();


    public static BufferedImage[][] player;
    public static BufferedImage[][][] player_moving;    // color, direction, state
    public static BufferedImage[][] player_dead;

    public static BufferedImage[] remote_bomb;
    public static BufferedImage[] normal_bomb;

    public static BufferedImage[] explosion_center;
    public static BufferedImage[] explosion_vertical;
    public static BufferedImage[] explosion_horizontal;
    public static BufferedImage[] explosion_end_left;
    public static BufferedImage[] explosion_end_right;
    public static BufferedImage[] explosion_end_up;
    public static BufferedImage[] explosion_end_down;

    public static BufferedImage[] score;

    public static BufferedImage[] item;

    public static BufferedImage portal[];

    public static BufferedImage grass;

    public static BufferedImage[][] destroyableObject;
    public static BufferedImage[] undestroyableObject;

    public static BufferedImage[][] enemy_ballom_moving;
    public static BufferedImage enemy_ballom_dead;
    public static BufferedImage[][] enemy_oneal_moving;
    public static BufferedImage enemy_oneal_dead;
    public static BufferedImage[][] enemy_boyon_moving;
    public static BufferedImage enemy_boyon_dead;
    public static BufferedImage[][] enemy_pass_moving;
    public static BufferedImage enemy_pass_dead;
    public static BufferedImage[][] enemy_doria_moving;
    public static BufferedImage enemy_doria_dead;

    public static BufferedImage[] enemy_dead;

    public static BufferedImage[][] boss_moving;
    public static BufferedImage[] boss_dead;

    public static BufferedImage[][][] dragon_body_moving; //color, direction, state
    public static BufferedImage[][][] dragon_head_moving; //color, direction, state
    public static BufferedImage[][][] dragon_tail_moving; //color, direction, state
    public static BufferedImage[] dragon_body_dead;
    public static BufferedImage[] dragon_head_dead;

    public static BufferedImage[] miniboss_riseup;
    public static BufferedImage[] miniboss_moving;
    public static BufferedImage[] miniboss_dead;

    private Sprite() {
        // Read from file
        try {
            bomber = ImageIO.read(Sprite.class.getResource("/Texture/Bomber.png"));
            enemy = ImageIO.read(Sprite.class.getResource("/Texture/Enemy.png"));
            object = ImageIO.read(Sprite.class.getResource("/Texture/Object.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        //-------------------------
        //      Player
        //-------------------------
        player = new BufferedImage[5][4];
        //stand
        for (int i = 0; i < 5; i++) {
            player[i][0] = bomber.getSubimage(2 * PLAYER_WIDTH, i * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
            player[i][3] = bomber.getSubimage(5 * PLAYER_WIDTH, i * PLAYER_HEIGHT, PLAYER_WIDTH + 1, PLAYER_HEIGHT);
            player[i][1] = horizontalFlip(player[i][3]);
            player[i][2] = bomber.getSubimage(0, i * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        //moving
        player_moving = new BufferedImage[5][4][3];
        for (int i = 0; i < 5; i++) {
            player_moving[i][0][0] = bomber.getSubimage(3 * PLAYER_WIDTH, i * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
            player_moving[i][0][1] = player[i][0];
            player_moving[i][0][2] = horizontalFlip(player_moving[i][0][0]);
            player_moving[i][3][0] = bomber.getSubimage(4 * PLAYER_WIDTH, i * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
            player_moving[i][3][1] = player[i][3];
            player_moving[i][3][2] = bomber.getSubimage(6 * PLAYER_WIDTH + 1, i * PLAYER_HEIGHT, PLAYER_WIDTH + 1, PLAYER_HEIGHT);
            player_moving[i][1][0] = horizontalFlip(player_moving[i][3][0]);
            player_moving[i][1][1] = player[i][1];
            player_moving[i][1][2] = horizontalFlip(player_moving[i][3][2]);
            player_moving[i][2][0] = bomber.getSubimage(PLAYER_WIDTH, i * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
            player_moving[i][2][1] = player[i][2];
            player_moving[i][2][2] = horizontalFlip(player_moving[i][2][0]);
        }

        //dead
        player_dead = new BufferedImage[5][7];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                player_dead[i][j] = bomber.getSubimage(j * PLAYER_DEAD, 5 * PLAYER_HEIGHT + i * PLAYER_DEAD, PLAYER_DEAD, PLAYER_DEAD);
            }
        }


        //-------------------------
        //      Destroyable
        //-------------------------
        destroyableObject = new BufferedImage[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                destroyableObject[i][j] = object.getSubimage(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }


        //-------------------------
        //      Score
        //-------------------------
        score = new BufferedImage[Value.SCORE.length];
        for (int i = 0; i < 5; i++) {
            score[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics g = score[i].getGraphics();
            g.setColor(Color.BLACK);
            g.setFont(Bomberman.font.deriveFont(Font.BOLD,20));
            g.drawString(Integer.toString(Value.SCORE[i]), 0, 40);
        }


        //-------------------------
        //      Undestroyable
        //-------------------------
        undestroyableObject = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            undestroyableObject[i] = object.getSubimage(i * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Portal
        //-------------------------
        portal = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            portal[i] = object.getSubimage(i * TILE_SIZE, 8 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Explosion
        //-------------------------
        explosion_center = new BufferedImage[5];
        explosion_vertical = new BufferedImage[5];
        explosion_horizontal = new BufferedImage[5];
        explosion_end_down = new BufferedImage[5];
        explosion_end_left = new BufferedImage[5];
        explosion_end_right = new BufferedImage[5];
        explosion_end_up = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            explosion_center[i] = object.getSubimage(6 * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            explosion_horizontal[i] = object.getSubimage(7 * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            explosion_end_right[i] = object.getSubimage(8 * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            explosion_end_left[i] = horizontalFlip(explosion_end_right[i]);
            explosion_vertical[i] = object.getSubimage(9 * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            explosion_end_up[i] = object.getSubimage(10 * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            explosion_end_down[i] = verticalFlip(explosion_end_up[i]);
        }

        //-------------------------
        //      Item
        //-------------------------
        item = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            item[i] = object.getSubimage(i * TILE_SIZE, 7 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Bomb
        //-------------------------
        remote_bomb = new BufferedImage[4];
        normal_bomb = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            remote_bomb[i] = object.getSubimage((6 + i) * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            normal_bomb[i] = object.getSubimage((6 + i) * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Enemy
        //-------------------------
        enemy_dead = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            enemy_dead[i] = enemy.getSubimage(i * TILE_SIZE, 7 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //ballom
        enemy_ballom_dead = enemy.getSubimage(3 * TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
        enemy_ballom_moving = new BufferedImage[2][3];
        for (int i = 0; i < 3; i++) {
            enemy_ballom_moving[0][i] = enemy.getSubimage(i * TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
            enemy_ballom_moving[1][i] = horizontalFlip(enemy_ballom_moving[0][i]);
        }

        //oneal
        enemy_oneal_dead = enemy.getSubimage(3 * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
        enemy_oneal_moving = new BufferedImage[2][3];
        for (int i = 0; i < 3; i++) {
            enemy_oneal_moving[1][i] = enemy.getSubimage(i * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
            enemy_oneal_moving[0][i] = horizontalFlip(enemy_oneal_moving[1][i]);
        }

        //boyon
        enemy_boyon_dead = enemy.getSubimage(3 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        enemy_boyon_moving = new BufferedImage[2][3];
        for (int i = 0; i < 3; i++) {
            enemy_boyon_moving[0][i] = enemy.getSubimage(i * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            enemy_boyon_moving[1][i] = enemy_boyon_moving[0][i];
        }

        //pass
        enemy_pass_dead = enemy.getSubimage(3 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        enemy_pass_moving = new BufferedImage[2][3];
        for (int i = 0; i < 3; i++) {
            enemy_pass_moving[1][i] = enemy.getSubimage(i * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            enemy_pass_moving[0][i] = horizontalFlip(enemy_pass_moving[1][i]);
        }

        //doria
        enemy_doria_dead = enemy.getSubimage(3 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        enemy_doria_moving = new BufferedImage[2][3];
        for (int i = 0; i < 3; i++) {
            enemy_doria_moving[0][i] = enemy.getSubimage(i * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            enemy_doria_moving[1][i] = horizontalFlip(enemy_doria_moving[0][i]);
        }

        //-------------------------
        //      Boss
        //-------------------------
        boss_moving = new BufferedImage[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                boss_moving[i][j] = enemy.getSubimage(j * TILE_SIZE, (8 + i) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        boss_dead = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            boss_dead[i] = enemy.getSubimage(i * TILE_SIZE, 11 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        miniboss_riseup = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            miniboss_riseup[i] = enemy.getSubimage(i * TILE_SIZE, 12 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        miniboss_moving = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            miniboss_moving[i] = enemy.getSubimage(i * TILE_SIZE, 13 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        miniboss_dead = new BufferedImage[7];
        for (int i = 0; i < 7; i++) {
            miniboss_dead[i] = enemy.getSubimage(i * TILE_SIZE, 14 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Dragon
        //-------------------------
        dragon_head_moving = new BufferedImage[3][4][4];
        for (int color = 0; color < 3; color++) {
            for (int state = 0; state < 4; state++) {
                dragon_head_moving[color][3][state] = enemy.getSubimage(state * TILE_SIZE, (15 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                dragon_head_moving[color][2][state] = enemy.getSubimage(state * TILE_SIZE, (16 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                dragon_head_moving[color][1][state] = horizontalFlip(dragon_head_moving[color][3][state]);
                dragon_head_moving[color][0][state] = verticalFlip(dragon_head_moving[color][2][state]);
            }
        }
        dragon_body_moving = new BufferedImage[3][4][3];
        for (int color = 0; color < 3; color++) {
            for (int state = 0; state < 3; state++) {
                dragon_body_moving[color][3][state] = enemy.getSubimage((4 + state) * TILE_SIZE, (15 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                dragon_body_moving[color][2][state] = enemy.getSubimage((4 + state) * TILE_SIZE, (16 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                dragon_body_moving[color][1][state] = dragon_body_moving[color][3][state];
                dragon_body_moving[color][0][state] = dragon_body_moving[color][2][state];
            }
        }
        dragon_tail_moving = new BufferedImage[3][4][3];
        for (int color = 0; color < 3; color++) {
            dragon_tail_moving[color][3][0] = enemy.getSubimage(7 * TILE_SIZE, (15 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            dragon_tail_moving[color][3][2] = enemy.getSubimage(7 * TILE_SIZE, (15 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            dragon_tail_moving[color][3][1] = enemy.getSubimage(8 * TILE_SIZE, (15 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            dragon_tail_moving[color][2][0] = enemy.getSubimage(7 * TILE_SIZE, (16 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            dragon_tail_moving[color][2][2] = horizontalFlip(dragon_tail_moving[color][2][0]);
            dragon_tail_moving[color][2][1] = enemy.getSubimage(8 * TILE_SIZE, (16 + color * 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            dragon_tail_moving[color][1][0] = horizontalFlip(dragon_tail_moving[color][3][0]);
            dragon_tail_moving[color][1][2] = horizontalFlip(dragon_tail_moving[color][3][2]);
            dragon_tail_moving[color][1][1] = horizontalFlip(dragon_tail_moving[color][3][1]);

            dragon_tail_moving[color][0][0] = verticalFlip(dragon_tail_moving[color][2][0]);
            dragon_tail_moving[color][0][2] = verticalFlip(dragon_tail_moving[color][2][2]);
            dragon_tail_moving[color][0][1] = verticalFlip(dragon_tail_moving[color][2][1]);
        }
        dragon_head_dead = new BufferedImage[8];
        dragon_body_dead = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            dragon_head_dead[i] = enemy.getSubimage(i * TILE_SIZE, 21 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            dragon_body_dead[i] = enemy.getSubimage(i * TILE_SIZE, 22 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //-------------------------
        //      Grass
        //-------------------------
        grass = object.getSubimage(5 * TILE_SIZE, 6 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private static BufferedImage horizontalFlip(BufferedImage bufferedImage) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-bufferedImage.getWidth(null), 0);
        return new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(bufferedImage, null);
    }

    private static BufferedImage verticalFlip(BufferedImage bufferedImage) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(1, -1);
        affineTransform.translate(0, -bufferedImage.getHeight(null));
        return new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(bufferedImage, null);
    }
}
