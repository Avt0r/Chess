package com.example.chess.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;

import com.example.chess.R;
import com.example.chess.gameLogic.Pieces.Types;
import com.example.chess.gameLogic.Player.Human;
import com.example.chess.gameLogic.Player.Player;

public class ChangePieceDialog extends Dialog {
    private volatile Types changeType = null;

    public ChangePieceDialog(Context context, Player player) {
        super(context);
        setContentView(R.layout.change_piece_item);
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
        setOnDismissListener(v-> player.changeType(changeType));
    }
}
