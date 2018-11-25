package oop.Entity.Static;

import oop.Animation.PortalAnimation;
import oop.Bomberman;
import oop.Entity.Movable.Enemy.*;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class Portal extends StaticEntity {

    private boolean summonMonster = false;
    private PortalAnimation animation;

    public Portal(double x, double y) {
        super(x, y);
        animation = new PortalAnimation();
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }

    @Override
    public void update() {
        if (getMapValue(x, y) == Value.EXPLOSION) {
            summonMonster = true;
        }
        if (summonMonster && getMapValue(x, y) == Value.GRASS) {
            for (int i = 0; i < 2; i++) {
                switch (random.nextInt(5)) {
                    case 0:
                        Bomberman.board.enemies.add(new Doria(x, y));
                        break;
                    case 1:
                        Bomberman.board.enemies.add(new Ballom(x, y));
                        break;
                    case 2:
                        Bomberman.board.enemies.add(new Pass(x, y));
                        break;
                    case 3:
                        Bomberman.board.enemies.add(new Oneal(x, y));
                        break;
                    case 4:
                        Bomberman.board.enemies.add(new Boyon(x, y));
                        break;
                }
            }
            summonMonster = false;
            animation.setOpen(false);
        }
        if (Bomberman.board.enemies.isEmpty()) {
            animation.setOpen(true);
        }
        animation.update();
    }
}
