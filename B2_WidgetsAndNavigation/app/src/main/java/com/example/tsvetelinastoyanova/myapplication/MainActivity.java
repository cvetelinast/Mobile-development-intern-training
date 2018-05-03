package com.example.tsvetelinastoyanova.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyApp", "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MyApp", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyApp", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MyApp", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyApp", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyApp", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyApp", "onPause");
    }

    public void openFirstScreen(View view) {
        Intent openFirstScreenIntent = new Intent(this,FirstScreenActivity.class);
        startActivity(openFirstScreenIntent);
    }

    public void openSecondScreen(View view) {
        Intent openSecondScreenIntent = new Intent(this,SecondScreenActivity.class);
        startActivity(openSecondScreenIntent);
    }

}
