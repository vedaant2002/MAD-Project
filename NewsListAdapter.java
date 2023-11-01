package com.example.myproject3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myproject3.News;
import java.util.List;

public class NewsListAdapter extends ArrayAdapter<News> {

    public NewsListAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News news = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.newsTitleTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        if (news != null) {
            titleTextView.setText(news.getTitle());

            // Set click listener for delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle delete button click
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    dbHelper.deleteNews(news.getId());

                    // Remove the item from the adapter
                    remove(news);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }
}
