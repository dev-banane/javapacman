package src;

import java.awt.*;
import java.util.*;

public class Player extends Mover {
    char currDirection;
    char desiredDirection;
    int pelletsEaten;
    boolean teleport;
    boolean stopped = false;

    public Player(int x, int y) {
        teleport = false;
        pelletsEaten = 0;
        pelletX = x / gridSize - 1;
        pelletY = y / gridSize - 1;
        this.lastX = x;
        this.lastY = y;
        this.x = x;
        this.y = y;
        currDirection = 'L';
        desiredDirection = 'L';
    }


    public void demoMove() {
        lastX = x;
        lastY = y;
        if (isChoiceDest()) {
            direction = newDirection();
        }
        switch (direction) {
            case 'L':
                if (isValidDest(x - increment, y)) {
                    x -= increment;
                } else if (y == 9 * gridSize && x < 2 * gridSize) {
                    x = max - gridSize;
                    teleport = true;
                }
                break;
            case 'R':
                if (isValidDest(x + gridSize, y)) {
                    x += increment;
                } else if (y == 9 * gridSize && x > max - gridSize * 2) {
                    x = gridSize;
                    teleport = true;
                }
                break;
            case 'U':
                if (isValidDest(x, y - increment)) {
                    y -= increment;
                }
                break;
            case 'D':
                if (isValidDest(x, y + gridSize)) {
                    y += increment;
                }
                break;
        }
        currDirection = direction;
        frameCount++;
    }

    public void move() {
        lastX = x;
        lastY = y;

        if (x % 20 == 0 && y % 20 == 0 ||
                (desiredDirection == 'L' && currDirection == 'R') ||
                (desiredDirection == 'R' && currDirection == 'L') ||
                (desiredDirection == 'U' && currDirection == 'D') ||
                (desiredDirection == 'D' && currDirection == 'U')
        ) {
            switch (desiredDirection) {
                case 'L':
                    if (isValidDest(x - increment, y))
                        x -= increment;
                    break;
                case 'R':
                    if (isValidDest(x + gridSize, y))
                        x += increment;
                    break;
                case 'U':
                    if (isValidDest(x, y - increment))
                        y -= increment;
                    break;
                case 'D':
                    if (isValidDest(x, y + gridSize))
                        y += increment;
                    break;
            }
        }

        if (lastX == x && lastY == y) {
            switch (currDirection) {
                case 'L':
                    if (isValidDest(x - increment, y))
                        x -= increment;
                    else if (y == 9 * gridSize && x < 2 * gridSize) {
                        x = max - gridSize;
                        teleport = true;
                    }
                    break;
                case 'R':
                    if (isValidDest(x + gridSize, y))
                        x += increment;
                    else if (y == 9 * gridSize && x > max - gridSize * 2) {
                        x = gridSize;
                        teleport = true;
                    }
                    break;
                case 'U':
                    if (isValidDest(x, y - increment))
                        y -= increment;
                    break;
                case 'D':
                    if (isValidDest(x, y + gridSize))
                        y += increment;
                    break;
            }
        } else {
            currDirection = desiredDirection;
        }

        if (lastX == x && lastY == y) {
            stopped = true;
        } else {
            stopped = false;
            frameCount++;
        }
    }

}
