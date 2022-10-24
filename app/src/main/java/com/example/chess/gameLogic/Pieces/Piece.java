package com.example.chess.gameLogic.Pieces;

import com.example.chess.gameLogic.Squares;

public abstract class Piece {
    public final boolean color;
    public final Types type;

    public Piece(Types type, boolean color) {
        this.type = type;
        this.color = color;
    }

    public Piece(Piece p) {
        this.type = p.type;
        this.color = p.color;
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


    public abstract boolean canMove(Squares from, Squares to);

    public abstract boolean canAttack(Squares from, Squares to);

    public abstract int getValue();

    public abstract int getImage();
}
