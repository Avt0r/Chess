package com.example.chess.gameLogic;

public class Step {
    private final Board board;
    private final Path path;
    private final String message;
    private final EventTypes type;

    public Step(Board board, Path path, EventTypes type){
        this.board = board;
        this.path = path;
        this.message = null;
        this.type = type;
    }

    public Step(Board board, String message, EventTypes type){
        this.board = board;
        this.path = null;
        this.message = message;
        this.type = type;
    }

    public String getMessage(){
        assert path != null || message != null;
        return path == null? message : path.toString();
    }

    public Board getBoard() {
        return board;
    }

    public EventTypes getType() {
        return type;
    }
}
