package com.example.chess.gameLogic;

public class Step {
    private final Board board;
    private final Path path;
    private final EventTypes type;

    public Step(Board board, Path path, EventTypes type){
        this.board = board;
        this.path = path;
        this.type = type;
    }

    public Path getPath() {
        return path;
    }

    public Board getBoard() {
        return board;
    }

    public EventTypes getType() {
        return type;
    }
}
