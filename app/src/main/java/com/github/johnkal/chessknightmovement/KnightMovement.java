package com.github.johnkal.chessknightmovement;

import java.util.LinkedList;

/**
 * A class that calculates the paths of moves that a knight could do according
 * to start and end position.
 */
public class KnightMovement {
    int N = 6;
    int[] knightPos = new int[2];
    int[] knightTarget = new int[2];

    //positions in x,y that knight can move
    int dx[] = {-2, -1, 1, 2, -2, -1, 1, 2};
    int dy[] = {-1, -2, -2, -1, 1, 2, 2, 1};
    boolean[][] visitableCells;

    private Cell firstCell;

    private LinkedList<LinkedList<Cell>> paths = new LinkedList<>();
    private LinkedList<Cell> path = new LinkedList<>();
    private LinkedList<LinkedList<Cell>> fullPaths = new LinkedList<>();
    private LinkedList<Cell> fullPath = new LinkedList<>();

    public KnightMovement(int[] knightPos, int[] knightTarget, int N) {
        this.knightPos = knightPos;
        this.knightTarget = knightTarget;
        this.N = N;
    }

    /**
     * A class that represents the cells that moves the knight.
     */
    class Cell
    {
        public int x; // row of cell
        public int y; // column of cell
        public int moveX; // number of x moves from previous cell.
        public int moveY; // number of y moves from previous cell.

        public Cell(int x, int y, int moveX, int moveY) {
            this.x = x;
            this.y = y;
            this.moveX = moveX;
            this.moveY = moveY;
        }
    }

    public LinkedList<LinkedList<Cell>> getPaths() {
        return paths;
    }

    /**
     * Initialize the array with visited cells. Add the start position as a first position of a path.
     * @return
     */
    public LinkedList<LinkedList<Cell>> calculateMoves() {
        visitableCells = new boolean[N + 1][N + 1];

        clearVisitedCells();

        visitableCells[knightPos[0]][knightPos[1]] = true;

        firstCell = new Cell(knightPos[0], knightPos[1], 0,0);
        path.add(firstCell);

        calculateMoveCells(firstCell, path);
        calculateMiddleCells();

        return fullPaths;
    }

    /**
     * Calculate possible 3 moves for target position.
     * Implement based on algorithm Depth First Search.
     * (https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/)
     * @param nextCell
     * @param path
     */
    private void calculateMoveCells(Cell nextCell, LinkedList<Cell> path) {

        int x, y;

        if (path.size() == 4) {
            return;
        }

        for (int i = 0; i < 8; i++) {

            x = nextCell.x + dx[i];
            y = nextCell.y + dy[i];

            if (isInside(x, y, N) && !visitableCells[x][y]) {
                visitableCells[x][y] = true;
                Cell cell = new Cell(x, y, dx[i], dy[i]);
                path.add(cell);
                if (path.size() == 4 && checkIfIsTargetPosition(cell)) {

                }
                else {
                    calculateMoveCells(cell, path);
                }
                path.remove(cell);
                visitableCells[x][y] = false;
            }
        }
    }

    /**
     * Calculates the middle cells for the "L" moves of a knight to draw the appropriates paths lines.
     */
    private void calculateMiddleCells() {
        for (LinkedList<Cell> path : paths) {
            fullPath = new LinkedList<>();
            for (int i = 0; i < path.size() - 1; i++) {
                Cell middleMoveCell = new Cell(path.get(i).x + path.get(i + 1).moveX,
                        path.get(i).y, 0, 0);
                fullPath.add(path.get(i));
                fullPath.add(middleMoveCell);
            }
            fullPath.add(path.get(path.size() - 1));
            fullPaths.add(fullPath);
        }
    }

    /**
     * Clear the array with visited cells.
     */
    private void clearVisitedCells () {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                visitableCells[i][j] = false;
            }
        }
    }

    /**
     * A method that check if a move is inside chess board limits.
     * @param x
     * @param y
     * @param N
     * @return
     */
    boolean isInside(int x, int y, int N)
    {
        if (x >= 0 && x < N && y >= 0 && y < N)
            return true;
        return false;
    }

    /**
     * A method that check if this cell is the end position cell.
     * We have to check if that move is the correct last move of the path.
     * @param nextMove
     * @return
     */
    boolean checkIfIsTargetPosition(Cell nextMove) {
        if (nextMove.x == knightTarget[0] && nextMove.y == knightTarget[1]) {
            LinkedList<Cell> addPath = new LinkedList<>();
            addPath.addAll(path);
            paths.add(addPath);
            return true;
        }
        return false;
    }
}
