package oop.Entity.Movable.Bomb;

import oop.Animation.BombAnimation;
import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Entity.Entity;
import oop.Entity.Movable.Enemy.Enemy;
import oop.Entity.Movable.MovableEntity;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Explosion;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.util.ArrayList;
import java.util.List;

public abstract class Bomb extends MovableEntity {

    protected List<Entity> entityIgnore = new ArrayList<>();
    protected int radius;
    protected boolean moving = false;
    protected boolean superBomb;

    protected Bomb(double x, double y, int radius, boolean superBomb) {
        super(x, y, Gameplay.BOMB_SPEED);
        this.radius = radius;
        this.superBomb = superBomb;
        for (Player player : Bomberman.board.players) {
            if (collide(player)) {
                entityIgnore.add(player);
            }
        }
        for (Enemy enemy : Bomberman.board.enemies) {
            if (collide(enemy)) {
                entityIgnore.add(enemy);
            }
        }
    }

    @Override
    public void update() {
        if (getMapValue(x, y) == Value.EXPLOSION) {
            detonate();
        }
        for (int i = 0; i < entityIgnore.size(); i++) {
            if (!entityIgnore.get(i).collide(this)) {
                entityIgnore.remove(i);
                i--;
            }
        }
        if (moving) {
            move();
        }
        animation.update();
    }

    public boolean ignoreEntity(Entity other) {
        return entityIgnore.contains(other);
    }

    public boolean kick(int direction) {
        this.direction = direction;
        double tempX = x;
        double tempY = y;
        switch (direction) {
            case Value.UP:
                tempY -= 1;
                break;
            case Value.DOWN:
                tempY += 1;
                break;
            case Value.LEFT:
                tempX -= 1;
                break;
            case Value.RIGHT:
                tempX += 1;
                break;
        }
        for (Bomb bomb : Bomberman.board.bombs) {
            if (bomb.collide(tempX, tempY)) {
                return false;
            }
        }
        for (Player player : Bomberman.board.players) {
            if (player.collide(tempX, tempY)) {
                return false;
            }
        }
        for (Enemy enemy : Bomberman.board.enemies) {
            if (enemy.collide(tempX, tempY)) {
                return false;
            }
        }
        int value = getMapValue(tempX, tempY);
        if (value == Value.WALL || value == Value.EXPLOSION_BRICK || value == Value.BRICK) {
            return false;
        }
        moving = true;
        return true;
    }

    private void move() {
        double nextX = x;
        double nextY = y;
        double checkX = 0;
        double checkY = 0;
        boolean needCheckNextTile = false;
        switch (direction) {
            case Value.UP:
                nextY -= speed;
                if ((int)y >= nextY) {
                    checkX = x;
                    checkY = (int) nextY;
                    needCheckNextTile = true;
                }
                break;
            case Value.DOWN:
                nextY += speed;
                if (y <= (int)nextY) {
                    checkX = x;
                    checkY = (int) nextY + 1;
                    needCheckNextTile = true;
                }
                break;
            case Value.LEFT:
                nextX -= speed;
                if ((int)x >= nextX) {
                    checkX = (int) nextX;
                    checkY = y;
                    needCheckNextTile = true;
                }
                break;
            case Value.RIGHT:
                nextX += speed;
                if (x <= (int)nextX) {
                    checkX = (int) nextX + 1;
                    checkY = y;
                    needCheckNextTile = true;
                }
                break;
        }
        if (needCheckNextTile) {
            int value = getMapValue(checkX, checkY);
            if (value == Value.BRICK || value == Value.EXPLOSION_BRICK || value == Value.WALL) {
                stopMoving();
                return;
            }
            for (Player player : Bomberman.board.players) {
                if (player.collide(checkX, checkY)) {
                    stopMoving();
                    return;
                }
            }
            for (Enemy enemy : Bomberman.board.enemies) {
                if (enemy.collide(checkX, checkY)) {
                    stopMoving();
                    return;
                }
            }
            for (Bomb bomb : Bomberman.board.bombs) {
                if (bomb != this) {
                    if (bomb.collide(nextX, nextY)) {
                        stopMoving();
                        return;
                    }
                }
            }
        }

        for (Enemy enemy : Bomberman.board.enemies) {
            if (enemy.collide(nextX, nextY)) {
                enemy.pushBack(direction);
                stopMoving();
                return;
            }
        }
        for (Player player : Bomberman.board.players) {
            if (player.collide(nextX, nextY)) {
                player.pushBack(direction);
                stopMoving();
                return;
            }
        }
        x = nextX;
        y = nextY;
    }

    private void stopMoving() {
        x = Math.round(x);
        y = Math.round(y);
        moving = false;
    }

    public int getRadius() {
        return radius;
    }

