package com.example.chess.gameLogic.Pieces;

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

    public static Piece makeCopyPiece(Piece i) {
        switch (i.type) {
            case KING:
                return new King((King) i);
            case PAWN:
                return new Pawn((Pawn) i);
            case ROOK:
                return new Rook((Rook) i);
            case QUEEN:
                return new Queen((Queen) i);
            case BISHOP:
                return new Bishop((Bishop) i);
            case KNIGHT:
                return new Knight((Knight) i);
            default:
                throw new RuntimeException();
        }
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
