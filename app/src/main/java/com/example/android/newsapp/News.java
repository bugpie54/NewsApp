package com.example.android.newsapp;


public class News {

    /** Title of the article */
    private String mTitle;

    /** Section name */
    private String mSection;

    /** Author name */
    private String mAuthor;

    /** Date published */
    private String mDate;

    /** Website URL of the article */
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the article
     * @param section is the section name of the article
     * @param date is the article published date
     * @param url is the website URL to read the full article
     */
    public News(String title, String section, String author, String date, String url) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section name of the article.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the article author name.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the article published date.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the website URL for the full article.
     */
    public String getUrl() {
        return mUrl;
    }
}
