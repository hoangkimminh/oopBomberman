package oop.Animation;

import oop.Entity.Movable.Player.Player;
import oop.Graphic.Sprite;
import java.awt.image.BufferedImage;

public class PlayerAnimation extends Animation {

    private Player player;
    private int color;

    public PlayerAnimation(Player player, int color) {
        this.player = player;
        this.color = color;
    }

    @Override
    public void update() {
        if (player.isAlive()) {
            if (player.isMoving()) {
                currentTick++;
                if (currentTick % tickPerImage == 0) {
                    state += add;
                    if (state == 0 || state == 2) {
                        add *= -1;
                    }
                }
            } else {
                currentTick = 0;
                state = 0;
                add = 1;
            }
        } else {
            currentTick++;
            if (currentTick % tickPerImage == 0) {
                state += add;
                if (state == Sprite.player_dead[color].length) {
                    player.remove();
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (player.isAlive()) {
            if (player.isMoving()) {
                return Sprite.player_moving[color][player.getDirection()][state];
            }
            return Sprite.player[color][player.getDirection()];
        }
        if (state < Sprite.player_dead[color].length) {
            return Sprite.player_dead[color][state];
        }
        return null;
    }

    public void dead() {
        state = 0;
        currentTick = 0;
        add = 1;
    }
}
