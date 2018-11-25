package oop.Entity.Movable.Bomb;

import oop.Animation.BombAnimation;
import oop.Setting.Gameplay;

public class NormalBomb extends Bomb {

    private int tickToDetonate;

    public NormalBomb(double x, double y, int radius, boolean superBomb) {
        super(x, y, radius, superBomb);
        tickToDetonate = Gameplay.TICKS_PER_SECOND * 2;
        animation = new BombAnimation(false);
    }

    @Override
    public void update() {
        super.update();
        tickToDetonate--;
        if (tickToDetonate == 0) {
            detonate();
        }
    }
}
