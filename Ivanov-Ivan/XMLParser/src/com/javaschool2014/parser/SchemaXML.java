package com.javaschool2014.parser;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class SchemaXML {

    // Returns an XML Schema in a Schema instance:
    public static Schema createSchema() {

        Schema schema = null;

        try {

            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            schema = factory.newSchema(new File("schema.xml"));
            return schema;

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            return null;

        }

    }

}