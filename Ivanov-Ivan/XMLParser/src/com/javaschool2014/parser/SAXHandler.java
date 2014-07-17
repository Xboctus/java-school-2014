package com.javaschool2014.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SAXHandler extends DefaultHandler implements Constants {

    // Parsed data contains here:
    private List<Book> books  = new ArrayList<Book>();

    // These stacks are used to get current/parent element names and current book object:
    private Stack<String> elementStack = new Stack<String>();
    private Stack<Object> objectStack  = new Stack<Object>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    // We should override default methods to suit our needs:
    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        // We push all start elements we find into elementStack:
        this.elementStack.push(qName);

        // But in case of book element - we get it's id attr value and add it in objectStack as well:
        if ("book".equals(qName)) {

            Book book = new Book();
            book.setBookId(attributes.getValue("id"));
            this.objectStack.push(book);
            this.books.add(book);

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // When closing element is found, remove the opening one from the elementStack:
        this.elementStack.pop();

        // If it is a book closing element, remove it's object form objectStack as well:
        if ("book".equals(qName)) {
            Object object = this.objectStack.pop();
        }

    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        String value = new String(ch, start, length).trim();

        // Ignore white spaces:
        if (value.length() == 0) {
            return;
        }

        if ("author".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            book.setAuthor(value);

        } else if ("title".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            book.setTitle(value);

        } else if ("genre".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            book.setGenre(value);

        } else if ("isbn".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            book.setIsbn(value);

        } else if ("price".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();

            try {

                book.setPrice(Float.parseFloat(value));

            } catch (NumberFormatException e) {
                System.out.println("exception: " + e.getMessage());
            }

        } else if ("publish_date".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            Date date = null;

            try {

                date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(value);
                book.setPublishDate(value);

            } catch (ParseException e) {
                System.out.println("exception: " + e.getMessage());
            }

        } else if ("description".equals(currentElement()) && "book".equals(currentElementParent())) {

            Book book = (Book) this.objectStack.peek();
            book.setDescription(value);

        }

    }

    private String currentElement() {

        return this.elementStack.peek();

    }

    private String currentElementParent() {

        if(this.elementStack.size() < 2) {
            return null;
        }

        return this.elementStack.get(this.elementStack.size()-2);

    }

}