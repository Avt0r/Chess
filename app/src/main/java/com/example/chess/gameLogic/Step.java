package com.example.chess.gameLogic;

import com.example.chess.gameLogic.Pieces.Piece;

public class Step {
    private final Board.PiecesListElement preCondition;
    private final Board.PiecesListElement condition;
    private Path path;
    private final String message;
    private final EventTypes type;

    public Step(Board board, Path path, EventTypes type){
        this.condition = new Board.PiecesListElement(board.getLastCondition());
        this.preCondition = new Board.PiecesListElement(board.getPreLastCondition());
        this.path = path;
        this.message = path.toString();
        this.type = type;
    }

    public Step(Board board, String message, EventTypes type){
        this.condition = new Board.PiecesListElement(board.getLastCondition());
        this.preCondition = new Board.PiecesListElement(board.getPreLastCondition());
        this.message = message;
        try {
            Path.getPath(message);
            this.path = Path.getPath(message);
        }catch (ArrayIndexOutOfBoundsException ignored){
            this.path = null;
        }
        this.type = type;
    }

    public String getMessage(){
        return message;
    }

    public Path getPath(){
        return path;
    }

    public Board.PiecesListElement getCondition() {
        return condition;
    }

    public EventTypes getType() {
        return type;
    }

    public Piece getPieceFrom(){
        return preCondition.getPiece(path.getFrom());
    }
}
