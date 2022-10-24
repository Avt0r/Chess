package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class King extends Piece {

    private static final int value = 100;

    private boolean firstStep = true;

    public King(boolean color) {
        super(Types.KING, color);
    }

    public King(King k) {
        super(k);
        this.firstStep = k.firstStep;
    }

    public boolean isFirstStep() {
        return firstStep;
    }

    @Override
    public boolean canMove(Squares from, Squares to) {
        int xto = to.getColumn();
        int xfrom = from.getColumn();
        int yto = to.getLine();
        int yfrom = from.getLine();
        return Math.abs(xfrom - xto) <= 1 && Math.abs(yfrom - yto) <= 1;
    }

    public boolean canCastling(Squares square) {
        if (firstStep)
            if (color) {
                return square == Squares.G1 || square == Squares.B1;
            } else {
                return square == Squares.G8 || square == Squares.B8;
            }
        return false;
    }

    @Override
    public boolean canAttack(Squares from, Squares to) {
        return canMove(from, to);
    }

    public void setFirstStep(boolean firstStep) {
        this.firstStep = firstStep;
    }

    @Override
    public int getValue() {
        return color ? value : - value;
    }

    @Override
    public String toString() {
        return color ? "K" : "k";
    }

    @Override
    public int getImage() {
        return color ? R.drawable.piece_king_white : R.drawable.piece_king_black;
    }
}
