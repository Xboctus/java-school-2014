package XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private String id;
    private String author;
    private String genre;
    private String isbn;
    private float price;
    private Date publish_date;
    private String description;
    private String title;

    public Book(){}

    public void setPrice(float price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublish_date(String publish_date) {
        try {
            this.publish_date = new SimpleDateFormat("yyyy-MM-dd").parse(publish_date);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public Date getPublish_date() {

        return publish_date;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }
 }
