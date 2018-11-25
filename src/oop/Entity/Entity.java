package oop.Entity;

import oop.Bomberman;
import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Enemy.Dragon;
import oop.Entity.Movable.Enemy.Oneal;
import oop.Entity.Movable.Enemy.Pass;
import oop.Entity.Static.Brick;
import oop.Setting.Size;
import oop.Setting.Value;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

public abstract class Entity implements Serializable {

    protected static Random random = new Random();
    protected double x;
    protected double y;
    private boolean remove;

    protected Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update();
    public abstract BufferedImage getImage();

    //getter
    public int getMapValue(double x, double y) {
        return Bomberman.board.map[(int) Math.round(y)][(int) Math.round(x)];
    }

    public int getMapValue() {
        return Bomberman.board.map[(int) Math.round(y)][(int) Math.round(x)];
    }

    public int getScreenX() {
        return (int) (x * Size.RTILE_SIZE);
    }

    public int getScreenY() {
        return (int) (y * Size.RTILE_SIZE);
    }

    public int getMapX() {
        return (int) Math.round(x);
    }

    public int getMapY() {
        return (int) Math.round(y);
    }

    //setter
    public void setMapValue(double x, double y, int value) {
        Bomberman.board.map[(int) Math.round(y)][(int) Math.round(x)] = value;
    }

    public void setMapValue(int value) {
        Bomberman.board.map[(int) Math.round(y)][(int) Math.round(x)] = value;
    }

    public void remove() {
        remove = true;
    }

    //function
    //vật thể bị đẩy về phía direction
    public void pushBack(int direction) {
        if (this instanceof Pass || this instanceof Dragon) {
            return;
        }
        switch (direction) {
            case Value.UP:
                y = (int)y;
                break;
            case Value.DOWN:
                y = (int)y + 1;
                break;
            case Value.LEFT:
                x = (int)x;
                break;
            case Value.RIGHT:
                x = (int)x + 1;
                break;
        }
        if (this instanceof Oneal) {
            ((Oneal) this).stopChasing();
        }
    }

    //kiểm tra va chạm với vật thể khác
    public boolean collide(Entity other) {
        return (Math.abs(this.x - other.x) < 1 && Math.abs(this.y - other.y) < 1);
    }

    public boolean collide(double x, double y) {
        return (Math.abs(this.x - x) < 1 && Math.abs(this.y - y) < 1);
    }

    public boolean samePosition(Entity other) {
        return (this.x == other.x && this.y == other.y);
    }

    public boolean samePosition(double x, double y) {
        return (this.x == x && this.y == y);
    }

    public boolean sameLine(Entity other) {
        return (x == other.x || y == other.y);
    }

    public double distance(Entity other) {
        return (other.x - x) * (other.x - x) + (other.y - y) * (other.y - y);
    }

    //Kiểm tra va chạm với bom
    public boolean fullCollideWithBomb() {
        for (Bomb bomb : Bomberman.board.bombs) {
            if (!bomb.ignoreEntity(this) && collide(bomb)) {
                return true;
            }
        }
        return false;
    }


    //Kiểm tra va chạm vật thể có size 1
    public boolean fullCollideWithExplosion() {
        int width = 1;
        int height = 1;
        if (x != (int)x) {
            width++;
        }
        if (y != (int)y) {
            height++;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = getMapValue((int) x + j, (int) y + i);
                if (value == Value.EXPLOSION || value == Value.EXPLOSION_BRICK) {
                    return true;
                }
            }
        }
        return false;
    }


    //Kiểm tra va chạm vật thể có size 1 nhưng chỉ xét va chạm kích thước 0.4
    public boolean halfCollideWithExplosion() {
        int width = 1;
        int height = 1;
        double tempX = x + 0.3;
        double tempY = y + 0.3;
        if ((int)tempX + 0.4 < tempX) {
            width++;
        }
        if ((int)tempY + 0.4 < tempY) {
            height++;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = getMapValue((int) tempX + j, (int) tempY + i);
                if (value == Value.EXPLOSION || value == Value.EXPLOSION_BRICK) {
                    return true;
                }
            }
        }
        return false;
    }

    //kiểm tra va chạm với gạch
    public boolean fullCollideWithBrick() {
        for (Brick brick : Bomberman.board.bricks) {
            if (collide(brick)) {
                return true;
            }
        }
        return false;
    }

    //kiểm tra va chạm với tường
    public boolean fullCollideWithWall() {
        int width = 1;
        int height = 1;
        if (x != (int)x) {
            width++;
        }
        if (y != (int)y) {
            height++;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = getMapValue((int) x + j, (int) y + i);
                if (value == Value.WALL) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRemove() {
        return remove;
    }
}
