package com.example.tsvetelinastoyanova.b3_listsintro;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Planet> planetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        prepareData();

        // To open a new window with description:
        RecyclerViewClickListener listenerNewActivity = (view, position) -> startPlanetDetailActivity(position);

        // To open a web browser
        RecyclerViewClickListener listenerBrowser = (view, position) -> openWebPage(planetList.get(position).getUrl());

        // here use listenerNewActivity or listenerBrowser:
        setupList(recyclerView, listenerNewActivity);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void setupList(RecyclerView recyclerView, RecyclerViewClickListener listenerNewActivity) {
        MyAdapter myAdapter = new MyAdapter(planetList, listenerNewActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void startPlanetDetailActivity(int position) {
        Intent moreInformationIntent = new Intent(this, MoreInformationActivity.class);
        moreInformationIntent.putExtra(getResources().getString(R.string.planet_object), planetList.get(position));
        startActivity(moreInformationIntent);
    }

    private void prepareData() {

        String[] s = getResources().getStringArray(R.array.planets_source);

        Planet planet = new Planet("Mercury", s[0], R.drawable.mercury, getResources().getString(R.string.mercury_url));
        planetList.add(planet);

        planet = new Planet("Venus", s[1], R.drawable.venus, getResources().getString(R.string.venus_url));
        planetList.add(planet);

        planet = new Planet("Earth", s[2], R.drawable.earth, getResources().getString(R.string.earth_url));
        planetList.add(planet);

        planet = new Planet("Mars", s[3], R.drawable.mars, getResources().getString(R.string.mars_url));
        planetList.add(planet);

        planet = new Planet("Jupiter", s[4], R.drawable.jupiter, getResources().getString(R.string.jupiter_url));
        planetList.add(planet);

        planet = new Planet("Saturn", s[5], R.drawable.saturn, getResources().getString(R.string.saturn_url));
        planetList.add(planet);

        planet = new Planet("Uranus", s[6], R.drawable.uranus, getResources().getString(R.string.uranus_url));
        planetList.add(planet);

        planet = new Planet("Neptune", s[7], R.drawable.neptune, getResources().getString(R.string.neptune_url));
        planetList.add(planet);

    }

}

