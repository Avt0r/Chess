package com.example.chess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.chess.R;
import com.example.chess.adapters.HistoryListAdapter;
import com.example.chess.gameLogic.EventTypes;
import com.example.chess.gameLogic.Game;
import com.example.chess.gameLogic.Player.Types;
import com.example.chess.gameLogic.Squares;
import com.example.chess.gameLogic.Step;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public Game game;
    HistoryListAdapter adapter;
    ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        Bundle bundle = getIntent().getExtras();

        Types first = (Types) bundle.get("Player1");
        Types second = (Types) bundle.get("Player2");

        if(first == Types.Human && second == Types.AI){
            int depth = bundle.getInt("depth");
            game = new Game(this, first,second,depth);
        }else if(first == Types.AI && second == Types.AI){
            int depth1 = bundle.getInt("depth1");
            int depth2 = bundle.getInt("depth2");
            game = new Game(this, first,second,depth1,depth2);
        }else{
            game = new Game(this, first,second);
        }

        historyList = findViewById(R.id.history_list);
        adapter = new HistoryListAdapter(this, R.layout.history_list_item, game.getStepsList());
        historyList.setAdapter(adapter);

        Squares.setActivity(this);
        Squares.updateImages();
        Squares.listenClick();
    }
}