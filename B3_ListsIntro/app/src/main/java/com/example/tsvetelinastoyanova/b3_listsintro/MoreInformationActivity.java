package com.example.tsvetelinastoyanova.b3_listsintro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreInformationActivity extends AppCompatActivity {
    Planet planet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);
        planet = getIntent().getExtras().getParcelable(getResources().getString(R.string.planet_object));
        setPlanetAttributesToView();
    }

    public void openLinkInWebView(View view) {
        WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(planet.getUrl());
    }

    private void setPlanetAttributesToView() {
        TextView textView = findViewById(R.id.planet_name);
        textView.setText(planet.getName());
        textView = findViewById(R.id.planet_description);
        textView.setText(planet.getDescription());
        textView = findViewById(R.id.planet_url);
        textView.setText(planet.getUrl());
        ImageView imageView = findViewById(R.id.planet_picture);
        imageView.setImageDrawable(this.getResources().getDrawable(planet.getPictureId()));
    }
}
