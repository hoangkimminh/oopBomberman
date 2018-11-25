package oop.Entity.Static;

import oop.Audio.AudioManager;
import oop.Entity.Movable.Player.Player;
import oop.Graphic.Sprite;
import oop.Setting.Value;

import java.awt.image.BufferedImage;

public class Item extends StaticEntity {

    private int type;
    private boolean active = false;

    public Item(double x, double y, int type) {
        super(x, y);
        this.type = type;
    }

    public void activate(Player player) {
        if (!isRemove() && active) {
            AudioManager.itemActivate.play();
            switch (type) {
                case Value.BOMB_ITEM:
                    player.increaseBombCapacity();
                    break;
                case Value.FIRE_ITEM:
                    player.increaseBombRadius();
                    break;
                case Value.BOMB_PASS_ITEM:
                    player.enableBombIgnore();
                    break;
                case Value.BRICK_PASS_ITEM:
                    player.enableBrickIgnore();
                    break;
                case Value.SUPER_BOMB_ITEM:
                    player.enableSuperBomb();
                    break;
                case Value.REMOTE_BOMB_ITEM:
                    player.enableRemoteBomb();
                    break;
                case Value.KICK_ITEM:
                    player.enableKick();
                    break;
                case Value.SPEED_ITEM:
                    player.increaseSpeed();
                    break;
            }
            remove();
        }
    }

    @Override
    public void update() {
        if (getMapValue(x, y) == Value.EXPLOSION) {
            remove();
        } else if (getMapValue(x, y) == Value.GRASS) {
            active = true;
        }
    }

    @Override
    public BufferedImage getImage() {
        return Sprite.item[type];
    }
}
