package com.example.tsvetelinastoyanova.registrationloginapp.visualization;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable {

    private List<User> usersList;
    private Context context;
    private List<User> usersListFiltered;
    private UsersAdapterListener listener;

    private Filter filter = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, email, firstName, lastName, age;

        MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            email = view.findViewById(R.id.email);
            firstName = view.findViewById(R.id.firstName);
            lastName = view.findViewById(R.id.lastName);
            age = view.findViewById(R.id.age);

            view.setOnClickListener(view1 -> listener.onUserSelected(usersListFiltered.get(getAdapterPosition())));
        }
    }

    public UsersAdapter(List<User> usersList, Context context, UsersAdapterListener listener) {
        this.usersList = usersList;
        this.context = context;
        this.usersListFiltered = usersList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final User user = usersListFiltered.get(position);
        holder.username.setText(user.getUsername());

        final String emailText = context.getResources().getString(R.string.email, user.getEmail());
        final String firstNameText = context.getResources().getString(R.string.first_name, user.getFirstName());
        final String lastNameText = context.getResources().getString(R.string.last_name, user.getLastName());
        final String age = context.getResources().getString(R.string.age, String.valueOf(user.getAge()));

        holder.email.setText(emailText);
        holder.firstName.setText(firstNameText);
        holder.lastName.setText(lastNameText);
        holder.age.setText(age);
    }

    @Override
    public int getItemCount() {
        return usersListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = createFilter();
        }

        return filter;
    }

    @NonNull
    private Filter createFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                String queryLowerCase = query.toString().toLowerCase();
                if (queryLowerCase.isEmpty()) {
                    usersListFiltered = usersList;
                } else {
                    usersListFiltered = findFilteredUsers(queryLowerCase);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usersListFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private List<User> findFilteredUsers(String queryLowerCase) {
        List<User> filteredList = new ArrayList<>();
        for (User user : usersList) {
            if (userContainsQuery(queryLowerCase, user)) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    private boolean userContainsQuery(String queryLowerCase, User row) {
        return row.getUsername().toLowerCase().contains(queryLowerCase) ||
                row.getEmail().toLowerCase().contains(queryLowerCase) ||
                row.getFirstName().toLowerCase().contains(queryLowerCase) ||
                row.getLastName().toLowerCase().contains(queryLowerCase) ||
                String.valueOf(row.getAge()).contains(queryLowerCase);
    }
}

