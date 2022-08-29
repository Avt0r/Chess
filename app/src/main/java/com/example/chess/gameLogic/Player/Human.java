package com.example.chess.gameLogic.Player;

import static com.example.chess.gameLogic.Squares.clicked;

import com.example.chess.activities.GameActivity;
import com.example.chess.dialogs.ChangePieceDialog;
import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Pieces.Types;
import com.example.chess.gameLogic.Squares;

import java.util.Scanner;

public class Human extends Player {
    private Squares start = null;
    private Squares finish = null;
    private GameActivity activity;

    public Human() {
        super();
    }

    public Human(GameActivity activity, Game game, boolean color) {
        super(game, color);
        this.activity = activity;
    }

    @Override
    public boolean hasPath() {
        return path != null;
    }

    @Override
    public void chooseType() {
        ChangePieceDialog dialog = new ChangePieceDialog(activity,this);
        dialog.show();
    }

    public void paveWay() {
        while (true) {
            if (!isMyStep()) {
                return;
            }
            if (clicked != null) {
                assert game != null;
                if (start != null && clicked != start) {
                    if(game.board.getPiece(clicked) != null) {
                        if (color == game.board.getPiece(clicked).color) {
                            start = clicked;
                        } else
                            finish = clicked;
                    } else
                        finish = clicked;
                    clicked = null;
                } else {
                    if (game.board.getPiece(clicked) != null) {
                        start = clicked;
                        clicked = null;
                    }
                }
                if (finish != null) {
                    break;
                }
            }
        }
        makePath();
    }

    public void makePath() {
        path = start + "-" + finish;
        start = null;
        finish = null;
    }
}
