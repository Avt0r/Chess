package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Rook extends Piece {

    public static final Types type = Types.ROOK;
    private static final int value = 5;
    private boolean firstStep = true;

    public Rook(Squares square, boolean color) {
        super(square, color);
    }

    public Rook(Rook r) {
        super(r);
        this.firstStep = r.firstStep;
    }

    public boolean isFirstStep() {
        return firstStep;
    }

    @Override
    public boolean canMove(Squares to) {
        int xto = to.getColumn();
        int xfrom = square.getColumn();
        int yto = to.getLine();
        int yfrom = square.getLine();
        return (xto == xfrom || yto == yfrom);
    }

    public boolean canCastling(){
        return firstStep;
    }

    @Override
    public boolean canAttack(Squares to) {
        return canMove(to);
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
        return color ? "R" : "r";
    }

    @Override
    public Types getType() {
        return type;
    }

    @Override
    public int getImage() {
        return color? R.drawable.piece_rook_white:R.drawable.piece_rook_black;
    }
}
