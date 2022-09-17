package com.example.chess.gameLogic;

public class Path {
    private Squares from;
    private Squares to;

    public Path(String path) {
        String[] paths = path.split("-");
        from = Squares.getSquare(paths[0]);
        to = Squares.getSquare(paths[1]);
    }

    public Path(Squares from, Squares to) {
        this.from = from;
        this.to = to;
    }

    public Path(Path path){
        this.from = path.from;
        this.to = path.to;
    }

    public void set(Path path){
        this.from = path.from;
        this.to = path.to;
    }

    public void setToEmpty(){
        from = null;
        to = null;
    }

    public void setFrom(Squares from) {
        this.from = from;
    }

    public void setTo(Squares to) {
        this.to = to;
    }

    public Squares getFrom() {
        return from;
    }

    public Squares getTo() {
        return to;
    }

    public static Path getPath(String path) {
        return new Path(Squares.getSquare(path.split("-")[0]), Squares.getSquare(path.split("-")[1]));
    }

    public boolean isEmptyFrom(){
        return from == null;
    }

    public boolean isEmptyTo(){
        return to == null;
    }

    public boolean isEmpty() {
        return from == null || to == null;
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
