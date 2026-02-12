package src;

import java.awt.*;
import java.util.*;

public class Ghost extends Mover {
    public enum Algorithm {
        RANDOM, DIJKSTRA, ASTAR, TEAM, RANDOM_EACH
    }

    public Algorithm alg = Algorithm.RANDOM;

    char direction;
    int lastX;
    int lastY;
    int x;
    int y;
    int pelletX, pelletY;
    int lastPelletX, lastPelletY;

    public Ghost(int x, int y) {
        direction = 'L';
        alg = Algorithm.RANDOM_EACH;
        pelletX = x / gridSize - 1;
        pelletY = y / gridSize - 1;
        lastPelletX = pelletX;
        lastPelletY = pelletY;
        this.lastX = x;
        this.lastY = y;
        this.x = x;
        this.y = y;
    }

    public void updatePellet() {
        int tempX = x / gridSize - 1;
        int tempY = y / gridSize - 1;
        if (tempX != pelletX || tempY != pelletY) {
            lastPelletX = pelletX;
            lastPelletY = pelletY;
            pelletX = tempX;
            pelletY = tempY;
        }
    }

    public boolean isChoiceDest() {
        return x % gridSize == 0 && y % gridSize == 0;
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

    public void move() {
        lastX = x;
        lastY = y;

        if (isChoiceDest()) {
            direction = newDirection();
        }

        switch (direction) {
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

    /**
     * Ghost movement logic: chooses direction based on the current algorithm.
     */
    public void move(int playerX, int playerY, char playerDirection, java.util.List<Ghost> ghosts, Algorithm boardAlg) {
        lastX = x;
        lastY = y;

        if (isChoiceDest()) {
            Algorithm currentAlg = boardAlg;
            if (boardAlg == Algorithm.RANDOM_EACH) {
                if (alg == Algorithm.RANDOM_EACH) {
                    Algorithm[] algs = {Algorithm.RANDOM, Algorithm.DIJKSTRA, Algorithm.ASTAR, Algorithm.TEAM};
                    alg = algs[(int) (Math.random() * algs.length)];
                }
                currentAlg = alg;
            }

            switch (currentAlg) {
                case DIJKSTRA:
                    direction = findPath(playerX, playerY, false, ghosts);
                    break;
                case ASTAR:
                    direction = findPath(playerX, playerY, true, ghosts);
                    break;
                case TEAM:
                    direction = team(playerX, playerY, playerDirection, ghosts);
                    break;
                case RANDOM:
                default:
                    direction = newDirection();
                    break;
            }
        }

        switch (direction) {
            case 'L':
                if (isValidDest(x - increment, y)) x -= increment;
                break;
            case 'R':
                if (isValidDest(x + gridSize, y)) x += increment;
                break;
            case 'U':
                if (isValidDest(x, y - increment)) y -= increment;
                break;
            case 'D':
                if (isValidDest(x, y + gridSize)) y += increment;
                break;
        }
    }

    /**
     * Ghost teamwork: determines target based on ghost index
     */
    private char team(int playerX, int playerY, char playerDirection, java.util.List<Ghost> ghosts) {
        int index = ghosts.indexOf(this);
        int targetX = playerX, targetY = playerY;

        switch (index) {
            case 1: // Pinky: Targets 4 tiles ahead of player (ambush)
                if (playerDirection == 'L') targetX -= 4 * gridSize;
                else if (playerDirection == 'R') targetX += 4 * gridSize;
                else if (playerDirection == 'U') targetY -= 4 * gridSize;
                else if (playerDirection == 'D') targetY += 4 * gridSize;
                break;
            case 2: // Inky: Flanks player by targeting relative to player position
                if (playerDirection == 'L' || playerDirection == 'R') targetY += 4 * gridSize;
                else targetX += 4 * gridSize;
                break;
            case 3: // Clyde: Flanks player from opposite side
                if (playerDirection == 'L' || playerDirection == 'R') targetY -= 4 * gridSize;
                else targetX -= 4 * gridSize;
                break;
        }
        return findPath(targetX, targetY, true, ghosts);
    }

    /**
     * Pathfinding algorithm: A* search if useHeuristic is true, Dijkstra's if false.
     * Finds the shortest path to (targetX, targetY) while avoiding walls.
     */
    private char findPath(int targetX, int targetY, boolean useHeuristic, java.util.List<Ghost> ghosts) {
        int startX = Math.max(0, Math.min(18, x / gridSize - 1));
        int startY = Math.max(0, Math.min(18, y / gridSize - 1));
        int destX = Math.max(0, Math.min(18, targetX / gridSize - 1));
        int destY = Math.max(0, Math.min(18, targetY / gridSize - 1));

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Node[][] nodes = new Node[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) nodes[i][j] = new Node(i, j);
        }

        Node startNode = nodes[startX][startY];
        startNode.gScore = 0;
        startNode.fScore = useHeuristic ? heuristic(startX, startY, destX, destY) : 0;
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.x == destX && current.y == destY) return reconstructPath(current);

            int[][] neighbors = {{0, -1, 'U'}, {0, 1, 'D'}, {-1, 0, 'L'}, {1, 0, 'R'}};
            for (int[] neighbor : neighbors) {
                int nx = current.x + neighbor[0], ny = current.y + neighbor[1];
                if (nx >= 0 && nx < 19 && ny >= 0 && ny < 19 && map[nx][ny] == 0) { // Check map boundaries and wall collisions (map[nx][ny] == 0 means path)
                    double moveCost = (isBackwards((char) neighbor[2], direction) && current == startNode) ? 10 : 1; // Penalty for reversing direction to prevent erratic movement
                    double tentativeGScore = current.gScore + moveCost;

                    if (tentativeGScore < nodes[nx][ny].gScore) {
                        nodes[nx][ny].parent = current;
                        nodes[nx][ny].dir = (char) neighbor[2];
                        nodes[nx][ny].gScore = tentativeGScore;
                        nodes[nx][ny].fScore = tentativeGScore + (useHeuristic ? heuristic(nx, ny, destX, destY) : 0);
                        if (!openSet.contains(nodes[nx][ny])) openSet.add(nodes[nx][ny]);
                    }
                }
            }
        }
        return newDirection();
    }

    private boolean isBackwards(char newDir, char currentDir) {
        if (newDir == 'L' && currentDir == 'R') return true;
        if (newDir == 'R' && currentDir == 'L') return true;
        if (newDir == 'U' && currentDir == 'D') return true;
        if (newDir == 'D' && currentDir == 'U') return true;
        return false;
    }

    /**
     * Manhattan distance heuristic for A* pathfinding.
     */
    private double heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Traces back from the target node to find the first move required to reach it.
     */
    private char reconstructPath(Node target) {
        Node curr = target;
        if (curr.parent == null) return newDirection();
        while (curr.parent.parent != null) {
            curr = curr.parent;
        }
        return curr.dir;
    }

    /**
     * Represents a cell in the pathfinding grid.
     * gScore: cost from start node
     * fScore: gScore + heuristic (estimated cost to destination)
     */
    private class Node implements Comparable<Node> {
        int x, y;
        double gScore = Double.POSITIVE_INFINITY;
        double fScore = Double.POSITIVE_INFINITY;
        Node parent = null;
        char dir;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }
}
