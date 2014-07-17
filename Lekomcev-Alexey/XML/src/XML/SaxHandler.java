package XML;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Stack;

public class SaxHandler extends DefaultHandler {

    private static ArrayList<Book> books;
    private boolean id = false;
    private boolean author = false;
    private boolean genre = false;
    private boolean isbn = false;
    private boolean price = false;
    private boolean publish_date = false;
    private boolean description = false;
    private boolean title = false;

    public void startDocument(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("id")) {
            id = true;
        }
        if (qName.equalsIgnoreCase("author")) {
            author = true;
        }
        if (qName.equalsIgnoreCase("genre")) {
            genre = true;
        }
        if (qName.equalsIgnoreCase("isbn")) {
            isbn = true;
        }
        if (qName.equalsIgnoreCase("price")) {
            price = true;
        }
        if (qName.equalsIgnoreCase("publish_date")) {
            publish_date = true;
        }
        if (qName.equalsIgnoreCase("description")) {
            description = true;
        }
        if (qName.equalsIgnoreCase("title")) {
            title = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (id == true){
            String value = new String(ch, start, length);
            id = false;
        }
        if (author == true){
            String value = new String(ch, start, length);
            author = false;
        }
        if (genre == true){
            String value = new String(ch, start, length);
            genre = false;
        }
        if (isbn == true){
            String value = new String(ch, start, length);
            isbn = false;
        }
        if (price == true){
            String value = new String(ch, start, length);
            price = false;
        }
        if (publish_date == true){
            String value = new String(ch, start, length);
            publish_date = false;
        }
        if (description == true){
            String value = new String(ch, start, length);
            description = false;
        }
        if (title == true){
            String value = new String(ch, start, length);
            title = false;
        }
    }
    public static void setBooks(ArrayList<Book> books) {
        SaxHandler.books = books;
    }
}