package com.example.whackamoleappkushaall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whackamoleappkushaall.databinding.ActivityMain2Binding;
import com.example.whackamoleappkushaall.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {
    private ActivityMain3Binding binding3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding3 = ActivityMain3Binding.inflate(getLayoutInflater());
        View view = binding3.getRoot();
        setContentView(view);
        binding3.textViewScore.setText("You Got a Score of "+getIntent().getStringExtra(MainActivity.scoreInfo));
        binding3.textViewTime.setText("You lasted "+getIntent().getStringExtra(MainActivity.timeInfo)+" seconds");
        binding3.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainMenu = new Intent(MainActivity3.this,MainActivity2.class);
                startActivity(mainMenu);
            }
        });

    }
}