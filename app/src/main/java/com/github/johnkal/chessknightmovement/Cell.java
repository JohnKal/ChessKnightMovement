package com.github.johnkal.chessknightmovement;

/**
 * A helper class for PixelGridView.java
 */
public class Cell {
    public int x;
    public int y;
    public boolean firstMove; // store if this cell is the first move of the path.

    public Cell(int x, int y, boolean firstMove) {
        this.x = x;
        this.y = y;
        this.firstMove = firstMove;
    }
}
