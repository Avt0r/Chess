package com.example.chess.gameLogic.Pieces;

import com.example.chess.gameLogic.Squares;

public class Pawn extends Piece {
    public Pawn(Squares square, boolean color) {
        super(Types.PAWN, square, color, color ? 1 : -1);
    }

    public Pawn(Pawn p) {
        super(p);
    }

    @Override
    public boolean canMove(Squares square) {
        int xto = square.getColumn();
        int xfrom = getSquare().getColumn();
        int yto = square.getLine();
        int yfrom = getSquare().getLine();
        if (xto != xfrom) {
            return false;
        }
        if (color) {
            if (yfrom >= yto) {
                return false;
            }
            if (getSquare().getLine() == 1) {
                return yto - yfrom <= 2;
            } else {
                return yto - yfrom == 1;
            }
        } else {
            if (yfrom <= yto) {
                return false;
            }
            if (getSquare().getLine() == 6) {
                return yfrom - yto <= 2;
            } else {
                return yfrom - yto == 1;
            }
        }
    }

    public boolean canChange(){
        if(color){
            return Squares.top(getSquare().number);
        }else{
            return Squares.bottom(getSquare().number);
        }
    }

    @Override
    public boolean canAttack(Squares square) {
        int xto = square.getColumn();
        int xfrom = getSquare().getColumn();
        int yto = square.getLine();
        int yfrom = getSquare().getLine();
        if (color) {
            return Math.abs(xfrom - xto) == 1 && yto - yfrom == 1;
        } else {
            return Math.abs(xfrom - xto) == 1 && yfrom - yto == 1;
        }
    }

    @Override
    public String toString() {
        return color ? "P" : "p";
    }
}
