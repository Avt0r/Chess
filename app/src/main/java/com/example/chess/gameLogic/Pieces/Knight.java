package com.example.chess.gameLogic.Pieces;

import com.example.chess.gameLogic.Squares;

public class Knight extends Piece {
    public Knight(Squares square, boolean color) {
        super(Types.KNIGHT, square, color, color ? 3 : -3);
    }

    public Knight(Knight k) {
        super(k);
    }

    @Override
    public boolean canMove(Squares square) {
        int xto = square.getColumn();
        int xfrom = getSquare().getColumn();
        int yto = square.getLine();
        int yfrom = getSquare().getLine();
        return (Math.abs(xfrom - xto) == 2 && Math.abs(yfrom - yto) == 1) ||
                (Math.abs(xfrom - xto) == 1 && Math.abs(yfrom - yto) == 2);
    }

    @Override
    public boolean canAttack(Squares square) {
        return canMove(square);
    }

    @Override
    public String toString() {
        return color ? "N" : "n";
    }
}
