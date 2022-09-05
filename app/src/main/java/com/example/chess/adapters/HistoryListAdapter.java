package com.example.chess.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.chess.R;
import com.example.chess.gameLogic.Board;
import com.example.chess.gameLogic.EventTypes;
import com.example.chess.gameLogic.Path;
import com.example.chess.gameLogic.Step;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Step> {

    private final List<Step> steps;

    public HistoryListAdapter(Context context, int res, List<Step> steps) {
        super(context, res, steps);
        this.steps = steps;
        steps.add(new Step(new Board(), "Game start!", EventTypes.START));
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Step step = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.history_list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.event_moving_path))
                .setText(step.getMessage());
        switch (step.getType()){
            case START:{
                ((CardView)convertView.findViewById(R.id.event_object)).setBackgroundResource(R.drawable.img_game_start);
            }
            case MOVING:{}
            case CASTLING:{}
            case CHANGING:{}
            case ATTACKING:{}
        }
        return convertView;
    }
}
