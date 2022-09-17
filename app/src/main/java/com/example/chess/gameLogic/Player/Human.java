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
        return !path.isEmpty();
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
                if (!path.isEmptyFrom()) {
                    if (game.board.isTherePiece(clicked)) {
                        if (color == game.board.getPiece(clicked).color) {
                            path.setFrom(clicked);
                            clicked = null;
                        } else if (game.board.canAttack(game.board.getPiece(path.getFrom()), clicked)) {
                            path.setTo(clicked);
                            clicked = null;
                        }
                    } else if (game.board.canMove(game.board.getPiece(path.getFrom()), clicked)) {
                        path.setTo(clicked);
                        clicked = null;
                    }
                } else {
                    if (game.board.isTherePiece(clicked)) {
                        path.setFrom(clicked);
                        clicked = null;
                    }
                }
                if (!path.isEmpty()) {
                    break;
                }
            }
        }
    }
}
