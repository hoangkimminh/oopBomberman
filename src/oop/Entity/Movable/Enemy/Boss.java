package oop.Entity.Movable.Enemy;

import oop.Animation.BossAnimation;
import oop.Bomberman;
import oop.Setting.Gameplay;
import oop.Setting.Value;

public class Boss extends Enemy {

    private int generateTick = Gameplay.TICKS_PER_SECOND * 3;
    private int immuneBombTick = 0;
    private int health = 2;

    public Boss(double x, double y) {
        super(x, y, Gameplay.BOSS_SPEED);
        tickToRemove = Gameplay.TICKS_PER_SECOND * 5;
        score = Value.SCORE[4];
        animation = new BossAnimation();
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            immuneBombTick--;
            generateTick--;
            if (x == getMapX() && y == getMapY()) {
                if (random.nextInt(5) == 0) {
                    direction = random.nextInt(4);
                }
            }
            wander();
            if (generateTick < 0) {
                Bomberman.board.enemies.add(new Miniboss(x, y));
                generateTick = Gameplay.TICKS_PER_SECOND * 3;
            }
        }
        animation.update();
    }

    @Override
    public void dead() {
        if (immuneBombTick < 0) {
            health--;
            ((BossAnimation) animation).dead();
            immuneBombTick = Gameplay.TICKS_PER_SECOND;
            if (health < 0) {
                alive = false;
            }
        }
    }
}
