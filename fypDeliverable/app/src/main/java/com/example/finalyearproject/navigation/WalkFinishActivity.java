package com.example.finalyearproject.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalyearproject.R;

public class WalkFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_finish_activity);
        Button button = findViewById(R.id.finishWalk);

        button.setOnClickListener(v -> finish());
    }
}
