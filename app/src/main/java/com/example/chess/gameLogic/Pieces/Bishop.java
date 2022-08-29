package com.example.chess.gameLogic.Pieces;


import com.example.chess.gameLogic.Squares;

public class Bishop extends Piece{
    public Bishop(Squares square, boolean color){
        super(Types.BISHOP, square,color,color?3:-3);
    }

    public Bishop(Bishop b){
        super(b);
    }
    @Override
    public boolean canMove(Squares square) {
        byte xto = square.getColumn();
        byte xfrom = getSquare().getColumn();
        byte yto = square.getLine();
        byte yfrom = getSquare().getLine();
        return Math.abs(xto - xfrom) == Math.abs(yto - yfrom);
    }

    @Override
    public boolean canAttack(Squares square) {
        return canMove(square);
    }

    @Override
    public String toString() {
        return color?"B":"b";
    }
}
