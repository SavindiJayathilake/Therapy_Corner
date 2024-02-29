package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DailyBoostActivity extends AppCompatActivity {

    private TextView selfCareTipTextView;
    private Button nextTipButton;
    private Button prevTipButton;

    private String[] selfCareTips = {
            "I am capable of achieving great things.",
            "I deserve happiness and success in all aspects of my life.",
            "I am enough, just as I am.",
            "I trust in my abilities to overcome challenges.",
            "I embrace change as an opportunity for growth.",
            "I am worthy of love and respect.",
            "I choose positivity and optimism in every situation.",
            "I am the architect of my own destiny.",
            "I forgive myself for past mistakes and learn from them.",
            "I am resilient and can handle whatever comes my way.",
            "I am surrounded by abundance and opportunities.",
            "I attract positivity and abundance into my life.",
            "I radiate confidence and self-assurance.",
            "I am at peace with who I am and where I am in life.",
            "I release all negativity and embrace positivity.",
            "I am in control of my thoughts and emotions.",
            "I trust the journey, even when I do not understand it.",
            "I am worthy of self-care and self-love.",
            "I am grateful for the lessons life presents to me.",
            "I am empowered to create the life I desire.",
            "I am open to receiving all the blessings that come my way.",
            "I am constantly evolving and growing into a better version of myself.",
            "I am courageous and can face challenges with confidence.",
            "I am the master of my own happiness and well-being.",
            "I am free to choose my own path and create my own destiny.",
            "I am surrounded by love, kindness, and support.",
            "I am resilient, and setbacks only make me stronger.",
            "I am deserving of success and abundance in all areas of my life.",
            "I am capable of achieving my dreams and goals.",
            "I am grateful for the beauty and joy that surrounds me every day.",
            "I am worthy of love, respect, and acceptance."
    };


    private int currentTipIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_boost_activity);

        Toolbar toolbar = findViewById(R.id.tooltbarboos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Self-Affirmations");


        selfCareTipTextView = findViewById(R.id.selfCareTipTextView);
        nextTipButton = findViewById(R.id.nextTipButton);
        prevTipButton = findViewById(R.id.prevTipButton);

        displayTip();

        nextTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextTip();
            }
        });

        prevTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousTip();
            }
        });
    }

    private void displayTip() {
        selfCareTipTextView.setText(selfCareTips[currentTipIndex]);
    }

    private void showNextTip() {
        currentTipIndex = (currentTipIndex + 1) % selfCareTips.length;
        displayTip();
    }

    private void showPreviousTip() {
        currentTipIndex = (currentTipIndex - 1 + selfCareTips.length) % selfCareTips.length;
        displayTip();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
