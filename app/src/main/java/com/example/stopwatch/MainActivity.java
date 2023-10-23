package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton, stopButton, holdButton;
    private boolean isRunning = false;
    private long startTime, elapsedTime = 0;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = SystemClock.elapsedRealtime() - startTime + elapsedTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds %= 60;
            int milliseconds = (int) (millis % 1000);
            timerTextView.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        holdButton = findViewById(R.id.holdButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    startTime = SystemClock.elapsedRealtime();
                    timerHandler.postDelayed(timerRunnable, 0);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;
                    elapsedTime += SystemClock.elapsedRealtime() - startTime;
                    timerHandler.removeCallbacks(timerRunnable);
                }
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;
                    timerHandler.removeCallbacks(timerRunnable);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
