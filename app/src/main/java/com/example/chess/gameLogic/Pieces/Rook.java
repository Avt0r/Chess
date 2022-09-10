package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Rook extends Piece {
    private boolean firstStep = true;

    public Rook(Squares square, boolean color) {
        super(Types.ROOK, square, color, color ? 5 : -5);
    }

    public Rook(Rook r) {
        super(r);
        this.firstStep = r.firstStep;
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
        return (xto == xfrom || yto == yfrom);
    }

    public boolean canCastling(){
        return firstStep;
    }

    @Override
    public boolean canAttack(Squares square) {
        return canMove(square);
    }

    @Override
    public String toString() {
        return color ? "R" : "r";
    }

    @Override
    public int getImage() {
        return color? R.drawable.piece_rook_white:R.drawable.piece_rook_black;
    }
}
