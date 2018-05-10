package com.example.tsvetelinastoyanova.b4_dynamiclistsrequeststhreading;

import android.util.Log;

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

public class Crawler {

    final CrawlerEventsListener listener;

    public Crawler(CrawlerEventsListener listener) {
        this.listener = listener;
    }

    private List<String> queueWithUrls = new ArrayList<>();
    boolean isCrawlingStopped = false;

    void startCrawling(String enteredUrl, int length) {
        new Thread(() -> {
            listener.onCrawlingStarted();
            diggingRecursive(enteredUrl, length);
        }).start();
    }

    void stopCrawling() {
        Log.d("stop", "Links are " + queueWithUrls.size());
        isCrawlingStopped = true;
    }

    List<String> getQueueWithUrls() {
        return queueWithUrls;
    }

    private void diggingRecursive(String enteredUrl, int length) {
        if (length == 0 || isCrawlingStopped) {
            return;
        }
        String page = getHtmlSourceFromEnteredUrl(enteredUrl);
        if (page.equals("")) {
            return;
        }

        Vector<String> grabHTMLLinks = new HTMLLinkExtractor().grabHTMLLinks(page);
        listener.onAllUrlsCrawled(grabHTMLLinks);
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
