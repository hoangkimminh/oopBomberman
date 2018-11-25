package oop.Animation;

import oop.Graphic.Sprite;

import java.awt.image.BufferedImage;

public class PortalAnimation extends Animation {

    private boolean open = false;

    public PortalAnimation() {
        tickPerImage = 20;
    }

    @Override
    public void update() {
        if (open) {
            currentTick++;
            if (currentTick % tickPerImage == 0) {
                state += add;
                if (state == 5) {
                    state = 1;
                }
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        if (!open) {
            return Sprite.portal[0];
        }
        return Sprite.portal[state];
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
