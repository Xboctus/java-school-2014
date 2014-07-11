import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


public class ShowInfoCreater extends JPanel{
    private JButton okButton;
    private JButton cancelButton;
    private JTextField tzname;
    private JTextField status;
    private boolean ok;
    private JDialog dialog;
    private User user;
    private JTable table;
    private int CELL_WIDTH = 200;
    private Validator tzValidator;
    private UserEventsTableModel tableModel;
    private static final String[] columnNames = {"Date", "Description", ""};

    public ShowInfoCreater(User p_user){
        user = p_user;
        setLayout(new BorderLayout());

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(2, 2));
        textFieldPanel.add(new JLabel("time zone:"));
        textFieldPanel.add(tzname = new JTextField(user.tz.getID()));
        textFieldPanel.add(new JLabel("status:"));
        textFieldPanel.add(status = new JTextField(user.status.toString()));

        JPanel tablePanel = new JPanel();
        tableModel = new UserEventsTableModel(user, columnNames);
        table = new JTable(tableModel);
        JTableHeader header = table.getTableHeader();
        table.getColumnModel().getColumn(0).setPreferredWidth(CELL_WIDTH);
        TableCellListener tcl = new TableCellListener(table, action);

        tablePanel.setLayout(new GridLayout(2,1));

        TableColumn hidden = table.getColumnModel().getColumn(UserEventsTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);

        tablePanel.add(header, BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);

        okButton = new JButton("Ok");
        okButton.setEnabled(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = true;
                dialog.setVisible(false);
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
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
                if (tzValidator.validate(tzname.getText())) {
                    okButton.setEnabled(true);
                }
                else {
                    okButton.setEnabled(false);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(textFieldPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean showDialog(Component parent, String title){
        tzValidator = new Validator("GMT[+-]{1}[0-9]{1,2}");
        Frame owner = null;
        owner = (Frame) parent;

        if (dialog == null){
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }
        dialog.setTitle(title);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return ok;
    }

    public String getTZ(){
        return tzname.getText();
    }

    public String getStatus(){
        return status.getText();
    }

    Action action = new AbstractAction()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:hh:mm:ss");
        public void actionPerformed(ActionEvent e)
        {
            TableCellListener tcl = (TableCellListener)e.getSource();
            Date date = new Date();
            String oldDescription = "";
            String newDescription;
            if (!tcl.getOldValue().equals(tcl.getNewValue())){
                GraphicScheduler.modification = true;
                switch (tcl.getColumn()){
                    case (UserEventsTableModel.DATE_INDEX):
                        oldDescription = (String)table.getModel().getValueAt(tcl.getRow(), UserEventsTableModel.DESCRIPTION_INDEX);
                        user.changeEventDate((String) tcl.getNewValue(), oldDescription);
                        break;
                    case (UserEventsTableModel.DESCRIPTION_INDEX):
                        oldDescription = (String)tcl.getOldValue();
                        break;
                }
                newDescription = (String)table.getModel().getValueAt(tcl.getRow(), UserEventsTableModel.DESCRIPTION_INDEX);
                if (GraphicScheduler.getEventsOutput() == false){
                    Sheduler.getSortingEvents().changeTreeSet(user, oldDescription, newDescription);
                    return;
                }
                OutputEventsRunnable.changeTimers(user, oldDescription, newDescription);
            }
        }
    };
}




