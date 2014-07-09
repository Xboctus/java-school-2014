import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerAddressCreater extends JPanel {
    private boolean ok;
    private JDialog dialog;
    private JTextField serverIp;
    private JTextField serverPort;
    private JButton okButton;

    public ServerAddressCreater(){
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.add(new Label("server ip:"));
        panel.add(serverIp = new JTextField("127.0.0.1"));
        panel.add(new Label("server port:"));
        panel.add(serverPort = new JTextField(""));
        add(panel, BorderLayout.CENTER);

        okButton = new JButton("ok");
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

    public boolean showDialog(Component parent, String title){
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

    public String getServerIP(){
        String serverIpText = serverIp.getText();
        return serverIpText;
    }

    public int getServerPort(){
        int serverPortInt = Integer.parseInt(serverPort.getText());
        return serverPortInt;
    }
}