package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Knight extends Piece {

    private static final int value = 3;

    public Knight(boolean color) {
        super(Types.KNIGHT, color);
    }

    public Knight(Knight k) {
        super(k);
    }

    @Override
    public boolean canMove(Squares from, Squares to) {
        int xto = to.getColumn();
        int xfrom = from.getColumn();
        int yto = to.getLine();
        int yfrom = from.getLine();
        return (Math.abs(xfrom - xto) == 2 && Math.abs(yfrom - yto) == 1) ||
                (Math.abs(xfrom - xto) == 1 && Math.abs(yfrom - yto) == 2);
    }

    @Override
    public boolean canAttack(Squares from, Squares to) {
        return canMove(from, to);
    }

    @Override
    public int getValue() {
        return color ? value : - value;
    }

    @Override
    public String toString() {
        return color ? "N" : "n";
    }

    @Override
    public int getImage() {
        return color?R.drawable.piece_knight_white:R.drawable.piece_knight_black;
    }
}
