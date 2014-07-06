import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EventCreater extends JPanel{
    private JTextField username;
    private JTextField description;
    private JTextField date;
    private JButton okButton;
    private boolean ok;
    private JDialog dialog;
    private Validator tzValidator;
    private Validator nameValidator;
    private Validator dateValidator;
    private boolean isName = false;
    private boolean isDate = false;
    private static String DEFAULT_USER_NAME = "Ivan";
    private static String DEFAULT_EVENT = "Event1";
    private static String DEFAULT_DATE = new SimpleDateFormat("dd-MM-yy:hh:mm:ss").format(new Date());

    public EventCreater(){
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));
        panel.add(new JLabel("user name:"));
        panel.add(username = new JTextField(DEFAULT_USER_NAME));
        panel.add(new JLabel("description:"));
        panel.add(description = new JTextField(DEFAULT_EVENT));
        panel.add(new JLabel("date:"));
        panel.add(date = new JTextField(DEFAULT_DATE));
        add(panel, BorderLayout.CENTER);

        username.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check(){
                if (nameValidator.validate(username.getText())){isName = true;}
                else{
                    okButton.setEnabled(false);
                    isName =false;
                }
                if ((isName == true) && (isDate == true)){
                    okButton.setEnabled(true);
                }
            }
        });

        date.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check(){
                if (dateValidator.validate(date.getText())){
                    isDate = true;
                }
                else{
                    okButton.setEnabled(false);
                    isDate = false;
                }
                if ((isDate == true) && (isName == true)){
                    okButton.setEnabled(true);
                }
            }
        });
        okButton = new JButton("Ok");
        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = true;
                dialog.setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getUserName(){
        return username.getText();
    }

    public String getDescription(){
        return description.getText();
    }

    public String getDate(){
        return date.getText();
    }

    public boolean showDialog(Component parent, String title){
        nameValidator = new Validator("[A-Z]{1}.*");
        dateValidator = new Validator("[0-9]{1,2}-[0-9]{1,2}-[0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{2}");//dd-MM-yy:HH:mm:ss
        ok = false;

        Frame owner = null;
        owner = (Frame) parent;

        if (dialog == null){
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }

        dialog.setLocationRelativeTo(owner);
        dialog.setTitle(title);
        dialog.setVisible(true);
        return ok;
    }
}
