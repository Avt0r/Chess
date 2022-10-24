package com.example.chess.gameLogic.Pieces;


import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Bishop extends Piece {

    private static final int value = 3;

    public Bishop(boolean color) {
        super(Types.BISHOP, color);
    }

    public Bishop(Bishop b) {
        super(b);
    }

    @Override
    public boolean canMove(Squares from, Squares to) {
        byte xto = to.getColumn();
        byte xfrom = from.getColumn();
        byte yto = to.getLine();
        byte yfrom = from.getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom);
    }

    @Override
    public boolean canAttack(Squares from, Squares to) {
        return canMove(from,to);
    }

    @Override
    public int getValue() {
        return color ? value : - value;
    }

    @Override
    public String toString() {
        return color ? "B" : "b";
    }


    @Override
    public int getImage() {
        return color ? R.drawable.piece_bishop_white : R.drawable.piece_bishop_black;
    }
}
