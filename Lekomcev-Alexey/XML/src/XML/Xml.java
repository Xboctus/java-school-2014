package XML;

import javax.swing.*;


public class Xml {
    public static void main(String[] args){
        JFrame frame = new XmlFrame();
        frame.setTitle("xmlReader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        DB db = new DB();
        db.createTables();

        XmlClass xmlClass = new XmlClass();

    }
}
