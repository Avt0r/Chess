package com.example.chess.gameLogic.Pieces;

import com.example.chess.R;
import com.example.chess.gameLogic.Squares;

public class Pawn extends Piece {

    public static final Types type = Types.PAWN;
    private static final int value = 1;

    public Pawn(Squares square, boolean color) {
        super(square, color);
    }

    public Pawn(Pawn p) {
        super(p);
    }

    @Override
    public boolean canMove(Squares to) {
        int xto = to.getColumn();
        int xfrom = square.getColumn();
        int yto = to.getLine();
        int yfrom = square.getLine();
        if (xto != xfrom) {
            return false;
        }
        if (color) {
            if (yfrom >= yto) {
                return false;
            }
            if (yto == 1) {
                return yto - yfrom <= 2;
            } else {
                return yto - yfrom == 1;
            }
        } else {
            if (yfrom <= yto) {
                return false;
            }
            if (yto == 6) {
                return yfrom - yto <= 2;
            } else {
                return yfrom - yto == 1;
            }
        }
    }

    public static boolean canChange(Squares square, boolean color){
        if(color){
            return Squares.top(square.number);
        }else{
            return Squares.bottom(square.number);
        }
    }

    @Override
    public boolean canAttack(Squares to) {
        int xto = to.getColumn();
        int xfrom = square.getColumn();
        int yto = to.getLine();
        int yfrom = square.getLine();
        if (color) {
            return Math.abs(xfrom - xto) == 1 && yto - yfrom == 1;
        } else {
            return Math.abs(xfrom - xto) == 1 && yfrom - yto == 1;
        }
    }

    @Override
    public int getValue() {
        return color ? value : - value;
    }

    @Override
    public String toString() {
        return color ? "P" : "p";
    }

    @Override
    public Types getType() {
        return type;
    }

    @Override
    public int getImage() {
        return color? R.drawable.piece_pawn_white:R.drawable.piece_pawn_black;
    }
}
