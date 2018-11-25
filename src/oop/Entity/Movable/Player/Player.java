package oop.Entity.Movable.Player;

import oop.Audio.AudioManager;
import oop.Bomberman;
import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Bomb.NormalBomb;
import oop.Entity.Movable.Bomb.RemoteBomb;
import oop.Entity.Movable.Enemy.Enemy;
import oop.Entity.Movable.MovableEntity;
import oop.Animation.*;
import oop.Entity.Static.Item;
import oop.Entity.Static.Portal;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.util.ArrayList;
import java.util.List;

public class Player extends MovableEntity {

    private boolean alive = true;
    private boolean moving = false;
    private boolean remoteBomb = false;
    private boolean superBomb = false;
    private boolean brickIgnore = false;
    private boolean bombIgnore = false;
    private boolean kick = false;
    private int bombCapacity = 1;
    private int bombRadius = 1;
    private int plantDelay = Gameplay.TICKS_PER_SECOND;
    private boolean[] keys = Bomberman.inputHandle.keys;
    private List<Bomb> bombs = new ArrayList<>();

    public Player(double x, double y, int color) {
        super(x, y, Gameplay.PLAYER_SPEED);
        if (Gameplay.FULL_ABILITY) {
            remoteBomb = true;
            kick = true;
            superBomb = true;
            brickIgnore = true;
            bombCapacity = 10;
            bombRadius = 10;
            speed *= 2;
        }
        direction = Value.DOWN;
        animation = new PlayerAnimation(this, color);
    }

    @Override
    public void update() {
        if (alive) {
            if (Bomberman.board.enemies.isEmpty()) {
                for (Portal portal : Bomberman.board.portals) {
                    if (collide(portal)) {
                        Bomberman.gameStateManager.nextLevel();
                        AudioManager.victory.play();
                        return;
                    }
                }
            }

            for (int i = 0; i < bombs.size(); i++) {
                if (!Bomberman.board.bombs.contains(bombs.get(i))) {
                    bombs.remove(i);
                    i--;
                }
            }
            for (Item item : Bomberman.board.items) {
                if (collide(item)) {
                    item.activate(this);
                }
            }
            plantDelay--;
            if (!Gameplay.GOD_MODE && (halfCollideWithExplosion() || collideWithEnemy())) {
                dead();
                return;
            }
            action();
        }
        animation.update();
    }

    public void update(boolean[] keys) {
        if (keys == null) {
            return;
        }
        this.keys = keys;
        update();
    }

    private void action() {
        double tempX = x;
        double tempY = y;
        moving = false;
        if (keys[Value.UP]) {
            y -= speed;
            xQuantimize();
            direction = Value.UP;
            moving = true;
        } else if (keys[Value.RIGHT]) {
            x += speed;
            yQuantimize();
            direction = Value.RIGHT;
            moving = true;
        } else if (keys[Value.DOWN]) {
            y += speed;
            xQuantimize();
            direction = Value.DOWN;
            moving = true;
        } else if (keys[Value.LEFT]) {
            x -= speed;
            yQuantimize();
            direction = Value.LEFT;
            moving = true;
        }
        if (keys[Value.PLANT]) {
            plant();
        }
        if (keys[Value.DETONATE]) {
            for (Bomb bomb : bombs) {
                if (bomb instanceof RemoteBomb) {
                    bomb.detonate();
                    bombs.remove(bomb);
                    break;
                }
            }
            keys[Value.DETONATE] = false;
        }

        if (fullCollideWithWall()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
            return;
        }
        if (!bombIgnore) {
            for (Bomb bomb : Bomberman.board.bombs) {
                if (!bomb.ignoreEntity(this) && collide(bomb)) {
                    if (kick && bomb.kick(direction)) {
                        return;
                    } else {
                        x = tempX;
                        y = tempY;
                        positionQuantimize();
                        return;
                    }
                }
            }
        }
        if (!brickIgnore && fullCollideWithBrick()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
        }
    }

    private void plant() {
        int value = getMapValue(x, y);
        if (plantDelay > 0 || value == Value.BRICK || value == Value.EXPLOSION_BRICK || bombs.size() == bombCapacity)
            return;
        int tempX = getMapX();
        int tempY = getMapY();
        for (Bomb bomb : Bomberman.board.bombs) {
            if (bomb.samePosition(tempX, tempY)) {
                return;
            }
        }
        AudioManager.plantBomb.play();
        if (remoteBomb) {
            RemoteBomb bomb = new RemoteBomb(tempX, tempY, bombRadius, superBomb);
            Bomberman.board.bombs.add(bomb);
            bombs.add(bomb);
        } else {
            NormalBomb bomb = new NormalBomb(tempX, tempY, bombRadius, superBomb);
            Bomberman.board.bombs.add(bomb);
            bombs.add(bomb);
        }
        plantDelay = (int) (1 / speed);
    }

    public boolean isMoving() {
        return moving;
    }

    private void xQuantimize() {
        if (Math.abs(Math.round(x) - x) < 0.3) {
            x = Math.round(x);
        }
    }

    private void yQuantimize() {
        if (Math.abs(Math.round(y) - y) < 0.3) {
            y = Math.round(y);
        }
    }

    private boolean collideWithEnemy() {
        for (Enemy enemy : Bomberman.board.enemies) {
            if (enemy.isAlive() && collide(enemy)) {
                return true;
            }
        }
        return false;
    }

    private void dead() {
        alive = false;
        ((PlayerAnimation) animation).dead();
    }

    public boolean isAlive() {
        return alive;
    }

    public Object[] getBuff() {
        Object[] objects = new Object[8];
        objects[0] = remoteBomb;
        objects[1] = superBomb;
        objects[2] = brickIgnore;
        objects[3] = bombIgnore;
        objects[4] = kick;
        objects[5] = bombCapacity;
        objects[6] = bombRadius;
        objects[7] = speed;
        return objects;
    }

    public void setBuff(Object[] objects) {
        remoteBomb = (boolean) objects[0];
        superBomb = (boolean) objects[1];
        brickIgnore = (boolean) objects[2];
        bombIgnore = (boolean) objects[3];
        kick = (boolean) objects[4];
        bombCapacity = (int) objects[5];
        bombRadius = (int) objects[6];
        speed = (double) objects[7];
    }

    public void increaseBombCapacity() {
        if (bombCapacity < 10) {
            bombCapacity++;
        }
    }

    public void increaseBombRadius() {
        if (bombRadius < 10) {
            bombRadius++;
        }
    }

    public void increaseSpeed() {
        if (speed < Gameplay.PLAYER_SPEED * 2) {
            speed += Gameplay.PLAYER_SPEED / 5;
        }
    }

    public void enableBrickIgnore() {
        brickIgnore = true;
    }

    public void enableBombIgnore() {
        kick = false;
        bombIgnore = true;
    }

    public void enableKick() {
        bombIgnore = false;
        kick = true;
    }

    public void enableRemoteBomb() {
        remoteBomb = true;
    }

    public void enableSuperBomb() {
        superBomb = true;
    }
}
