package oop.Entity.Movable.Enemy;

import oop.Animation.EnemyAnimation;
import oop.Setting.Gameplay;
import oop.Setting.Value;

public class Ballom extends Enemy {

    public Ballom(double x, double y) {
        super(x, y, Gameplay.BALLOM_SPEED);
        score = Value.SCORE[0];
        animation = new EnemyAnimation(Value.BALLOM_ENEMY, 0);
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            wander();
        }
        ((EnemyAnimation) animation).update(direction);
    }
}
