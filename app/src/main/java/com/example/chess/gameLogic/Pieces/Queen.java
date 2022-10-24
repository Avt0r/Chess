package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Queen extends Piece {

    private static final int value = 12;

    public Queen(boolean color) {
        super(Types.QUEEN, color);
    }

    public Queen(Queen q) {
        super(q);
    }

    @Override
    public boolean canMove(Squares from, Squares to) {
        int xto = to.getColumn();
        int xfrom = from.getColumn();
        int yto = to.getLine();
        int yfrom = from.getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom) ||
                (xto == xfrom || yto == yfrom);
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
        return color ? "Q" : "q";
    }

    @Override
    public int getImage() {
        return color? R.drawable.piece_queen_white:R.drawable.piece_queen_black;
    }
}
