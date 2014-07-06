import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserEventsTableModel extends AbstractTableModel {
    private User user;
    public static final int DATE_INDEX = 0;
    public static final int DESCRIPTION_INDEX = 1;
    public static final int HIDDEN_INDEX = 2;
    private String[] columnNames;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");

    public UserEventsTableModel(User user, String[] columnNames){
        this.columnNames = columnNames;
        this.user = user;
    }

    public int getRowCount(){
        return user.events.size();
    }

    public int getColumnCount(){
        return columnNames.length;
    }

    public Object getValueAt(int row, int column){
        Event event = this.user.events.get(row);
        switch (column){
            case DATE_INDEX:
                return sdf.format(event.getDate());
            case DESCRIPTION_INDEX:
                return event.getDescription();
            default:
                return new Object();
        }
    }

    public String getColumnName(int column){
        return columnNames[column];
    }

    public void setValueAt(Object value, int row, int column){
        Event event = this.user.events.get(row);
        try {
            switch (column) {
                case DATE_INDEX:
                    event.setDate(sdf.parse((String) value));
                    break;
                case DESCRIPTION_INDEX:
                    event.setDescription((String) value);
                    break;
                default:
            }
        }
        catch (ParseException pe){
            JOptionPane.showMessageDialog(null, "parse exception is happened");
        }

        fireTableCellUpdated(row, column);
    }

    public boolean isCellEditable(int row, int column){
        if (column == HIDDEN_INDEX) return false;
        else return true;
    }

//    public boolean hasEmptyRow() {
//        if (user.events.size() == 0) return false;
//        Event eventRecord = user.events.get(user.events.size() - 1);
//        if (eventRecord.getDate().toString().trim().equals("") && eventRecord.getDescription().trim().equals("")) {
//            return true;
//        }
//        else return false;
//    }

    public void addEmptyRow(){
        //user.events.add(new Event())
    }
}
