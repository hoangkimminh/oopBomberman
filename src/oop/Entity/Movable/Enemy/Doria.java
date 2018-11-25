package oop.Entity.Movable.Enemy;

import oop.Animation.EnemyAnimation;
import oop.Bomberman;
import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Player.Player;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Stack;

public class Doria extends Enemy {

    private boolean chasing;
    private int wanderingTick;
    private String stringPath;
    private Stack<Integer> ways = new Stack<>();

    public Doria(double x, double y) {
        super(x, y, Gameplay.DORIA_SPEED);
        brickIgnore = true;
        score = Value.SCORE[3];
        animation = new EnemyAnimation(Value.DORIA_ENEMY, 3);
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            wanderingTick--;
            positionQuantimize();
            if (!chasing && (int)x == x && (int)y == y && wanderingTick <= 0) {
                for (Player player : Bomberman.board.players) {
                    if (distance(player) <= 25) {
                        if (findPath(player)) {
                            chasing = true;
                        }
                        break;
                    }
                }
            }
            //move
            if (!chasing) {
                wander();
            } else {
                moveToPlayer();
            }
        }
        ((EnemyAnimation) animation).update(direction);
    }

    private void moveToPlayer() {
        if (x == getMapX() && y == getMapY()) {
            if (ways.isEmpty()) {
                stopChasing();
                return;
            } else {
                direction = ways.pop();
            }
        }
        double nextX = x;
        double nextY = y;
        switch (direction) {
            case Value.UP:
                nextY -= speed;
                break;
            case Value.DOWN:
                nextY += speed;
                break;
            case Value.LEFT:
                nextX -= speed;
                break;
            case Value.RIGHT:
                nextX += speed;
                break;
        }
        for (Bomb bomb : Bomberman.board.bombs) {
            if (!bomb.ignoreEntity(this)) {
                if (bomb.collide(nextX, nextY)) {
                    direction = random.nextInt(4);
                    stopChasing();
                    return;
                }
            }
        }
        x = nextX;
        y = nextY;
    }

    @Override
    public BufferedImage getImage() {
        return animation.getImage();
    }

    private boolean findPath(Player target) {
        ways.clear();
        int[][] matrix = new int[Bomberman.board.map.length][Bomberman.board.map[0].length];
        for (int[] temp : matrix) {
            Arrays.fill(temp, 1000);
        }
        stringPath = "";
        pathFinding(getMapX(), getMapY(), target.getMapX(), target.getMapY(), matrix, "", 0);
        if (stringPath.isEmpty()) {
            return false;
        }
        for (int i = stringPath.length() - 1; i >= 0; i--) {
            ways.push(stringPath.charAt(i) - '0');
        }
        return true;
    }

    private void pathFinding(int fromX, int fromY, int toX, int toY, int[][] matrix, String path, int value) {
        if (fromX == toX && fromY == toY && matrix[fromY][fromX] > value) {
            matrix[fromY][fromX] = value;
            stringPath = path;
            return;
        }
        if (matrix[fromY][fromX] <= value) {
            return;
        }
        for (Bomb bomb : Bomberman.board.bombs) {
            if (bomb.samePosition(fromX, fromY)) {
                return;
            }
        }
        int tileValue = getMapValue(fromX, fromY);
        if (tileValue == Value.WALL) {
            return;
        }
        matrix[fromY][fromX] = value;
        value++;
        pathFinding(fromX + 1, fromY, toX, toY, matrix, path + "1", value);
        pathFinding(fromX - 1, fromY, toX, toY, matrix, path + "3", value);
        pathFinding(fromX, fromY + 1, toX, toY, matrix, path + "2", value);
        pathFinding(fromX, fromY - 1, toX, toY, matrix, path + "0", value);
    }

    private void stopChasing() {
        wanderingTick = Gameplay.TICKS_PER_SECOND * 5;
        chasing = false;
    }
}
