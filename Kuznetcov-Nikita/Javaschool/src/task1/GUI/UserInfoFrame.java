package task1.GUI;

import task1.*;
import task1.Event;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import task1.Util.DateFormatter;

/**
 * Created by Sunrise on 11.07.2014.
 */
public class UserInfoFrame extends TemplateFrame {

  private final String[] colNames = { "Event date", "Event text" };

  public UserInfoFrame(final Coordinator taskCoordinator, final User user) {
    super("User info");
    contentPanel = new JPanel(new BorderLayout());
    JPanel inputPanel = new JPanel(new GridLayout(2, 2));

    JLabel userNameLabel = new JLabel("User name");
    JLabel timeZoneLabel = new JLabel("Time zone ID");
    JLabel userEventsLabel = new JLabel("User events");

    JFormattedTextField userNameInput = ComponentsFactory.getTextInputFieldInstance();
    userNameInput.setValue(user.getUserName());
    JFormattedTextField timeZoneInput = ComponentsFactory.getTimeZoneInputFieldInstance();
    timeZoneInput.setValue(user.getTimeZone().getID());

    final Event[] events = user.getEventsArray();

    JTable userEventsTable = new JTable(new AbstractTableModel() {
      @Override
      public int getRowCount() {
        return events.length;
      }
      @Override
      public int getColumnCount() {
        return colNames.length;
      }
      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
        Event event = events[rowIndex];
        switch (columnIndex) {
          case 0 :
            return DateFormatter.formatDate(event.getEventDate(), user.getTimeZone());
          case 1 :
            return event.getEventText();
          default :
            return "";
        }
      }
      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
      }
    });

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });

    inputPanel.add(userNameLabel);
    inputPanel.add(userNameInput);
    inputPanel.add(timeZoneLabel);
    inputPanel.add(timeZoneInput);
    contentPanel.add(inputPanel, BorderLayout.NORTH);

    contentPanel.add(userEventsLabel, BorderLayout.CENTER);

    contentPanel.add(userEventsTable, BorderLayout.SOUTH);

    add(contentPanel);

    this.setBounds(400, 200, 400, 200);
    showFrame();
  }

}
