package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context  of the app
     * @param articles is the list of the news articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);

        }
        // Find the article at the given position in the list of articles
        News currentArticle = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = listItemView.findViewById(R.id.article_title);
        // Display the title of the article in that TextView
        assert currentArticle != null;
        titleView.setText(currentArticle.getTitle());

        // Find the TextView with view ID section name
        TextView sectionView = listItemView.findViewById(R.id.section_name);
        // Display the section name of the article in that TextView
        sectionView.setText(currentArticle.getSection());

        // Find the TextView with view ID section author
        TextView authorView = listItemView.findViewById(R.id.author);
        // Display the author name of the article in that TextView
        authorView.setText(currentArticle.getAuthor());

        // Find the TextView with the date ID
        TextView dateView = listItemView.findViewById(R.id.date);

        // Display the category of the current news in that TextView
        SimpleDateFormat originalDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            Date dateObject = originalDate.parse(currentArticle.getDate());
            String date = formattedDate.format(dateObject);
            dateView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listItemView;
    }

}
