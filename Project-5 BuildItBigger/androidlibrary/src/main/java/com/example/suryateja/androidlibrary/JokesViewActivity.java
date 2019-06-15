package com.example.suryateja.androidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokesViewActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_view);
        textView = findViewById(R.id.jokestextView);
        String myjoke = getIntent().getStringExtra("MYJOKES");
        textView.setText(myjoke);
    }
}
