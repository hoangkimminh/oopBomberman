package oop.Animation;

import oop.Graphic.Sprite;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class EnemyAnimation extends Animation {

    private int type;
    private int score;
    private int direction;
    private boolean alive = true;

    public EnemyAnimation(int type, int score) {
        this.type = type;
        this.score = score;
    }

    public void update(int direction) {
        this.direction = direction;
        update();
    }

    @Override
    public void update() {
        currentTick++;
        if (currentTick % tickPerImage == 0) {
            state += add;
            if (alive) {
                if (state == 0 || state == 2) {
                    add *= -1;
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (alive) {
            switch (type) {
                case Value.BALLOM_ENEMY:
                    return Sprite.enemy_ballom_moving[direction / 2][state];
                case Value.ONEAL_ENEMY:
                    return Sprite.enemy_oneal_moving[direction / 2][state];
                case Value.BOYON_ENEMY:
                    return Sprite.enemy_boyon_moving[direction / 2][state];
                case Value.PASS_ENEMY:
                    return Sprite.enemy_pass_moving[direction / 2][state];
                case Value.DORIA_ENEMY:
                    return Sprite.enemy_doria_moving[direction / 2][state];
            }
        } else if (state < 0) {
            switch (type) {
                case Value.BALLOM_ENEMY:
                    return Sprite.enemy_ballom_dead;
                case Value.ONEAL_ENEMY:
                    return Sprite.enemy_oneal_dead;
                case Value.BOYON_ENEMY:
                    return Sprite.enemy_boyon_dead;
                case Value.PASS_ENEMY:
                    return Sprite.enemy_pass_dead;
                case Value.DORIA_ENEMY:
                    return Sprite.enemy_doria_dead;
            }
        } else if (state < 5) {
            return Sprite.enemy_dead[state];
        }
        return Sprite.score[score];
    }

    public void dead() {
        alive = false;
        state = -5;
        add = 1;
        currentTick = 0;
    }
}
