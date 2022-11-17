package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Queen extends Piece {

    public static final Types type = Types.QUEEN;
    private static final int value = 12;

    public Queen(Squares square, boolean color) {
        super(square, color);
    }

    public Queen(Queen q) {
        super(q);
    }

    @Override
    public boolean canMove(Squares to) {
        int xto = to.getColumn();
        int xfrom = square.getColumn();
        int yto = to.getLine();
        int yfrom = square.getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom) ||
                (xto == xfrom || yto == yfrom);
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
        return color ? "Q" : "q";
    }

    @Override
    public Types getType() {
        return type;
    }

    @Override
    public int getImage() {
        return color? R.drawable.piece_queen_white:R.drawable.piece_queen_black;
    }
}
