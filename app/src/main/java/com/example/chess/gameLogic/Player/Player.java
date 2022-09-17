package com.example.chess.gameLogic.Player;

import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Path;
import com.example.chess.gameLogic.Pieces.Types;

public abstract class Player {
    protected final Game game;
    protected final boolean color;
    protected final Path path = new Path(null, null);

    public Player() {
        game = null;
        color = false;
    }

    public Player(Game game, boolean color) {
        this.game = game;
        this.color = color;
    }

    public void changeType(Types type) {
        assert game != null;
        game.changePiece(type);
    }

    public abstract void chooseType();

    public abstract void paveWay();

    public boolean hasPath() {
        return !path.isEmpty();
    }

    protected boolean isMyStep() {
        assert game != null;
        return game.whoseMove() == color;
    }

    public void sendStep() {
        if (!hasPath()) {
            return;
        }
        assert game != null;
        game.makeStep(new Path(path), color);
        path.setToEmpty();
    }
}
