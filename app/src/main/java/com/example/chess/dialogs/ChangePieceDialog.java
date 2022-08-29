package com.example.chess.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;

import com.example.chess.R;
import com.example.chess.gameLogic.Pieces.Types;

public class ChangePieceDialog extends Dialog {
    private volatile Types changeType = null;

    public ChangePieceDialog(Context context) {
        super(context);
        Mutex mutex = new Mutex();
        ImageButton queenButton = findViewById(R.id.change_piece_queen);
        ImageButton knightButton = findViewById(R.id.change_piece_knight);
        ImageButton rookButton = findViewById(R.id.change_piece_rook);
        ImageButton bishopButton = findViewById(R.id.change_piece_bishop);
        queenButton.setOnClickListener(v -> {
            changeType = Types.QUEEN;
            dismiss();
        });
        knightButton.setOnClickListener(v -> {
            changeType = Types.KNIGHT;
            dismiss();
        });
        rookButton.setOnClickListener(v -> {
            changeType = Types.ROOK;
            dismiss();
        });
        bishopButton.setOnClickListener(v -> {
            changeType = Types.BISHOP;
            dismiss();
        });
        setOnDismissListener(v->{
            mutex.unlock();
        });
    }



    @Override
    public void show() {
        super.show();
    }

    public Types getSelectedType() {
        return changeType;
    }

    public static class Mutex{
        public synchronized void lock(){
            try{
                this.wait();
            }catch (InterruptedException i){lock();}
        }
        public synchronized void unlock(){
            this.notify();
        }
    }
}
