package XML;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Timer;

public class XmlFrame extends JFrame{
    private static final int TEXT_ROWS = 30;
    private static final int TEXT_COLUMNS = 30;
    private static JTextArea textArea;
    private JPanel outputPan;
    private static ArrayList<String> files = new ArrayList<String>();
    private static ArrayList<Book> books = new ArrayList<Book>();
    private JPanel buttonPan;
    private JRadioButton domButton;
    private JRadioButton saxButton;

    public XmlFrame(){
        textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        outputPan = new JPanel();
        buttonPan = new JPanel();
        buttonPan.setLayout(new GridLayout(10,1));

        ActionListener startTimer = new startTimerAction();

        addTextArea();

        domButton = new JRadioButton("dom");
        domButton.setMnemonic(KeyEvent.VK_B);
        domButton.setSelected(true);

        saxButton = new JRadioButton("sax");
        saxButton.setMnemonic(KeyEvent.VK_C);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(domButton);
        group.add(saxButton);

        addButton("startTimer", startTimer);
        buttonPan.add(domButton);
        buttonPan.add(saxButton);

        add(outputPan);
        add(buttonPan);
        setLayout(new FlowLayout());
        pack();
    }

    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        buttonPan.add(button);
    }

    private void addTextArea() {
        textArea.append((new Date().toString()) + '\n');
        JScrollPane scrollPane = new JScrollPane(textArea);
        outputPan.add(scrollPane);
    }

    public static ArrayList<String> getFiles(){
        return files;
    }

    public static ArrayList<Book> getBooks(){
        return books;
    }

    private class startTimerAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            TimerTask timerTask = new TimerTaskScan();
            String parsingMethod;
            parsingMethod = "sax";
            if (domButton.isSelected() == true){
                parsingMethod = "dom";
            }
            TimerTaskScan.setParsingMethod(parsingMethod);
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 10 * 1000);
        }
    }
}
