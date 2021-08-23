package com.harini.primary.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.harini.primary.R;

public class ViewExamPapers extends AppCompatActivity implements View.OnClickListener{

    private CardView card_sinhala_papers,card_english_papers,card_maths_papers,card_science_papers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam_papers);
        setupUI();
        setActions();
    }

    private void setupUI(){

        card_sinhala_papers = findViewById(R.id.card_sinhala_papers);
        card_english_papers = findViewById(R.id.card_english_papers);
        card_maths_papers = findViewById(R.id.card_maths_papers);
        card_science_papers = findViewById(R.id.card_science_papers);
    }

    private void setActions(){
        card_sinhala_papers.setOnClickListener(this);
        card_english_papers.setOnClickListener(this);
        card_maths_papers.setOnClickListener(this);
        card_science_papers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent sinpapers = new Intent(getApplicationContext(), com.harini.primary.Settings.class);
        startActivity(sinpapers);
        switch (v.getId()){

            case R.id.card_sinhala_papers:

                break;
            case R.id.card_english_papers:

                break;
            case R.id.card_maths_papers:

                break;
            case R.id.card_science_papers:

                break;
            default:
                return;

        }
    }
}