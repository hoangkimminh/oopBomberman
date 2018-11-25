package oop.Entity.Movable.Bomb;

import oop.Animation.BombAnimation;

public class RemoteBomb extends Bomb {

    public RemoteBomb(double x, double y, int radius, boolean superBomb) {
        super(x, y, radius, superBomb);
        animation = new BombAnimation(true);
    }

    @Override
    public void update() {
        super.update();
    }
}
