package com.example.myproject3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView newsTextView;
    private static final int REQUEST_CODE_VIEW_NEWS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsTextView = findViewById(R.id.newsTextView);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddNewsActivity when Add News button is clicked
                Intent intent = new Intent(MainActivity.this, AddNewsActivity.class);
                startActivity(intent);
            }
        });

        Button viewButton = findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open NewsListActivity to view the news list
                Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_VIEW_NEWS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VIEW_NEWS && resultCode == RESULT_OK) {
            // Get the selected news details from the result Intent
            int selectedNewsId = data.getIntExtra("SELECTED_NEWS_ID", -1);
            String selectedNewsTitle = data.getStringExtra("SELECTED_NEWS_TITLE");
            String selectedNewsDescription = data.getStringExtra("SELECTED_NEWS_DESCRIPTION");

            // Display the selected news details in the newsTextView
            if (selectedNewsId != -1 && selectedNewsTitle != null && selectedNewsDescription != null) {
                String newsDetails = "                         ID: " + selectedNewsId + "\n"+
                                     ",                       Title: " + selectedNewsTitle.toUpperCase() +"\n\n\n"
                       + ", Description: " + selectedNewsDescription + "\n";
                newsTextView.setText(newsDetails);
            }
        }
    }
}