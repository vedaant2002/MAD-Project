package com.example.myproject3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myproject3.DatabaseHelper;
import com.example.myproject3.R;

public class AddNewsActivity extends AppCompatActivity {

    private static final int MINIMUM_WORD_COUNT = 10; // Minimum word count for description
    private DatabaseHelper dbHelper;
    private EditText titleEditText, descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        dbHelper = new DatabaseHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save news entry to the database
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                // Validate description word count
                if (TextUtils.isEmpty(description) || getWordCount(description) < MINIMUM_WORD_COUNT) {
                    descriptionEditText.setError("Description must be at least " + MINIMUM_WORD_COUNT + " words.");
                    return;
                }

                // Save the news entry if title and description are not empty and word count is valid
                if (!title.isEmpty() && !description.isEmpty()) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_TITLE, title);
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
                    db.insert(DatabaseHelper.TABLE_NAME, null, values);
                    db.close();

                    // Finish the activity after saving the entry
                    finish();
                }
            }
        });
    }

    // Helper method to count words in a string
    private int getWordCount(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }
}

