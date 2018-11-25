package oop.Animation;

import oop.Graphic.Sprite;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class ExplosionAnimation extends Animation {

    private int type;

    public ExplosionAnimation(int type) {
        this.type = type;
        tickPerImage = 6;
    }

    @Override
    public void update() {
        currentTick++;
        if (currentTick % tickPerImage == 0) {
            state += add;
        }
    }

    @Override
    public BufferedImage getImage() {
        if (state < 5) {
            switch (type) {
                case Value.DOWN:
                case Value.UP:
                    return Sprite.explosion_vertical[state];
                case Value.RIGHT:
                case Value.LEFT:
                    return Sprite.explosion_horizontal[state];
                case Value.END_UP:
                    return Sprite.explosion_end_up[state];
                case Value.END_DOWN:
                    return Sprite.explosion_end_down[state];
                case Value.END_RIGHT:
                    return Sprite.explosion_end_right[state];
                case Value.END_LEFT:
                    return Sprite.explosion_end_left[state];
                case Value.MIDDLE:
                    return Sprite.explosion_center[state];
            }
        }
        return null;
    }
}
