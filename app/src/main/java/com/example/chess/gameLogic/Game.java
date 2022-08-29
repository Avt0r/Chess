package com.example.chess.gameLogic;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.chess.activities.GameActivity;
import com.example.chess.dialogs.EndGameDialog;
import com.example.chess.gameLogic.Player.AI;
import com.example.chess.gameLogic.Player.Human;
import com.example.chess.gameLogic.Player.Player;
import com.example.chess.gameLogic.Player.Types;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static final boolean white = true;
    public static final boolean black = false;
    private final Player first;
    private final Player second;
    public final Board board = new Board();
    private boolean step = true;
    private final GameActivity activity;
    private final List<Step> steps = new ArrayList<>();

    public Game(GameActivity activity, Types first, Types second, int... depth) {
        this.activity = activity;
        switch (first) {
            case Human:
                this.first = new Human(activity, this, true);
                break;
            case AI:
                this.first = new AI(this, depth[0], true);
                break;
            default:
                throw new RuntimeException();
        }
        switch (second) {
            case Human:
                this.second = new Human(activity, this, false);
                break;
            case AI:
                this.second = new AI(this, first == Types.Human ? depth[0] : depth[1], false);
                break;
            default:
                throw new RuntimeException();
        }
        taskManager.execute();
    }

    public void changePiece(com.example.chess.gameLogic.Pieces.Types type){
        board.changePiece(type);
        Squares.updateImages();
    }

    public List<Step> getStepsList() {
        return steps;
    }

    public void makeStep(String path, boolean color) {
        if (board.move(path, color)) {
            if (board.canChange(color)) {
                if (step) {
                    first.chooseType();
                } else {
                    second.chooseType();
                }
            }
            changeStep();
            Squares.updateImages();
            if (board.isMate(!color)) {
                EndGameDialog dialog = new EndGameDialog(activity);
                dialog.show();
            }
        }
    }

    public boolean whoseMove() {
        return step;
    }

    public void changeStep() {
        step = !step;
    }

    AsyncTask<Void, Void, Void> taskManager = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                if (whoseMove()) {
                    first.paveWay();
                } else {
                    second.paveWay();
                }
                publishProgress();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (whoseMove()) {
                first.sendStep();
            } else {
                second.sendStep();
            }
        }
    };
}
