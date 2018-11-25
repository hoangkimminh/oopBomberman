package oop.Entity.Movable;

import oop.Animation.Animation;
import oop.Entity.Entity;

import java.awt.image.BufferedImage;

public abstract class MovableEntity extends Entity {

    protected double speed;
    protected int direction;
    protected Animation animation;

    protected MovableEntity(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void positionQuantimize() {
        if (Math.abs(Math.round(x) - x) < speed * 0.99) {
            x = Math.round(x);
        }
        if (Math.abs(Math.round(y) - y) < speed * 0.99) {
            y = Math.round(y);
        }
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }
}
