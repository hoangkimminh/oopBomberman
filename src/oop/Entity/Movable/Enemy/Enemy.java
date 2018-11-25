package oop.Entity.Movable.Enemy;

import oop.Animation.EnemyAnimation;
import oop.Bomberman;
import oop.Entity.Movable.MovableEntity;
import oop.Setting.Gameplay;
import oop.Setting.Value;

public abstract class Enemy extends MovableEntity {

    protected boolean alive = true;
    protected int score;
    protected boolean brickIgnore = false;
    protected boolean bombIgnore = false;
    protected int tickToRemove = (int) (Gameplay.TICKS_PER_SECOND * 2.5);

    protected Enemy(double x, double y, double speed) {
        super(x, y, speed);
        direction = random.nextInt(4);
    }

    @Override
    public void update() {
        if (alive && fullCollideWithExplosion()) {
            dead();
        }
        if (!alive) {
            tickToRemove--;
            if (tickToRemove == 0) {
                Bomberman.board.score += score;
                remove();
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void dead() {
        alive = false;
        ((EnemyAnimation) animation).dead();
    }

    protected void wander() {
        double tempX = x;
        double tempY = y;
        switch (direction) {
            case Value.UP:
                y -= speed;
                break;
            case Value.DOWN:
                y += speed;
                break;
            case Value.LEFT:
                x -= speed;
                break;
            case Value.RIGHT:
                x += speed;
                break;
            default:
                return;
        }
        positionQuantimize();
        if (!bombIgnore && fullCollideWithBomb()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
            direction = random.nextInt(4);
            return;
        }
        if (fullCollideWithWall()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
            direction = random.nextInt(4);
            return;
        }
        if (!brickIgnore && fullCollideWithBrick()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
            direction = random.nextInt(4);
        }
    }
}