    public void detonate() {
        remove();
        x = getMapX();
        y = getMapY();
        AudioManager.explosion.play();
        Bomberman.board.explosions.add(new Explosion(x, y, Value.MIDDLE));
        if (superBomb) {
            expandFullRadiusUp();
            expandFullRadiusRight();
            expandFullRadiusDown();
            expandFullRadiusLeft();
        } else {
            expandUp();
            expandRight();
            expandDown();
            expandLeft();
        }
    }

    private void expandRight() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x + i, y);
            if (value == Value.WALL || value == Value.EXPLOSION_BRICK) {
                return;
            }
            if (value == Value.BRICK) {
                setMapValue(x + i, y, Value.EXPLOSION_BRICK);
                return;
            }
            int nextValue = getMapValue(x + i + 1, y);
            if (i == radius || nextValue == Value.WALL || nextValue == Value.BRICK || nextValue == Value.EXPLOSION_BRICK) {
                Bomberman.board.explosions.add(new Explosion(x + i, y, Value.END_RIGHT));
            } else {
                Bomberman.board.explosions.add(new Explosion(x + i, y, Value.RIGHT));
            }
        }
    }

    private void expandDown() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x, y + i);
            if (value == Value.WALL || value == Value.EXPLOSION_BRICK) {
                return;
            }
            if (value == Value.BRICK) {
                setMapValue(x, y + i, Value.EXPLOSION_BRICK);
                return;
            }
            int nextValue = getMapValue(x, y + i + 1);
            if (i == radius || nextValue == Value.WALL || nextValue == Value.BRICK || nextValue == Value.EXPLOSION_BRICK) {
                Bomberman.board.explosions.add(new Explosion(x, y + i, Value.END_DOWN));
            } else {
                Bomberman.board.explosions.add(new Explosion(x, y + i, Value.DOWN));
            }
        }
    }

    private void expandLeft() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x - i, y);
            if (value == Value.WALL || value == Value.EXPLOSION_BRICK) {
                return;
            }
            if (value == Value.BRICK) {
                setMapValue(x - i, y, Value.EXPLOSION_BRICK);
                return;
            }
            int nextValue = getMapValue(x - i - 1, y);
            if (i == radius || nextValue == Value.WALL || nextValue == Value.BRICK || nextValue == Value.EXPLOSION_BRICK) {
                Bomberman.board.explosions.add(new Explosion(x - i, y, Value.END_LEFT));
            } else {
                Bomberman.board.explosions.add(new Explosion(x - i, y, Value.LEFT));
            }
        }
    }

    private void expandUp() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x, y - i);
            if (value == Value.WALL || value == Value.EXPLOSION_BRICK) {
                return;
            }
            if (value == Value.BRICK) {
                setMapValue(x, y - i, Value.EXPLOSION_BRICK);
                return;
            }
            int nextValue = getMapValue(x, y - i - 1);
            if (i == radius || nextValue == Value.WALL || nextValue == Value.BRICK || nextValue == Value.EXPLOSION_BRICK) {
                Bomberman.board.explosions.add(new Explosion(x, y - i, Value.END_UP));
            } else {
                Bomberman.board.explosions.add(new Explosion(x, y - i, Value.UP));
            }
        }
    }

    private void expandFullRadiusRight() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x + i, y);
            if (value == Value.WALL) {
                return;
            }
            int nextValue = getMapValue(x + i + 1, y);
            if (i == radius || nextValue == Value.WALL) {
                Bomberman.board.explosions.add(new Explosion(x + i, y, Value.END_RIGHT));
            } else {
                Bomberman.board.explosions.add(new Explosion(x + i, y, Value.RIGHT));
            }
        }
    }

    private void expandFullRadiusDown() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x, y + i);
            if (value == Value.WALL) {
                return;
            }
            int nextValue = getMapValue(x, y + i + 1);
            if (i == radius || nextValue == Value.WALL) {
                Bomberman.board.explosions.add(new Explosion(x, y + i, Value.END_DOWN));
            } else {
                Bomberman.board.explosions.add(new Explosion(x, y + i, Value.DOWN));
            }
        }
    }

    private void expandFullRadiusLeft() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x - i, y);
            if (value == Value.WALL) {
                return;
            }
            int nextValue = getMapValue(x - i - 1, y);
            if (i == radius || nextValue == Value.WALL) {
                Bomberman.board.explosions.add(new Explosion(x - i, y, Value.END_LEFT));
            } else {
                Bomberman.board.explosions.add(new Explosion(x - i, y, Value.LEFT));
            }
        }
    }

    private void expandFullRadiusUp() {
        for (int i = 1; i <= radius; i++) {
            int value = getMapValue(x, y - i);
            if (value == Value.WALL) {
                return;
            }
            int nextValue = getMapValue(x, y - i - 1);
            if (i == radius || nextValue == Value.WALL) {
                Bomberman.board.explosions.add(new Explosion(x, y - i, Value.END_UP));
            } else {
                Bomberman.board.explosions.add(new Explosion(x, y - i, Value.UP));
            }
        }
    }
}
