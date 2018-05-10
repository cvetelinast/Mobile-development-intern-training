package com.example.tsvetelinastoyanova.b4_dynamiclistsrequeststhreading;

import android.content.Intent;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

interface CrawlerEventsListener {
    void onCrawlingStarted();

    void onAllUrlsCrawled(List<String> crawledUrls);
}

public class CrawlingActivity extends AppCompatActivity implements UrlsAdapter.ItemClickListener, CrawlerEventsListener {
    private UrlsAdapter adapter;
    private Crawler crawler;
    private String url;
    private int depth;

    private List<String> adapterItems = new ArrayList<>();

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawling);

        Intent intent = getIntent();
        url = intent.getStringExtra(this.getString(R.string.url_input));
        depth = Integer.parseInt(intent.getStringExtra(this.getString(R.string.depth_input)));

        crawler = new Crawler(this);
        initializeRecyclerViewAndAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        crawler.startCrawling(url, depth);
    }

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(this.getString(R.string.is_crawling_stopped), isCrawlingStopped);
        savedInstanceState.putStringArrayList(this.getString(R.string.queue_with_urls), (ArrayList<String>) queueWithUrls); // Saving the ArrayList queueWithUrls
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isCrawlingStopped = savedInstanceState.getBoolean(this.getString(R.string.is_crawling_stopped));
        queueWithUrls = savedInstanceState.getStringArrayList(this.getString(R.string.queue_with_urls)); //Restoring queueWithUrls

    }*/

    public void stopCrawling(View view) {
        crawler.stopCrawling();
    }

    private void initializeRecyclerViewAndAdapter() {
        RecyclerView recyclerView = findViewById(R.id.urls);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initializeAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initializeAdapter() {
        adapter = new UrlsAdapter(this, adapterItems);
        adapter.setClickListener(this);
    }

    private void makeProgressBarGone() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private void makeProgressBarVisible() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCrawlingStarted() {
        runOnUiThread(() -> makeProgressBarVisible());
    }

    @Override
    public void onAllUrlsCrawled(List<String> urlsCrawled) {
        runOnUiThread(() -> {
            adapterItems.addAll(urlsCrawled);
            adapter.notifyDataSetChanged();
            makeProgressBarGone();
        });
    }


}
