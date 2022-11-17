package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Knight extends Piece {

    public static final Types type = Types.KNIGHT;
    private static final int value = 3;

    public Knight(Squares square, boolean color) {
        super(square, color);
    }

    public Knight(Knight k) {
        super(k);
    }

    @Override
    public boolean canMove(Squares to) {
        int xto = to.getColumn();
        int xfrom = square.getColumn();
        int yto = to.getLine();
        int yfrom = square.getLine();
        return (Math.abs(xfrom - xto) == 2 && Math.abs(yfrom - yto) == 1) ||
                (Math.abs(xfrom - xto) == 1 && Math.abs(yfrom - yto) == 2);
    }

    @Override
    public boolean canAttack(Squares to) {
        return canMove(to);
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
    public Types getType() {
        return type;
    }

    @Override
    public int getImage() {
        return color?R.drawable.piece_knight_white:R.drawable.piece_knight_black;
    }
}
