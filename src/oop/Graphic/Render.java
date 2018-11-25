package oop.Graphic;

import oop.Bomberman;
import oop.Entity.Entity;
import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Enemy.Enemy;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Explosion;
import oop.Setting.Size;
import oop.Setting.Value;

import java.awt.*;
import java.awt.image.BufferedImage;

import static oop.Bomberman.graphics;

public class Render {

    private int xOffset;
    private int yOffset;
    private BufferedImage background;

    public void render(int mainCharacter) {
        calculateOffset(mainCharacter);
        //draw background
        graphics.drawImage(background, -xOffset, -yOffset, null);
        //draw item
        for (Entity temp : Bomberman.board.items) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }
        //draw portal
        for (Entity temp : Bomberman.board.portals) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }
        //draw brick
        for (Entity temp : Bomberman.board.bricks) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }
        //draw bomb
        for (Bomb temp : Bomberman.board.bombs) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }
        //draw explosion
        for (Explosion temp : Bomberman.board.explosions) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }

        //draw enemy
        for (Enemy temp : Bomberman.board.enemies) {
            if (temp.isRemove()) {
                continue;
            }
            graphics.drawImage(temp.getImage(),
                    temp.getScreenX() - xOffset,
                    temp.getScreenY() - yOffset,
                    Size.RTILE_SIZE, Size.RTILE_SIZE, null);
        }
        //draw player
        for (Player temp : Bomberman.board.players) {
            BufferedImage pImage = temp.getImage();
            if (pImage == null) {
                continue;
            }
            int imageWidth = Size.SCALE * pImage.getWidth();
            int imageHeight = Size.SCALE * pImage.getHeight();
            graphics.drawImage(pImage,
                    temp.getScreenX() - (imageWidth - Size.RTILE_SIZE) / 2 - xOffset,
                    temp.getScreenY() - imageHeight + Size.RTILE_SIZE - yOffset,
                    imageWidth, imageHeight, null);

        }
        showGameInfo();
        Bomberman.bufferStrategy.show();
    }

    public void showGameInfo() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Size.SCREEN_WIDTH, 60);
        graphics.setColor(Color.WHITE);
        Bomberman.graphics.drawString("Time: " + Integer.toString(Bomberman.board.time), 150, 40);
        Bomberman.graphics.drawString("Score: " + Integer.toString(Bomberman.board.score), 600, 40);
    }

    public void redrawBackground() {
        background = new BufferedImage(Bomberman.board.map[0].length * Size.RTILE_SIZE, Bomberman.board.map.length * Size.RTILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.getGraphics();
        for (int y = 0; y < Bomberman.board.map.length; y++) {
            for (int x = 0; x < Bomberman.board.map[0].length; x++) {
                if (Bomberman.board.map[y][x] == Value.WALL) {
                    g.drawImage(Sprite.undestroyableObject[0], x * Size.RTILE_SIZE, y * Size.RTILE_SIZE, Size.RTILE_SIZE, Size.RTILE_SIZE, null);
                } else {
                    g.drawImage(Sprite.grass, x * Size.RTILE_SIZE, y * Size.RTILE_SIZE, Size.RTILE_SIZE, Size.RTILE_SIZE, null);
                }
            }
        }
    }

    public void calculateOffset(int mainCharacter) {
        if (Bomberman.board.players.isEmpty()) {
            return;
        }
        int playerX = Bomberman.board.players.get(mainCharacter).getScreenX() + Size.RTILE_SIZE / 2;
        int playerY = Bomberman.board.players.get(mainCharacter).getScreenY() + Size.RTILE_SIZE / 2;
        if (playerX >= Size.SCREEN_WIDTH / 2 && playerX <= background.getWidth() - Size.SCREEN_WIDTH / 2) {
            xOffset = playerX - Size.SCREEN_WIDTH / 2;
        } else if (playerX > background.getWidth() - Size.SCREEN_WIDTH / 2) {
            xOffset = background.getWidth() - Size.SCREEN_WIDTH;
        } else {
            xOffset = 0;
        }
        int height = Size.SCREEN_HEIGHT - 60;
        if (playerY >= height / 2 && playerY <= background.getHeight() - height / 2) {
            yOffset = playerY - height / 2 - 60;
        } else if (playerY < height / 2) {
            yOffset = -60;
        } else {
            yOffset = background.getHeight() - height - 60;
        }
    }
}
