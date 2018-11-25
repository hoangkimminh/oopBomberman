package oop.Entity.Movable.Enemy;

import oop.Animation.EnemyAnimation;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class Boyon extends Enemy {

    public Boyon(double x, double y) {
        super(x, y, Gameplay.BOYON_SPEED);
        score = Value.SCORE[1];
        animation = new EnemyAnimation(Value.BOYON_ENEMY, 1);
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            positionQuantimize();
            if (x == getMapX() && y == getMapY()) {
                if (random.nextInt(5) == 0) {
                    direction = random.nextInt(4);
                }
            }
            wander();
        }
        ((EnemyAnimation) animation).update(direction);
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }
}
