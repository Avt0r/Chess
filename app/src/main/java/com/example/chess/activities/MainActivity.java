package com.example.chess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.chess.dialogs.ChooseGameModeDialog;
import com.example.chess.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChooseGameModeDialog dialog = new ChooseGameModeDialog(MainActivity.this);

        Button button = findViewById(R.id.startGame);
        button.setOnClickListener(v->{
            try {
                dialog.show();
            }catch (NumberFormatException ignored){}
        });
    }
}