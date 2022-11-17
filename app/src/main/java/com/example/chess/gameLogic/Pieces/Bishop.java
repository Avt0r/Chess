package com.example.chess.gameLogic.Pieces;


import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Bishop extends Piece {

    public static final Types type = Types.BISHOP;
    private static final int value = 3;

    public Bishop(Squares square, boolean color) {
        super(square, color);
    }

    public Bishop(Bishop b) {
        super(b);
    }

    @Override
    public boolean canMove(Squares to) {
        byte xto = to.getColumn();
        byte xfrom = square.getColumn();
        byte yto = to.getLine();
        byte yfrom = square.getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom);
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
        return color ? "B" : "b";
    }

    @Override
    public Types getType() {
        return type;
    }

    @Override
    public int getImage() {
        return color ? R.drawable.piece_bishop_white : R.drawable.piece_bishop_black;
    }
}
