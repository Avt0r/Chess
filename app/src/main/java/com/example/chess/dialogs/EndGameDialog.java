package com.example.chess.dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.chess.R;

public class EndGameDialog extends Dialog {

    public EndGameDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.game_finish);
    }
}
