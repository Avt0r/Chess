package com.example.chess.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.chess.R;
import com.example.chess.gameLogic.Board;
import com.example.chess.gameLogic.EventTypes;
import com.example.chess.gameLogic.Step;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Step> {

    private final List<Step> steps;

    public HistoryListAdapter(Context context, int res, List<Step> steps) {
        super(context, res, steps);
        this.steps = steps;
        steps.add(new Step(new Board(), "Game start!", EventTypes.START));
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Step step = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.history_list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.event_message))
                .setText(step.getMessage());
        {
            ImageView imageView1;
            ImageView imageView2;
            TextView textView2_square;
            ImageView event;
            switch (step.getType()) {
                case START:
                    imageView1 = (convertView.findViewById(R.id.event_object));
                    imageView1.setBackground(getContext().getDrawable(R.drawable.event_start));
                    break;
                case MOVING:
                    imageView1 = (convertView.findViewById(R.id.event_object1));
                    textView2_square = (convertView.findViewById(R.id.event_object2_square));
                    event = (convertView.findViewById(R.id.event));
                    imageView1.setBackground(getContext().getDrawable(step.getPieceFrom().getImage()));
                    textView2_square.setText(step.getPath().getTo().toString());
                    event.setBackground(getContext().getDrawable(R.drawable.event_move));
                    break;
                case CASTLING:
                    break;
                case CHANGING:
                    break;
                case ATTACKING:
                    imageView1 = (convertView.findViewById(R.id.event_object1));
                    imageView2 = (convertView.findViewById(R.id.event_object2));
                    event = (convertView.findViewById(R.id.event));
                    imageView1.setBackground(getContext().getDrawable(step.getPieceFrom().getImage()));
                    imageView2.setBackground(getContext().getDrawable(step.getPieceTo().getImage()));
                    event.setBackground(getContext().getDrawable(R.drawable.event_attack));
                    break;
            }
        }
        return convertView;
    }
}
