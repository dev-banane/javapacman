package src;

import java.awt.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.*;
import java.io.*;

public class Mover {
    int frameCount = 0;
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
}
