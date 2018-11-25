package oop.Entity.Movable.Enemy;

import oop.Animation.DragonAnimation;
import oop.Bomberman;
import oop.Entity.Movable.Player.Player;
import oop.Entity.Static.Portal;
import oop.Setting.Gameplay;
import oop.Setting.Value;

import java.util.Arrays;

public class Dragon extends Enemy {

    private int length;
    private int health = 2;
    private Dragon nextBody = null;
    private Dragon prevBody = null;
    private static String stringPath;
    private static int immuneBombTick = 0;

    public Dragon(double x, double y, int length) {
        super(x, y, Gameplay.DRAGON_SPEED);
        score = Value.SCORE[4];
        brickIgnore = true;
        bombIgnore = true;
        this.length = length;
        tickToRemove = Gameplay.TICKS_PER_SECOND * 5;
        animation = new DragonAnimation(length);
        if (length == Gameplay.DRAGON_LENGTH) {
            direction = random.nextInt(4);
        } else {
            direction = -1;
        }
        if (length > 0) {
            nextBody = new Dragon(x, y, length - 1);
            nextBody.prevBody = this;
            Bomberman.board.enemies.add(nextBody);
        }
    }

    private void passDirection(Dragon next) {
        if (next.nextBody == null) {
            return;
        }
        passDirection(next.nextBody);
        next.nextBody.direction = next.direction;
    }

    @Override
    public void update() {
        if (length != Gameplay.DRAGON_LENGTH) {
            return;
        }
        if (alive) {
            immuneBombTick--;
            boolean bombImpact = false;
            if (immuneBombTick < 0 && fullCollideWithExplosion()) {
                if (checkEqualHealth(this)) {
                    if (health == 0) {
                        if (!loseBody()) {
                            dead();
                        }
                    } else {
                        loseHealth(this);
                    }
                    bombImpact = true;
                }
            }

            if (!headMove()) {
                findPath();
                return;
            }

            //update body
            Dragon temp = this;
            while (temp.nextBody != null) {
                temp = temp.nextBody;
                if (immuneBombTick < 0 && temp.fullCollideWithExplosion()) {
                    if (checkEqualHealth(temp)) {
                        loseHealth(temp);
                        bombImpact = true;
                    }
                }
                temp.bodyMove();
                ((DragonAnimation) temp.animation).update(temp.direction);
            }

            if (bombImpact) {
                immuneBombTick = Gameplay.TICKS_PER_SECOND;
            }

            positionQuantimize();
            if (samePosition(getMapX(), getMapY())) {
                passDirection(this);
                findPath();
            }
            ((DragonAnimation)animation).update(direction);
        } else {
            tickToRemove--;
            Dragon temp = this;
            while (temp != null) {
                if (tickToRemove == 0) {
                    temp.remove();
                }
                temp.animation.update();
                temp = temp.nextBody;
            }
            if (tickToRemove == 0 && Bomberman.board.portals.isEmpty()) {
                Bomberman.board.portals.add(new Portal(getMapX(), getMapY()));
            }
        }
    }

    private void bodyMove() {
        switch (direction) {
            case Value.UP:
                y -= speed;
                break;
            case Value.DOWN:
                y += speed;
                break;
            case Value.LEFT:
                x -= speed;
                break;
            case Value.RIGHT:
                x += speed;
                break;
            default:
                return;
        }
        positionQuantimize();
    }

    private boolean headMove() {
        double tempX = x;
        double tempY = y;
        switch (direction) {
            case Value.UP:
                y -= speed;
                break;
            case Value.DOWN:
                y += speed;
                break;
            case Value.LEFT:
                x -= speed;
                break;
            case Value.RIGHT:
                x += speed;
                break;
            default:
                return false;
        }
        if (fullCollideWithWall()) {
            x = tempX;
            y = tempY;
            positionQuantimize();
            return false;
        }
        return true;
    }

    private void changeDirection() {
        direction = random.nextInt(4);
        for (int i = direction; i < direction + 4; i++) {
            if (i % 4 == 1 && getMapValue(x, y - 1) != Value.WALL) {
                direction = 0;
                break;
            }
            if (i % 4 == 3 && getMapValue(x + 1, y) != Value.WALL) {
                direction = 1;
                break;
            }
            if (i % 4 == 0 && getMapValue(x, y + 1) != Value.WALL) {
                direction = 2;
                break;
            }
            if (i % 4 == 2 && getMapValue(x - 1, y) != Value.WALL) {
                direction = 3;
                break;
            }
        }
    }

    private boolean checkEqualHealth(Dragon dragon) {
        Dragon prev = dragon.prevBody;
        Dragon next = dragon.nextBody;
        while (prev != null) {
            if (prev.health > dragon.health) {
                return false;
            }
            prev = prev.prevBody;
        }
        while (next != null) {
            if (next.health > dragon.health) {
                return false;
            }
            next = next.nextBody;
        }
        return true;
    }

    private boolean loseBody() {
        int length = 0;
        Dragon tail = this;
        while (tail.nextBody != null) {
            tail = tail.nextBody;
            length++;
        }
        if (length < 3) {
            return false;
        }
        tail.remove();
        tail = tail.prevBody;
        tail.nextBody = null;
        ((DragonAnimation) tail.animation).setLength(0);
        return true;
    }

    private void loseHealth(Dragon dragon) {
        if (dragon.health > 0) {
            dragon.health--;
        }
        ((DragonAnimation) dragon.animation).loseHealth();
    }

    @Override
    public void dead() {
        Dragon temp = this;
        while (temp != null) {
            temp.alive = false;
            ((DragonAnimation) temp.animation).dead();
            temp = temp.nextBody;
        }
    }

    private void findPath() {
        int[][] matrix = new int[Bomberman.board.map.length][Bomberman.board.map[0].length];
        for (int[] temp : matrix) {
            Arrays.fill(temp, 1000);
        }
        double minDistance = 99999999.0f;
        Player target = null;
        for (int i = 0; i < Bomberman.board.players.size(); i++) {
            Player player = Bomberman.board.players.get(i);
            if (player.isAlive() && distance(player) < minDistance) {
                minDistance = distance(player);
                target = player;
            }
        }
        if (target == null) {
            changeDirection();
            return;
        }
        stringPath = "";
        pathFinding(getMapX(), getMapY(), target.getMapX(), target.getMapY(), matrix, "", 0);
        if (stringPath.isEmpty()) {
            changeDirection();
            return;
        }
        direction = Integer.parseInt(stringPath.substring(0, 1));
    }

    private void pathFinding(int fromX, int fromY, int toX, int toY, int[][] matrix, String path, int value) {
        if (fromX == toX && fromY == toY && matrix[fromY][fromX] >= value) {
            if (matrix[fromY][fromX] == value) {
                if (random.nextInt(2) == 0) {
                    stringPath = path;
                }
            } else {
                matrix[fromY][fromX] = value;
                stringPath = path;
            }
            return;
        }
        if (matrix[fromY][fromX] <= value) {
            return;
        }
        if (getMapValue(fromX, fromY) == Value.WALL) {
            return;
        }
        matrix[fromY][fromX] = value;
        value++;
        pathFinding(fromX + 1, fromY, toX, toY, matrix, path + "1", value);
        pathFinding(fromX - 1, fromY, toX, toY, matrix, path + "3", value);
        pathFinding(fromX, fromY + 1, toX, toY, matrix, path + "2", value);
        pathFinding(fromX, fromY - 1, toX, toY, matrix, path + "0", value);
    }
}
