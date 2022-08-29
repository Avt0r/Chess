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

import com.example.chess.R;
import com.example.chess.gameLogic.Events;

import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<Events> {

    private final List<Events> events;

    public HistoryListAdapter(Context context, int res, List<Events> events){
        super(context,res,events);
        this.events = events;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Events event = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.history_list_item, null);
        }
        ((TextView)convertView.findViewById(R.id.event_moving_path))
                .setText(event.getText());

        return convertView;
    }
}
