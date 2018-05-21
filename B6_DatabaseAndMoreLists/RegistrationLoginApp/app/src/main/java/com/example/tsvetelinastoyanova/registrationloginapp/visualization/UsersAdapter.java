package com.example.tsvetelinastoyanova.registrationloginapp.visualization;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.activities.UsersListActivity;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable {


    // https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/

    private List<User> usersList;
    Context context;
    private List<User> usersListFiltered; // new
    private UsersAdapterListener listener; // ?? new

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, email, firstName, lastName, age;

        public MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            email = view.findViewById(R.id.email);
            firstName = view.findViewById(R.id.firstName);
            lastName = view.findViewById(R.id.lastName);
            age = view.findViewById(R.id.age);
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
       // User user = usersList.get(position);
        holder.username.setText(user.getUsername());
        final String emailText = context.getResources().getString(R.string.email);
        holder.email.setText(emailText + user.getEmail());
        final String firstNameText = context.getResources().getString(R.string.first_name);
        holder.firstName.setText(firstNameText + user.getFirstName());
        final String lastNameText = context.getResources().getString(R.string.last_name);
        holder.lastName.setText(lastNameText + user.getLastName());
        final String age = context.getResources().getString(R.string.age);
        holder.age.setText(age + user.getAge());

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    usersListFiltered = usersList;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : usersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getUsername().toLowerCase().contains(charString) || row.getEmail().toLowerCase().contains(charSequence) ||
                        row.getFirstName().toLowerCase().contains(charString) || row.getLastName().toLowerCase().contains(charSequence) ||
                        String.valueOf(row.getAge()).contains(charSequence)){
                            filteredList.add(row);
                        }
                    }
                    usersListFiltered = filteredList;
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

}

