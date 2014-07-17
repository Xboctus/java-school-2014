package com.javaschool2014.parser;

import java.util.HashMap;

public class Main implements Constants {

    // psvm + tab to invoke template
    public static void main(String[] args) {

        HashMap<String, String> resArgs = new HashMap<>();
        MainParser mainParser           = new MainParser();

        // Check args for algorithm parameter:
        for(String arg: args) {

            String[] arguments = arg.split(":");

            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = arguments[i].trim();
            }

            resArgs.put(arguments[0], arguments[1]);

        }

        // If present - SAX/DOM, SAX in case of wrong one:
        if (resArgs.containsKey("algorithm")) {

            if (resArgs.get("algorithm").equals("SAX")) {

                mainParser.parseSAX();

            } else if (resArgs.get("algorithm").equals("DOM")) {

                mainParser.parseDOM();

            } else {

                System.out.println("Wrong algorithm parameter, exit.");
                mainParser.parseSAX();

            }

        // If not - use SAX parser:
        } else {

            mainParser.parseSAX();

        }

    }

}