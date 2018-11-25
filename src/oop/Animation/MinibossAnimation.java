package oop.Animation;

import oop.Graphic.Sprite;

import java.awt.image.BufferedImage;

public class MinibossAnimation extends Animation {

    private boolean alive = true;
    private boolean rise = true;
    @Override
    public void update() {
        currentTick++;
        if (rise) {
            if (currentTick % (tickPerImage * 3) == 0) {
                state += add;
                if (state == 5) {
                    state = 0;
                }
            }
        } else if (currentTick % tickPerImage == 0) {
            state += add;
            if (alive && state == 4) {
                state = 0;
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (alive) {
            if (rise) {
                return Sprite.miniboss_riseup[state];
            } else {
                return Sprite.miniboss_moving[state];
            }
        }
        if (state < 0) {
            return Sprite.miniboss_dead[0];
        } else if (state < 7) {
            return Sprite.miniboss_dead[state];
        }
        return null;
    }

    public void setRiseFalse() {
        rise = false;
        state = 0;
        currentTick = 0;
    }

    public void dead() {
        alive = false;
        rise = false;
        state = -5;
        add = 1;
        currentTick = 0;
    }
}
