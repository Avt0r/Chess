package com.example.chess.gameLogic.Player;

import static com.example.chess.gameLogic.Squares.clicked;

import com.example.chess.activities.GameActivity;
import com.example.chess.dialogs.ChangePieceDialog;
import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Path;
import com.example.chess.gameLogic.Pieces.Types;
import com.example.chess.gameLogic.Squares;

import java.util.Scanner;

public class Human extends Player {
    private Path path = new Path(null, null);
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
        ChangePieceDialog dialog = new ChangePieceDialog(activity, this);
        dialog.show();
    }

    public void paveWay() {
        assert game != null;
        if (game.whoseMove() != color) {
            return;
        }
        while (true) {
            if (clicked != null) {
                if (path.getFrom() != null && clicked != path.getFrom()) {
                    if (game.board.getPiece(clicked) != null) {
                        if (color == game.board.getPiece(clicked).color) {
                            path.setFrom(clicked);
                        } else
                            path.setTo(clicked);
                    } else
                        path.setTo(clicked);
                    clicked = null;
                } else {
                    if (game.board.getPiece(clicked) != null) {
                        path.setFrom(clicked);
                        clicked = null;
                    }
                }
                if (path.getFrom() != null) {
                    break;
                }
            }
        }
    }
}
