package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Queen extends Piece {
    public Queen(Squares square, boolean color) {
        super(Types.QUEEN, square, color, color ? 12 : -12);
    }

    public Queen(Queen q) {
        super(q);
    }

    @Override
    public boolean canMove(Squares square) {
        int xto = square.getColumn();
        int xfrom = getSquare().getColumn();
        int yto = square.getLine();
        int yfrom = getSquare().getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom) ||
                (xto == xfrom || yto == yfrom);
    }

    @Override
    public boolean canAttack(Squares square) {
        return canMove(square);
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
