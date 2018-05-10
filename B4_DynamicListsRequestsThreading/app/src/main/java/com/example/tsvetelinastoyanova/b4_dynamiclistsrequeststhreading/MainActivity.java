package com.example.tsvetelinastoyanova.b4_dynamiclistsrequeststhreading;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String URL_PATTERN = "^(https?:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";

    private String url;
    private String depth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCrawling(View view) {
       initializeInputs();
       validateInputs();
    }

    private void initializeInputs() {
        TextInputEditText url_input = findViewById(R.id.url_input);
        url = url_input.getText().toString();
        TextInputEditText depth_input = findViewById(R.id.depth_input);
        depth = depth_input.getText().toString();

        // hard coded:
       // url = "https://github.com/kstpr";
       // depth = "2";

    }

    private void validateInputs() {
        boolean isUrlValid = validateUrl(url);
        boolean isDepthValid = validateNumber(depth);

       /* if (!isOnline()) {
            Toast.makeText(this, this.getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (isUrlValid && isDepthValid) {
            startCrawlingActivity();
        } else if (!isUrlValid) {
            Toast.makeText(this, this.getString(R.string.wrong_url_prompt), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, this.getString(R.string.wrong_depth_prompt), Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrawlingActivity() {
        Intent startCrawlingIntent = new Intent(this, CrawlingActivity.class);
        startCrawlingIntent.putExtra(this.getString(R.string.url_input), url);
        startCrawlingIntent.putExtra(this.getString(R.string.depth_input), depth);
        startActivity(startCrawlingIntent);
    }

    private boolean validateUrl(String url) {
        return Pattern.matches(URL_PATTERN, url);
    }

    private boolean validateNumber(String number) {
        if (number.equals("")) {
            depth = "100"; // = forever ?
            return true;
        }
        return Pattern.matches("\\d+", number);
    }

    /*public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }*/

    /*
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            // is this reliable ??
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }*/
}
