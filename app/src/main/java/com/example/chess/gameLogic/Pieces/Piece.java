package com.example.chess.gameLogic.Pieces;


import android.view.View;

import com.example.chess.gameLogic.Squares;

public abstract class Piece {
    public final int value;
    public final boolean color;
    private Squares square;
    public final Types type;

    public Piece(Types type, Squares square, boolean color, int value) {
        this.type = type;
        this.square = square;
        this.color = color;
        this.value = value;
    }

    public Piece(Piece p) {
        this.type = p.type;
        this.square = p.square;
        this.color = p.color;
        this.value = p.value;
    }

    public abstract boolean canMove(Squares square);

    public abstract boolean canAttack(Squares square);

    public boolean canAttack(Piece piece) {
        return canAttack(piece.square);
    }

    public Squares getSquare() {
        return square;
    }

    public void setSquare(Squares square) {
        this.square = square;
    }

    public abstract int getImage();
}
