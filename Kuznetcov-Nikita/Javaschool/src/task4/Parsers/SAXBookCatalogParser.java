package task4.Parsers;

import task4.Model.Book;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sunrise on 14.07.2014.
 */
public class SAXBookCatalogParser {

  private static final String dateFormat = "yyyy-MM-dd";
  private static final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

  public static ArrayList<Book> parseCatalog(File target) throws Exception { // todo good exception handling
    ArrayList<Book> result = new ArrayList<>();

    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(true);

    SAXParser saxParser = factory.newSAXParser();
    SAXBookCatalogHandler handler = new SAXBookCatalogHandler(result);

    saxParser.parse(target, handler);

    return result;
  }

}
