package XML;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.*;
import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class XmlClass {
    private static Schema schema = null;
    private static ArrayList<Book> books = XmlFrame.getBooks();

    public XmlClass(){
        String filePath = System.getProperty("user.dir")  + "/xmlfiles/schema.xsd";
        try{
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            schema = factory.newSchema(new File(filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void parseFileWithDom(String fileName){
        books.clear();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        builderFactory.setValidating(true);
        builderFactory.setNamespaceAware(true);
        try{
            builderFactory.setFeature("http://apache.org/xml/features/validation/schema", true);
            builderFactory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        try {
            builder = builderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }

        try {
            fileName = "./xmlfiles/" + fileName;
            document = builder.parse(new FileInputStream(fileName));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));
        }
        catch (org.xml.sax.SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        NodeList booksElement = document.getElementsByTagName("book");
        for (int i = 0; i < booksElement.getLength(); i++){
            Node bookElement = booksElement.item(i);
            if (bookElement.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) bookElement;
                String id = e.getAttribute("id");
                Book book = new Book();
                book.setId(id);
                book.setAuthor(((Element) bookElement).getElementsByTagName("author").item(0).getTextContent());
                book.setTitle(((Element) bookElement).getElementsByTagName("title").item(0).getTextContent());
                book.setGenre(((Element) bookElement).getElementsByTagName("genre").item(0).getTextContent());
                book.setPrice(Float.parseFloat(((Element) bookElement).getElementsByTagName("price").item(0).getTextContent()));
                book.setDescription(((Element) bookElement).getElementsByTagName("description").item(0).getTextContent());
                book.setPublish_date(((Element) bookElement).getElementsByTagName("publish_date").item(0).getTextContent());
                if (((Element) bookElement).getElementsByTagName("isbn").getLength() != 0){
                    book.setIsbn(((Element) bookElement).getElementsByTagName("isbn").item(0).getTextContent());
                }
                books.add(book);
            }
        }
        DB.insertIntoTable(books);
    }

    public static void parseFileWithSax(String fileName){
        books.clear();
        SaxHandler.setBooks(books);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            fileName = "./xmlfiles/" + fileName;
            InputStream xmlInput = new FileInputStream(fileName);
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new SaxHandler();
            saxParser.parse(xmlInput, handler);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


