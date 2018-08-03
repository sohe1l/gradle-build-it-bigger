package com.example.android.jokeui;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {

    public final static String JOKE_KEY = "JOKE_KEY";

    private TextView mJokeTV;
    private Intent creatingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mJokeTV = findViewById(R.id.tv_joke);

        creatingIntent = getIntent();
        if(creatingIntent.hasExtra(JOKE_KEY)){
            mJokeTV.setText(creatingIntent.getStringExtra(JOKE_KEY));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, creatingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
