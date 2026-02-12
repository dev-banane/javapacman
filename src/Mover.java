package src;

import java.awt.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.*;
import java.io.*;

public class Mover {
    int x, y;
    int lastX, lastY;
    char direction;
    int frameCount = 0;
    int pelletX, pelletY;
    int[][] map;
    int gridSize;
    int max;
    int increment;

    public Mover() {
        gridSize = 20;
        increment = 4;
        max = 400;
        map = new int[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                map[i][j] = 0;
            }
        }
    }

    public void updateState(int[][] map) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                this.map[i][j] = map[i][j];
            }
        }
    }

    public boolean isValidDest(int x, int y) {
        if ((((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400 && map[x / 20 - 1][y / 20 - 1] == 0) {
            return true;
        }
        return false;
    }

    public boolean isChoiceDest() {
        return x % gridSize == 0 && y % gridSize == 0;
    }

    public void updatePellet() {
        if (isChoiceDest()) {
            pelletX = x / gridSize - 1;
            pelletY = y / gridSize - 1;
        }
    }

    public char newDirection() {
        int random;
        char backwards = 'U';
        int lookX = x, lookY = y;
        Set<Character> set = new HashSet<Character>();
        switch (direction) {
            case 'L':
                backwards = 'R';
                break;
            case 'R':
                backwards = 'L';
                break;
            case 'U':
                backwards = 'D';
                break;
            case 'D':
                backwards = 'U';
                break;
        }

        char newDir = backwards;
        while (newDir == backwards || !isValidDest(lookX, lookY)) {
            if (set.size() == 3) {
                newDir = backwards;
                break;
            }

            lookX = x;
            lookY = y;

            random = (int) (Math.random() * 4) + 1;
            if (random == 1) {
                newDir = 'L';
                lookX -= increment;
            } else if (random == 2) {
                newDir = 'R';
                lookX += gridSize;
            } else if (random == 3) {
                newDir = 'U';
                lookY -= increment;
            } else if (random == 4) {
                newDir = 'D';
                lookY += gridSize;
            }
            if (newDir != backwards) {
                set.add(newDir);
            }
        }
        return newDir;
    }
}
