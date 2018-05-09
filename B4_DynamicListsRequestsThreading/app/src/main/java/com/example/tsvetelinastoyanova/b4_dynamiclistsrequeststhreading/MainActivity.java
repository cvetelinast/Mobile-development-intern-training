package com.example.tsvetelinastoyanova.b4_dynamiclistsrequeststhreading;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // here or in strings.xml is a better practice ?
    private static final String URL_PATTERN = "^(https?:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";

    private String url;
    private String depth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCrawling(View view) {

        // Todo: 
        // -case withouth internet
        // - case rotate screen

        // hard coded:
        Intent startCrawlingIntent = new Intent(this, CrawlingActivity.class);
        startCrawlingIntent.putExtra(this.getString(R.string.url_input), "https://github.com/kstpr");
        startCrawlingIntent.putExtra(this.getString(R.string.depth_input), "2");
        startActivity(startCrawlingIntent);

     // execute this:

     /*  initializeInputs();
       validateInputs();*/
    }

    private void initializeInputs() {
        TextInputEditText url_input = findViewById(R.id.url_input);
        url = url_input.getText().toString();
        TextInputEditText depth_input = findViewById(R.id.depth_input);
        depth = depth_input.getText().toString();
    }

    private void validateInputs() {
        boolean isUrlValid = validateUrl(url);
        boolean isDepthValid = validateNumber(depth);

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
        if(number.equals("")) {
            depth = "100"; // = forever ?
            return true;
        }
        return Pattern.matches("\\d+", number);
    }
}
