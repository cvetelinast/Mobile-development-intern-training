package com.example.tsvetelinastoyanova.registrationloginapp.activities;

import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;
import com.example.tsvetelinastoyanova.registrationloginapp.visualization.UsersAdapter;
import com.example.tsvetelinastoyanova.registrationloginapp.visualization.UsersAdapterListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersListActivity extends AppCompatActivity implements UsersAdapterListener {
    private List<User> usersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private SearchView searchView;
    ExecutorService notMainThread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        prepareShowingUsers();
        loadUsersFromDatabase();
    }

    private void prepareShowingUsers() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new UsersAdapter(usersList, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadUsersFromDatabase() {
        notMainThread.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users").build();
            usersList.addAll(db.userDao().getAll());
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_search || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onUserSelected(User user) {
        Toast.makeText(getApplicationContext(), "Selected: " + user.getUsername(), Toast.LENGTH_LONG).show();
    }


}
