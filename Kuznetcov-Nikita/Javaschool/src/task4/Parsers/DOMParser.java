package task4.Parsers;

import org.w3c.dom.*;
import task4.Model.Book;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sunrise on 14.07.2014.
 */
public class DOMParser {

  private static final String dateFormat = "yyyy-MM-dd";
  private static final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

  public static ArrayList<Book> parseCatalog(File target) throws Exception { // todo normal ex handling
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = factory.newDocumentBuilder();

    Document doc = builder.parse(target);

    Element root = doc.getDocumentElement();
    if (!root.getTagName().equals("catalog"))
      throw new IllegalArgumentException("Root element of target file is not 'catalog'!");

    NodeList booksNodes = root.getChildNodes();
    ArrayList<Book> result = new ArrayList<>(booksNodes.getLength());

    for (int i = 0; i < booksNodes.getLength(); i++) {
      if (booksNodes.item(i) instanceof Element) {
        Book book = new Book();
        NodeList bookParams = booksNodes.item(i).getChildNodes();
        for (int j = 0; j < bookParams.getLength(); j++) {
          Node param = bookParams.item(j);
          if (param instanceof Element) {
            Text text = (Text) param.getFirstChild();
            String data = text.getData().trim();
            switch (((Element) param).getTagName()) {
              case "author": {
                book.setAuthor(data);
                break;
              }
              case "title": {
                book.setTitle(data);
                break;
              }
              case "genre": {
                book.setGenre(data);
                break;
              }
              case "isbn": {
                book.setIsbn(data);
                break;
              }
              case "price": {
                book.setPrice(Double.valueOf(data));
                break;
              }
              case "publish_date": {
                book.setPublishDate(formatter.parse(data));
                break;
              }
              case "description": {
                book.setDescription(data);
                break;
              }
            }
          }
        }
        result.add(book);
      }
    }
    return result;
  }

}
