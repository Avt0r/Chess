package com.example.chess.gameLogic;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.chess.activities.GameActivity;
import com.example.chess.adapters.HistoryListAdapter;
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
    private final ArrayAdapter<Step> adapter;

    public Game(GameActivity activity, Types first, Types second, ArrayAdapter<Step> adapter, int... depth) {
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
        this.adapter = adapter;
        taskManager.execute();
    }

    public void changePiece(com.example.chess.gameLogic.Pieces.Types type) {
        board.changePiece(type);
        Squares.updateImages();
    }

    public void makeStep(Path path, boolean color) {
        if (board.move(path, color)) {
            EventTypes event = board.getLastEvent();
            adapter.add(new Step(board, path, event));
            if (board.canChange(color)) {
                if (step) {
                    first.chooseType();
                } else {
                    second.chooseType();
                }
                adapter.add(new Step(board, "Changing", EventTypes.CHANGING));
            }
            Squares.updateImages();
            changeStep();
            if (board.isMate(!color)) {
                EndGameDialog dialog = new EndGameDialog(activity);
                dialog.show();
                taskManager.cancel(true);
            }
        }
    }

    public boolean whoseMove() {
        return step;
    }

    public void changeStep() {
        step = !step;
    }

    @SuppressLint("StaticFieldLeak")
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
            adapter.notifyDataSetChanged();
        }
    };
}
