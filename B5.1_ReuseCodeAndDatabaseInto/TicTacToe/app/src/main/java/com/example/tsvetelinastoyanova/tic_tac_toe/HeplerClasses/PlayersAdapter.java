package com.example.tsvetelinastoyanova.tic_tac_toe.HeplerClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Database.Player;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.MyViewHolder> {

    private List<Player> playersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, wins, loses;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            wins = view.findViewById(R.id.wins);
            loses = view.findViewById(R.id.loses);
        }
    }

    public PlayersAdapter(List<Player> playersList) {
        this.playersList = playersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.players_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Player player = playersList.get(position);
        holder.name.setText(player.getName());
        holder.wins.setText("Wins: " + player.getWins());
        holder.loses.setText("Loses: " + player.getLoses());
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }
}

