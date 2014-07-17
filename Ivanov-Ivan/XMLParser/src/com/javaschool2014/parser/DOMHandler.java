package com.javaschool2014.parser;

import org.w3c.dom.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DOMHandler implements Constants {

    // Parsed data contains here:
    private List<Book> books  = new ArrayList<Book>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // Using "getElementById" parse method< to create contrast with SAX (the better way - "Looping" through nodes):
    public void parse(Document document) {

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("book");

        for (int currElement = 0; currElement < nodeList.getLength(); currElement++) {

            Node node = nodeList.item(currElement);

            if (node.getNodeType() == Node.ELEMENT_NODE) {


                Element element = (Element) node;
                Book book = new Book();

                book.setBookId(element.getAttribute("id"));

                if (checkNull(element, "author")) {
                    book.setAuthor(element.getElementsByTagName("author").item(0).getTextContent());
                }

                if (checkNull(element, "title")) {
                    book.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
                }

                if (checkNull(element, "genre")) {
                    book.setGenre(element.getElementsByTagName("genre").item(0).getTextContent());
                }

                if (checkNull(element, "isbn")) {
                    book.setIsbn(element.getElementsByTagName("isbn").item(0).getTextContent());
                }

                if (checkNull(element, "price")) {
                    book.setPrice(Float.parseFloat(element.getElementsByTagName("price").item(0).getTextContent()));
                }

                if (checkNull(element, "publish_date")) {

                    Date date = null;

                    try {

                        date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(element.getElementsByTagName("publish_date").item(0).getTextContent());
                        book.setPublishDate(element.getElementsByTagName("publish_date").item(0).getTextContent());

                    } catch (ParseException e) {
                        System.out.println("exception: " + e.getMessage());
                    }

                }

                if (checkNull(element, "description")) {
                    book.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
                }

                books.add(book);

            }

        }

    }

    private boolean checkNull(Element element, String name) {

        return (element.getElementsByTagName(name).item(0) != null);

    }

}