package oop.Animation;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Animation implements Serializable {

    int tickPerImage = 10;
    int currentTick = 0;
    int state = 0;
    int add = 1;

    public abstract void update();
    public abstract BufferedImage getImage();
}
