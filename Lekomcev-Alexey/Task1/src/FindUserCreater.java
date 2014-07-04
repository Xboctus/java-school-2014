import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindUserCreater extends JPanel {
    private boolean ok;
    private JDialog dialog;
    private JTextField username;
    private JButton okButton;
    private Validator nameValidator;

    public FindUserCreater(){
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.add(new Label("user name:"));
        panel.add(username = new JTextField(""));
        add(panel, BorderLayout.CENTER);

        okButton = new JButton("ok");
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
                if (nameValidator.validate(username.getText()) && (getUser() != null)) {
                    okButton.setEnabled(true);
                }
                else {
                    okButton.setEnabled(false);
                }
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean showDialog(Component parent, String title){
        nameValidator = new Validator("[A-Z]{1}.*");
        Frame owner = null;
        owner = (Frame) parent;

        if (dialog == null){
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }

        dialog.setLocationRelativeTo(parent);
        dialog.setTitle(title);
        dialog.setVisible(true);
        return ok;
    }

    public User getUser(){
        String name = username.getText();
        User user = Coordinator.getUser(name);
        return user;
    }
}