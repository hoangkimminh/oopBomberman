package oop.Entity.Movable.Enemy;

import oop.Animation.MinibossAnimation;
import oop.Setting.Gameplay;

public class Miniboss extends Enemy {

    private int riseupTick = 150;

    public Miniboss(double x, double y) {
        super(x, y, Gameplay.MINIBOSS_SPEED);
        tickToRemove = Gameplay.TICKS_PER_SECOND * 3;
        score = 0;
        animation = new MinibossAnimation();
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            if (riseupTick > 0) {
                riseupTick--;
                if (riseupTick == 0) {
                    ((MinibossAnimation) animation).setRiseFalse();
                }
            } else {
                if (x == getMapX() && y == getMapY()) {
                    if (random.nextInt(5) == 0) {
                        direction = random.nextInt(4);
                    }
                }
                wander();
            }
        }
        animation.update();
    }

    @Override
    public void dead() {
        alive = false;
        ((MinibossAnimation) animation).dead();
    }
}
