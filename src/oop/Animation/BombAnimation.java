package oop.Animation;

import oop.Graphic.Sprite;

import java.awt.image.BufferedImage;

public class BombAnimation extends Animation {

    private boolean remote;

    public BombAnimation(boolean remote) {
        this.remote = remote;
    }

    @Override
    public void update() {
        currentTick++;
        if (currentTick % tickPerImage == 0) {
            state += add;
            if (state == 4) {
                state = 0;
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (remote) {
            return Sprite.remote_bomb[state];
        }
        return Sprite.normal_bomb[state];
    }
}
