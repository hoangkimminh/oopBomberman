package oop.Entity.Static;

import oop.Animation.Animation;
import oop.Animation.BrickAnimation;
import oop.Graphic.Sprite;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class Brick extends StaticEntity {

    private int tick = Gameplay.TICKS_PER_SECOND / 2;
    private boolean destroy = false;
    private BrickAnimation animation = new BrickAnimation();

    public Brick(double x, double y) {
        super(x, y);
        setMapValue(Value.BRICK);
    }

    @Override
    public void update() {
        if (!destroy && getMapValue() == Value.EXPLOSION_BRICK) {
            destroy();
        }
        if (destroy) {
            tick--;
            if (tick == 0) {
                remove();
                setMapValue(Value.GRASS);
            }
        }
        animation.update();
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }

    public void destroy() {
        destroy = true;
        animation.destroy();
    }
}
