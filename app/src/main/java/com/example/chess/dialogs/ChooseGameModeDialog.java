package com.example.chess.dialogs;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.chess.R;
import com.example.chess.activities.GameActivity;
import com.example.chess.gameLogic.Player.Types;

public class ChooseGameModeDialog extends android.app.Dialog {

    public ChooseGameModeDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.game_mode);
        Button HumanVsAIButton = findViewById(R.id.Button_HumanVsAI);
        Button HumanVsHumanButton = findViewById(R.id.Button_HumanVsHuman);
        Button AIVsAIButton = findViewById(R.id.Button_AIVsAI);
        EditText depth1 = findViewById(R.id.depth1);
        EditText depth2 = findViewById(R.id.depth2);
        EditText depth3 = findViewById(R.id.depth3);
        Intent intent = new Intent(getContext(), GameActivity.class);
        HumanVsAIButton.setOnClickListener(v->{
            intent.putExtra("Player1", Types.Human);
            intent.putExtra("Player2", Types.AI);
            intent.putExtra("depth",Integer.parseInt(depth1.getText().toString()));
            getContext().startActivity(intent);
        });
        HumanVsHumanButton.setOnClickListener(v->{
            intent.putExtra("Player1",Types.Human);
            intent.putExtra("Player2",Types.Human);
            getContext().startActivity(intent);
        });
        AIVsAIButton.setOnClickListener(v->{
            intent.putExtra("Player1",Types.AI);
            intent.putExtra("Player2",Types.AI);
            intent.putExtra("depth1",Integer.parseInt(depth2.getText().toString()));
            intent.putExtra("depth2",Integer.parseInt(depth3.getText().toString()));
            getContext().startActivity(intent);
        });
    }

}
