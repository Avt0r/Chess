package com.example.chess.gameLogic.Player;

import androidx.annotation.NonNull;

import com.example.chess.gameLogic.Board;
import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Path;
import com.example.chess.gameLogic.Pieces.Types;

import java.util.ArrayList;
import java.util.List;

public class AI extends Player {

    private Board board;
    private final int depth;

    public AI(int depth) {
        super();
        this.depth = depth;
    }

    public AI(Game game, int depth, boolean color) {
        super(game, color);
        this.depth = depth;
        this.board = game.board;
    }

    public boolean hasPath() {
        return path != null;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void chooseType() {
        changeType(Types.QUEEN);
    }

    @Override
    public void paveWay() {
        assert game != null;
        if (game.whoseMove() != color) {
            return;
        }
        Path move = null;
        List<Path> paths = board.generatePaths(color);
        Board copy = new Board(board);
        int alfa = -6000;
        int beta = 6000;
        if (color) {
            int best = -5000;
            for (Path path : paths) {
                copy.fastMove(path);
                int x = minimax(depth - 1, copy, false, alfa, beta);
                if (best < x) {
                    move = path;
                    best = x;
                }
                copy = new Board(board);
            }
        } else {
            int best = 5000;
            for (Path path : paths) {
                copy.fastMove(path);
                int x = minimax(depth - 1, copy, true, alfa, beta);
                if (best > x) {
                    move = path;
                    best = x;
                }
                copy = new Board(board);
            }
        }
        assert move != null;
        path = move;
    }

    private int minimax(int depth, Board board, boolean color, int alfa, int beta) {
        if (depth == 0) {
            return board.count();
        }
        List<Path> paths = board.generatePaths(color);
        Board copy = new Board(board);
        int value;
        if (color) {
            value = -5000;
            for (Path path : paths) {
                copy.fastMove(path);
                value = Math.max(value, minimax(depth - 1, copy, false, alfa, beta));
                copy = new Board(board);
                alfa = Math.max(alfa, value);
                if (value >= beta) {
                    return value;
                }
            }
        } else {
            value = 5000;
            for (Path path : paths) {
                copy.fastMove(path);
                value = Math.min(value, minimax(depth - 1, copy, true, alfa, beta));
                copy = new Board(board);
                beta = Math.min(beta, value);
                if (value <= alfa) {
                    return value;
                }
            }
        }
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return "AI:" + " level = " + depth + ", color = " + (color ? "white" : "black");
    }
}
