package oop.Entity.Static;

import oop.Animation.Animation;
import oop.Animation.ExplosionAnimation;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class Explosion extends StaticEntity {

    private int tick = Gameplay.TICKS_PER_SECOND / 2;
    private ExplosionAnimation animation;

    public Explosion(double x, double y, int type) {
        super(x, y);
        if (getMapValue() == Value.BRICK) {
            setMapValue(x, y, Value.EXPLOSION_BRICK);
        } else if (getMapValue() == Value.GRASS){
            setMapValue(x, y, Value.EXPLOSION);
        }
        animation = new ExplosionAnimation(type);
    }

    @Override
    public void update() {
        tick--;
        if (tick == 0) {
            setMapValue(x, y, Value.GRASS);
            remove();
        }
        animation.update();
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }
}
