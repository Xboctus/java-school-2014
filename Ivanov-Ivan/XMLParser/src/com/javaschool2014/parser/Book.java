package com.javaschool2014.parser;

import java.util.Date;

public class Book {

    private String bookId      = null;
    private String author      = null;
    private String title       = null;
    private String genre       = null;
    private String isbn        = null;
    private float  price       = 0.0F;
    private String   publishDate = null;
    private String description = null;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {

        return  "ID:          " + this.bookId                                 + "; \n" +
                "Author:      " + this.author                                 + "; \n" +
                "Title:       " + this.title                                  + "; \n" +
                "Genre:       " + this.genre                                  + "; \n" +
                "ISBN :       " + (this.isbn != null ? this.isbn : "no isbn") + "; \n" +
                "Price:       " + this.price                                  + "; \n" +
                "Published:   " + this.publishDate                            + "; \n" +
                "Description: " + this.description                            + "  \n";

    }

}