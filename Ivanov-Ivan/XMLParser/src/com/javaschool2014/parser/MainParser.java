package com.javaschool2014.parser;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainParser {

    private Schema schema               = SchemaXML.createSchema();
    private Validator validator         = schema.newValidator();
    private SQLHandler sqlHandler       = new SQLHandler();
    private FolderMonitor folderMonitor = new FolderMonitor();

    public void parseSAX() {

        SAXParserFactory factory = SAXParserFactory.newInstance();



        try {

            SAXHandler handler   = new SAXHandler();
            SAXParser saxParser  = factory.newSAXParser();

            while (true) {

                String inputFile = folderMonitor.listenFolder();
                InputStream xmlInput = new FileInputStream(inputFile);

                try {
                    validator.validate(new StreamSource(new File(inputFile)));
                } catch (SAXException | IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    continue;
                }

                saxParser.parse(xmlInput, handler);

                sqlHandler.saveData(handler.getBooks());

            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("exceptions: " + e.getMessage());
        } finally {
            sqlHandler.closeConnection();
        }

    }

    public void parseDOM() {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {

            DOMHandler handler = new DOMHandler();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            while (true) {

                String inputFile = folderMonitor.listenFolder();
                InputStream xmlInput = new FileInputStream(inputFile);
                Document document = dBuilder.parse(xmlInput);

                try {
                    validator.validate(new DOMSource(document));
                } catch (SAXException e) {
                    System.out.println("Validation error: " + e.getMessage());
                    continue;
                }

                handler.parse(document);
                sqlHandler.saveData(handler.getBooks());

            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("exceptions: " + e.getMessage());
        } finally {
            sqlHandler.closeConnection();
        }

    }

}