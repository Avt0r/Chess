package com.example.chess.gameLogic;

public class Path {
    private final Squares from;
    private final Squares to;

    public Path(String path){
        String[] paths = path.split("-");
        from = Squares.getSquare(paths[0]);
        to = Squares.getSquare(paths[1]);
    }

    public Path(Squares from, Squares to){
        this.from=from;
        this.to=to;
    }

    public Squares getFrom(){
        return from;
    }

    public Squares getTo(){
        return to;
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
