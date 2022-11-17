package com.example.chess.gameLogic.Pieces;

import com.example.chess.gameLogic.Squares;

public abstract class Piece {

    public final boolean color;
    protected Squares square;

    public Piece(Squares square, boolean color) {
        this.square = square;
        this.color = color;
    }

    public Piece(Piece p) {
        this.square = p.square;
        this.color = p.color;
    }

    public static Piece makeCopyPiece(Piece i) {
        switch (i.getType()) {
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

    public void move(Squares square) {
        this.square = square;
    }

    public abstract Types getType();

    public abstract int getValue();

    public abstract int getImage();
}
