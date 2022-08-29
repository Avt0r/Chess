package com.example.chess.gameLogic.Pieces;

import com.example.chess.gameLogic.Squares;

public class King extends Piece {
    private boolean firstStep = true;

    public King(Squares square, boolean color) {
        super(Types.KING, square, color, color ? 100 : -100);
    }

    public King(King k) {
        super(k);
        this.firstStep = k.firstStep;
    }

    @Override
    public void setSquare(Squares square) {
        super.setSquare(square);
        firstStep = false;
    }

    public boolean isFirstStep() {
        return firstStep;
    }

    @Override
    public boolean canMove(Squares square) {
        int xto = square.getColumn();
        int xfrom = getSquare().getColumn();
        int yto = square.getLine();
        int yfrom = getSquare().getLine();
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
    public boolean canAttack(Squares square) {
        return canMove(square);
    }

    @Override
    public String toString() {
        return color ? "K" : "k";
    }
}
