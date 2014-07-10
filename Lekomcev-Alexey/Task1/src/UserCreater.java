import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserCreater extends JPanel{
    private JTextField username;
    private JTextField tzname;
    private JTextField status;
    private JButton okButton;
    private boolean ok;
    private JDialog dialog;
    private Validator tzValidator;
    private Validator nameValidator;
    private boolean isName = false;
    private boolean isTZ = false;
    private static String DEFAULT_USER_NAME = "Ivan";
    private static String DEFAULT_TIMEZONE = "GMT+0";
    private static String DEFAULT_STATUS = "true";

    public UserCreater(){
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));
        panel.add(new JLabel("user name:"));
        panel.add(username = new JTextField(DEFAULT_USER_NAME));
        panel.add(new JLabel("time zone:"));
        panel.add(tzname = new JTextField(DEFAULT_TIMEZONE));
        panel.add(new JLabel("status:"));
        panel.add(status = new JTextField(DEFAULT_STATUS));
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
                if ((isName == true) && (isTZ == true)){
                    okButton.setEnabled(true);
                }
            }
        });

        tzname.getDocument().addDocumentListener(new DocumentListener() {
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
                if (tzValidator.validate(tzname.getText())){
                    isTZ = true;
                }
                else{
                    okButton.setEnabled(false);
                    isTZ = false;
                }
                if ((isTZ == true) && (isName == true)){
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

    public User getUser(){
        return new User(username.getText(), tzname.getText(), status.getText());
    }

    public boolean showDialog(Component parent, String title){
        nameValidator = new Validator("[A-Z]{1}.*");
        tzValidator = new Validator("GMT[+-]{1}[0-9]{1,2}");
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
