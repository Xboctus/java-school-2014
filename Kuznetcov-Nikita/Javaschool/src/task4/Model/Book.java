package task4.Model;

import java.util.Date;

/**
 * Created by Sunrise on 14.07.2014.
 */
public class Book {

  private String author;
  private String title;
  private String genre;
  private String isbn;
  private double price;
  private Date publishDate;
  private String description;

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public Book() {
  }

  public Book(String author, String title, String genre, String isbn, double price, Date publishDate, String desc) {
    this.author = author;
    this.title = title;
    this.genre = genre;
    this.isbn = isbn;
    this.price = price;
    this.publishDate = publishDate;
    this.description = desc;
  }

  @Override
  public String toString() {
    return "Book info: " + LINE_SEPARATOR +
        "Author: " + author + LINE_SEPARATOR +
        "Title: " + title + LINE_SEPARATOR +
        "Genre: " + genre + LINE_SEPARATOR +
        "ISBN: " + isbn + LINE_SEPARATOR +
        "Price: " + price + LINE_SEPARATOR +
        "Date of publication: " + publishDate + LINE_SEPARATOR +
        "Description: " + description;
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

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Date getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(Date publishDate) {
    this.publishDate = publishDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
