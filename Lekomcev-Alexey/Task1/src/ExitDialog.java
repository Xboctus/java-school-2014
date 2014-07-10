import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitDialog extends JPanel{
    private JButton yesButton;
    private JButton noButton;
    private JButton cancelButton;
    private JDialog dialog;
    public static int YES_BUTTON = 0;
    public static int NO_BUTTON = 1;
    public static int CANCEL_BUTTON = 2;
    private static int RETURN_VALUE = YES_BUTTON;

    public ExitDialog(){
        setLayout(new BorderLayout());

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(1, 1));
        textFieldPanel.add(new JLabel("Do you want to save change?"));


        yesButton = new JButton("yes");
        yesButton.setEnabled(true);
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RETURN_VALUE = YES_BUTTON;
                dialog.setVisible(false);
            }
        });

        noButton = new JButton("no");
        noButton.setEnabled(true);
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RETURN_VALUE = NO_BUTTON;
                dialog.setVisible(false);
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RETURN_VALUE = CANCEL_BUTTON;
                dialog.setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.add(cancelButton);
        add(textFieldPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public int showDialog(Component parent, String title){
        Frame owner = null;
        owner = (Frame) parent;

        if (dialog == null){
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(yesButton);
            dialog.pack();
        }
        dialog.setTitle(title);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return RETURN_VALUE;
    }
}




