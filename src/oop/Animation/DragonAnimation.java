package oop.Animation;

import oop.Graphic.Sprite;
import oop.Setting.Gameplay;

import java.awt.image.BufferedImage;

public class DragonAnimation extends Animation {

    private boolean alive = true;
    private int health = 2;
    private int length;
    private int direction = 0;

    public DragonAnimation(int length) {
        this.length = length;
    }

    public void update(int direction) {
        if (direction != -1) {
            this.direction = direction;
        }
        update();
    }

    @Override
    public void update() {
        currentTick++;
        if (currentTick % tickPerImage == 0) {
            state += add;
            if (alive) {
                if (length == Gameplay.DRAGON_LENGTH) {
                    if (state == 0 || state == 3) {
                        add *= -1;
                    }
                } else if (state == 2 || state == 0) {
                    add *= -1;
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (length == Gameplay.DRAGON_LENGTH) {
            if (alive) {
                return Sprite.dragon_head_moving[2 - health][direction][state];
            } else if (state < 8) {
                return Sprite.dragon_head_dead[state];
            } else {
                return Sprite.score[4];
            }
        } else if (alive) {
            if (length == 0) {
                return Sprite.dragon_tail_moving[2 - health][direction][state];
            } else {
                return Sprite.dragon_body_moving[2 - health][direction][state];
            }
        } else if (state < 8) {
            return Sprite.dragon_body_dead[state];
        }
        return null;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void loseHealth() {
        if (health > 0) {
            health--;
        }
    }

    public void dead() {
        alive = false;
        add = 1;
        state = 0;
        currentTick = 0;
        tickPerImage = 15;
    }
}
