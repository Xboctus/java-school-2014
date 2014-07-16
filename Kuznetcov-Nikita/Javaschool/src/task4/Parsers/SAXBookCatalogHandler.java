package task4.Parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import task4.Model.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by Sunrise on 16.07.2014.
 */
public class SAXBookCatalogHandler extends DefaultHandler {

  private static final String dateFormat = "yyyy-MM-dd";
  private static final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

  private Collection result;
  private Book book;
  boolean isAuthor;
  boolean isTitle;
  boolean isGenre;
  boolean isIBSN;
  boolean isPrice;
  boolean isPublishDate;
  boolean isDescription;

  public SAXBookCatalogHandler(Collection collection) {
    result = collection;
  }

  public Collection getResult() {
    return result;
  }

  @Override
  public void startDocument() throws SAXException {
    System.out.println("Start parsing document...");
  }

  @Override
  public void endDocument() throws SAXException {
    System.out.println("SAX parsing complete!");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    System.out.println("Start parsing element " + qName + "...");
    if (qName.equalsIgnoreCase("BOOK")) {
      book = new Book();
    }
    switch (qName.toLowerCase()) {
      case "author": {
        isAuthor = true;
        break;
      }
      case "title": {
        isTitle = true;
        break;
      }
      case "genre": {
        isGenre = true;
        break;
      }
      case "isbn": {
        isIBSN = true;
        break;
      }
      case "price": {
        isPrice = true;
        break;
      }
      case "publish_date": {
        isPublishDate = true;
        break;
      }
      case "description": {
        isDescription = true;
        break;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    System.out.println("Parsing element " + qName + " completed!");
    if (qName.equalsIgnoreCase("BOOK")) {
      result.add(book);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    String data = new String(ch, start, length).trim();
    System.out.println("PARSED DATA: " + data);
    if (isAuthor) {
      book.setAuthor(data);
      isAuthor = false;
    }
    if (isTitle) {
      book.setTitle(data);
      isTitle = false;
    }
    if (isGenre) {
      book.setGenre(data);
      isGenre = false;
    }
    if (isIBSN) {
      book.setIsbn(data);
      isIBSN = false;
    }
    if (isPrice) {
      try {
        book.setPrice(Double.valueOf(data));
      } catch (NumberFormatException nfex) {
        System.out.println("Bad double format in string " + data);
      } finally {
        isPrice = false;
      }
    }
    if (isPublishDate) {
      try {
        book.setPublishDate(formatter.parse(data));
      } catch (ParseException parex) {
        System.out.println("Bad date format in string " + data);
      } finally {
        isPublishDate = false;
      }
    }
    if (isDescription) {
      book.setDescription(data);
      isDescription = false;
    }
  }

}
