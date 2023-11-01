package com.example.myproject3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private List<News> newsList;
    private NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        dbHelper = new DatabaseHelper(this);
        newsList = getNewsData();

        adapter = new NewsListAdapter(this, newsList);
        ListView listView = findViewById(R.id.newsListView);
        listView.setAdapter(adapter);

        // Set item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected news item
                News selectedNews = newsList.get(position);

                // Create an Intent to send back the selected news details to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_NEWS_ID", selectedNews.getId());
                resultIntent.putExtra("SELECTED_NEWS_TITLE", selectedNews.getTitle());
                resultIntent.putExtra("SELECTED_NEWS_DESCRIPTION", selectedNews.getDescription());

                // Set the result code and send the Intent
                setResult(RESULT_OK, resultIntent);
                finish(); // Close the NewsListActivity
            }
        });

        // Set long click listener for the ListView to handle delete functionality
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected news item
                News selectedNews = newsList.get(position);

                // Delete the news item from the database
                deleteNews(selectedNews.getId());

                // Remove the news item from the list and update the adapter
                newsList.remove(position);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private List<News> getNewsData() {
        List<News> newsList = new ArrayList<>();

        // Open a readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_DESCRIPTION
        };

        // Query the database to get all news items
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,  // The table to query
                projection,                 // The columns to return
                null,                       // The columns for the WHERE clause
                null,                       // The values for the WHERE clause
                null,                       // Don't group the rows
                null,                       // Don't filter by row groups
                null                        // The sort order
        );

        // Iterate through the cursor and populate the newsList
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

            // Create a News object and add it to the newsList
            News news = new News();
            news.setId(id);
            news.setTitle(title);
            news.setDescription(description);

            newsList.add(news);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return newsList;
    }

    private void deleteNews(int newsId) {
        // Open a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define the WHERE clause
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newsId) };

        // Delete the news item
        db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);

        // Close the database
        db.close();
    }
}
