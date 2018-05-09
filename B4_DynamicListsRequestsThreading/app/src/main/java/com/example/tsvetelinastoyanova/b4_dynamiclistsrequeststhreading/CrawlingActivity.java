package com.example.tsvetelinastoyanova.b4_dynamiclistsrequeststhreading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class CrawlingActivity extends AppCompatActivity implements UrlsAdapter.ItemClickListener {

    private final List<String> queueWithUrls = new LinkedList<>(); // to fix to be a queue ?
    private UrlsAdapter adapter;
    private RecyclerView recyclerView;
    boolean flag;

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawling);

        Intent intent = getIntent();
        String url = intent.getStringExtra(this.getString(R.string.url_input));
        String depth = intent.getStringExtra(this.getString(R.string.depth_input));

        initializeRecyclerViewAndAdapter();
        crawling(url, Integer.parseInt(depth));
        flag = false;
    }

    public void stopCrawling(View view) {
        Log.d("stop", "Links are " + queueWithUrls.size());
        flag = true;
        adapter.notifyDataSetChanged();

    }

    private void initializeRecyclerViewAndAdapter() {
        recyclerView = findViewById(R.id.urls);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UrlsAdapter(this, queueWithUrls);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    private void crawling(String enteredUrl, int length) {
        new Thread(() -> {
            diggingRecursive(enteredUrl, length);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    private void diggingRecursive(String enteredUrl, int length) {
        if (length == 0 || flag) {
            return;
        }
        String page = getHtmlSourceFromEnteredUrl(enteredUrl);
        if (page.equals("")) {
            return;
        }
        Vector<String> grabHTMLLinks = new HTMLLinkExtractor().grabHTMLLinks(page);
        queueWithUrls.addAll(grabHTMLLinks);
        for (int i = 0; i < grabHTMLLinks.size(); i++) { // only the new links
            diggingRecursive(grabHTMLLinks.get(i), length - 1);
        }
    }

    private String getHtmlSourceFromEnteredUrl(String enteredUrl) {
        StringBuilder htmlPage = new StringBuilder();
        InputStream in;
        try {
            URLConnection connection = handleUrlConnection(enteredUrl);

            // Read and store the result line by line then return the entire string.
            in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            for (String line; (line = reader.readLine()) != null; ) {
                htmlPage.append(line);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            return htmlPage.toString();
        }
        // get the whole page
        return htmlPage.toString();
    }

    private URLConnection handleUrlConnection(String enteredUrl) throws IOException {
        URLConnection connection = (createURL(enteredUrl)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        return connection;
    }

    private URL createURL(String urlLink) {
        URL url = null;
        try {
            url = new URL(urlLink);
        } catch (MalformedURLException e) {
            Log.d("Exception", "A problem with URL occured.");
            e.printStackTrace();
        }
        return url;
    }
}
