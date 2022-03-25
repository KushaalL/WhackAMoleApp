package com.example.whackamoleappkushaall;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.whackamoleappkushaall.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding2 = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = binding2.getRoot();
        setContentView(view);
        binding2.Main2GameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}