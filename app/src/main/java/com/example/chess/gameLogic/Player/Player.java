package com.example.chess.gameLogic.Player;

import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Pieces.Types;

public abstract class Player {
    protected final Game game;
    protected final boolean color;
    protected String path;

    public Player(){
        game = null;
        color = false;
    }

    public Player(Game game,boolean color){
        this.game = game;
        this.color = color;
    }

    public abstract Types changeType();
    public abstract void paveWay();
    public boolean hasPath(){
        return path!=null;
    }
    protected boolean isMyStep(){
        assert game != null;
        return game.whoseMove()==color;
    }
    public void sendStep(){
        if(!hasPath()){return;}
        assert game != null;
        game.makeStep(path,color);
        path = null;
    }
}
