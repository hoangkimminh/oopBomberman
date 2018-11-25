package oop.Animation;

import oop.Graphic.Sprite;

import java.awt.image.BufferedImage;

public class BossAnimation extends Animation {

    private boolean alive = true;
    private int health = 2;

    public BossAnimation() {

    }

    @Override
    public void update() {
        currentTick++;
        if (currentTick % tickPerImage == 0) {
            state += add;
            if (alive) {
                if (state == 4) {
                    state = 0;
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (alive) {
            return Sprite.boss_moving[2 - health][state];
        } else if (state < 0) {
            return Sprite.boss_dead[0];
        } else if (state < 8) {
            return Sprite.boss_dead[state];
        }
        return Sprite.score[4];
    }

    public void dead() {
        health--;
        if (health < 0) {
            alive = false;
            state = -5;
            add = 1;
            currentTick = 0;
        }
    }
}
