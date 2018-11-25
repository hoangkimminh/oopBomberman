package oop.Entity.Movable.Enemy;

import oop.Animation.EnemyAnimation;
import oop.Bomberman;
import oop.Entity.Movable.Bomb.Bomb;
import oop.Entity.Movable.Player.Player;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.util.Arrays;
import java.util.Stack;

public class Pass extends Enemy {

    public Pass(double x, double y) {
        super(x, y, Gameplay.PASS_SPEED);
        bombIgnore = true;
        score = Value.SCORE[2];
        animation = new EnemyAnimation(Value.PASS_ENEMY, 2);
    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            if (samePosition(getMapX(), getMapY())) {
                if (random.nextInt(2) == 0) {
                    avoidBomb();
                }
            }
            wander();
        }
        ((EnemyAnimation) animation).update(direction);
    }

    private void avoidBomb() {
        int[][] matrix = new int[Bomberman.board.map.length][Bomberman.board.map[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = getMapValue(j, i);
            }
        }
        for (Bomb bomb : Bomberman.board.bombs) {
            int radius = bomb.getRadius();
            int bombX = bomb.getMapX();
            int bombY = bomb.getMapY();
            matrix[bombY][bombX] = Value.EXPLOSION;
            for (int i = 1; i <= radius; i++) {
                if (getMapValue(bombX, bombY + i) == Value.WALL ||
                    getMapValue(bombX, bombY + i) == Value.BRICK ||
                    getMapValue(bombX, bombY + i) == Value.EXPLOSION_BRICK) {
                    break;
                }
                matrix[bombY + i][bombX] = Value.EXPLOSION;
            }
            for (int i = 1; i <= radius; i++) {
                if (getMapValue(bombX, bombY - i) == Value.WALL ||
                    getMapValue(bombX, bombY - i) == Value.BRICK ||
                    getMapValue(bombX, bombY - i) == Value.EXPLOSION_BRICK) {
                    break;
                }
                matrix[bombY - i][bombX] = Value.EXPLOSION;
            }
            for (int i = 1; i <= radius; i++) {
                if (getMapValue(bombX + i, bombY) == Value.WALL ||
                    getMapValue(bombX + i, bombY) == Value.BRICK ||
                    getMapValue(bombX + i, bombY) == Value.EXPLOSION_BRICK) {
                    break;
                }
                matrix[bombY][bombX + i] = Value.EXPLOSION;
            }
            for (int i = 1; i <= radius; i++) {
                if (getMapValue(bombX - i, bombY) == Value.WALL ||
                    getMapValue(bombX - i, bombY) == Value.BRICK ||
                    getMapValue(bombX - i, bombY) == Value.EXPLOSION_BRICK) {
                    break;
                }
                matrix[bombY][bombX - i] = Value.EXPLOSION;
            }
        }
        if (matrix[(int)y][(int)x] != Value.GRASS) {
            direction = random.nextInt(4);
            for (int i = direction; i < direction + 4; i++) {
                if (i % 4 == 1 && matrix[(int)y - 1][(int)x] == Value.GRASS) {
                    direction = 0;
                    break;
                }
                if (i % 4 == 3 && matrix[(int)y][(int)x + 1] == Value.GRASS) {
                    direction = 1;
                    break;
                }
                if (i % 4 == 0 && matrix[(int)y + 1][(int)x] == Value.GRASS) {
                    direction = 2;
                    break;
                }
                if (i % 4 == 2 && matrix[(int)y][(int)x - 1] == Value.GRASS) {
                    direction = 3;
                    break;
                }
            }
        }
        switch (direction) {
            case 0:
                if (matrix[(int)y - 1][(int)x] == Value.EXPLOSION) {
                    direction = (direction + 1 + random.nextInt(3)) % 4;
                }
                break;
            case 1:
                if (matrix[(int)y][(int)x + 1] == Value.EXPLOSION) {
                    direction = (direction + 1 + random.nextInt(3)) % 4;
                }
                break;
            case 2:
                if (matrix[(int)y + 1][(int)x] == Value.EXPLOSION) {
                    direction = (direction + 1 + random.nextInt(3)) % 4;
                }
                break;
            case 3:
                if (matrix[(int)y][(int)x - 1] == Value.EXPLOSION) {
                    direction = (direction + 1 + random.nextInt(3)) % 4;
                }
                break;
        }
    }
}
