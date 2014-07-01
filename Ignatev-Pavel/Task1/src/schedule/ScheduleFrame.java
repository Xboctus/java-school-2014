/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



/**
 *
 * @author ip
 */
public class ScheduleFrame extends javax.swing.JFrame implements CListener{

	
	private GListener gl=null;
	private boolean isScheduling = false;
	
	/**
	 * Creates new form ScheduleFrame
	 */
	public ScheduleFrame() {
		initComponents();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
   		this.setLocation(x, y);
		this.setTitle("Scheduling");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jTextArea1.setEditable(false);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		       if (gl!=null) gl.alertDeath();
		    }
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
		jTextArea1 = new JEditorPane("text/html", "");
        jScrollPane1 = new javax.swing.JScrollPane();
       // jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTextArea1.setSize(100, 30);;
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("AddEvent");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("RemoveEvent");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Modify");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("ShowInfo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("CloneEvent");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("AddRandomTimeEvent");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("StartScheduling");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>    
	
	private MaskFormatter getMf(String mask,String characters){
		MaskFormatter mf = null;
		try {
			mf = new MaskFormatter(mask);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        mf.setValidCharacters(characters);
        mf.setAllowsInvalid(false);
        return mf;
		
	}
	
	private void sendToListener(String msg){
		final String out = msg;
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					
		    		gl.sendMessage(out);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}
			
		}).start();
		
	}
	
	//RemoveEvent
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
				new JLabel("Eventname"),
    			arg1 = new JTextField()
    	};
		JOptionPane.showMessageDialog(null, inputs, "RemoveEvent", JOptionPane.PLAIN_MESSAGE);
		String out = "RemoveEvent("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ")";
		sendToListener(out);
       
    }                                        
	//Modify
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	JCheckBox arg2 ;
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
				new JLabel("GMT"),
    			arg1 = new JFormattedTextField(getMf("GMT**","1234567890-+")),
    			arg2  = new JCheckBox("isActive")
    	};
		JOptionPane.showMessageDialog(null, inputs, "Modify", JOptionPane.PLAIN_MESSAGE);
		String str = (arg2.isSelected()) ?  MessagesAndRegularExpressions.activeName :
				MessagesAndRegularExpressions.passiveName;
		String out = "Modify("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ","
				+ str
				+ ")";
		sendToListener(out);
    }                                        
	//ShowInfo
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	final JTextField arg0;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
		};
    	JOptionPane.showMessageDialog(null, inputs, "ShowInfo", JOptionPane.PLAIN_MESSAGE);
		final String out = "ShowInfo("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ")";
		
		
		
		sendToListener(out);
		
		
	}  
    
    @Override
    public void showInfo(User user){
    	
    	
    	final JTable table = new javax.swing.JTable();
    	table.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {

	            },
	            new String [] {
	                "Eventname", "Date"
	            }
	        ) {
	            boolean[] canEdit = new boolean [] {
	                false, true
	            };

	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
        });
    	
    	/*
    	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

    	   			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int i = arg0.getFirstIndex();
				
			}
    	});*/
    	
    	
    	
    	TableColumn tc = table.getColumnModel().getColumn(1);
        tc.setCellRenderer(new TableCellRenderer() {
        	@Override
			public Component getTableCellRendererComponent(JTable arg0,
					Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        		JTextField field =  new JFormattedTextField(getMf("**.**.****-**:**:**","1234567890"));
        		
        		String str = (String) arg1;
        		field.setText(str);
        		return field;
			}
        });
    	
    	
    	Iterator<Event> it = user.iterator();
		String title =  user.getName() + " "
				+ user.getTimeZone().getID() + " "
				+ ((user.isActive()) ? MessagesAndRegularExpressions.activeName : MessagesAndRegularExpressions.passiveName);
		
		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		while (it.hasNext()) {
			
			Event event = it.next();
			SimpleDateFormat dateFormat = new SimpleDateFormat(MessagesAndRegularExpressions.dateTimeFormat);
			model.addRow(new Object[]{event.getElement().getText(),dateFormat.format(event.getElement().getDate())});
					
			//out = out + event.getElement().getText() + " " + event.getElement().getDate().toString() + (it.hasNext() ? "\n":"");
		}
		
		
		JButton arg1 = new JButton();
    	JButton arg2 = new JButton();
    	final User u =user;
    	arg1.setText("Add");
    	arg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	addEventToTable(evt,model,u);
            }
        });
    	
    	arg2.setText("Remove");
    	arg2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	removeEventFromTable(evt,table,u);
            }
        });
		    	
    	JScrollPane arg0;
    	arg0 = new JScrollPane();
    	arg0.setViewportView(table);
    	final JComponent[] inputs = new JComponent[] {
				new JLabel(title),
    			arg0,
    			arg1,
    			arg2,
		};
		JOptionPane.showMessageDialog(null, inputs,"User: " + user.getName(), JOptionPane.PLAIN_MESSAGE);
    	
    }
    
    
    
    
    
    
	//CloneEvent
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	JTextField arg2;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("From Username"),
    			arg0 = new JTextField(),
				new JLabel("Eventname"),
    			arg1 = new JTextField(),
				new JLabel("To Username"),
    			arg2 = new JTextField()
    	};
		JOptionPane.showMessageDialog(null, inputs, "CloneEvent", JOptionPane.PLAIN_MESSAGE);
		String out = "CloneEvent("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg2.getText().toString().replaceAll("\\s","")
				+ ")";
		sendToListener(out);
       
    }                                        
	//AddRandomTimeEvent
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	JTextField arg2;
    	JTextField arg3;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
    			new JLabel("Eventname"),
    	    	arg1 = new JTextField(),
				new JLabel("From"),
    			arg2 = new JFormattedTextField(getMf("**.**.****-**:**:**","1234567890")),
    			new JLabel("To"),
    			arg3 = new JFormattedTextField(getMf("**.**.****-**:**:**","1234567890")),
    	};
		JOptionPane.showMessageDialog(null, inputs, "AddRandomTimeEvent", JOptionPane.PLAIN_MESSAGE);
		String out = "AddRandomTimeEvent("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg2.getText().toString()
				+ ","
				+ arg3.getText().toString()
				+ ")";
		sendToListener(out);
    }                                        
	//StartScheduling
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null || isScheduling) return;
    	isScheduling = true;
    	sendToListener("StartScheduling");
    }                                        
	//Create
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) { 
    	if (gl==null) return;
    	   	
    	JTextField arg0;
    	JTextField arg1;
    	JCheckBox arg2 ;
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
				new JLabel("GMT"),
    			arg1 = new JFormattedTextField(getMf("GMT**","1234567890-+")),
    			arg2  = new JCheckBox("isActive")
    	};
		JOptionPane.showMessageDialog(null, inputs, "Create", JOptionPane.PLAIN_MESSAGE);
		String str = (arg2.isSelected()) ?  MessagesAndRegularExpressions.activeName :
				MessagesAndRegularExpressions.passiveName;
		String out = "Create("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ","
				+ str
				+ ")";
		sendToListener(out);
    }                                        
	//AddEvent
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	JTextField arg2;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("Username"),
    			arg0 = new JTextField(),
    			new JLabel("Eventname"),
    	    	arg1 = new JTextField(),
				new JLabel("Date"),
    			arg2 = new JFormattedTextField(getMf("**.**.****-**:**:**","1234567890")),
    	};
		JOptionPane.showMessageDialog(null, inputs, "AddEvent", JOptionPane.PLAIN_MESSAGE);
		String out = "AddEvent("
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg2.getText().toString()
				+ ")";
		sendToListener(out);
    }                                        
    //AddEventToTable
    private void addEventToTable(java.awt.event.ActionEvent evt,DefaultTableModel model,User user) {                                         
    	if (gl==null) return;
	   	
    	JTextField arg0;
    	JTextField arg1;
    	final JComponent[] inputs = new JComponent[] {
				new JLabel("Eventname"),
    	    	arg0 = new JTextField(),
				new JLabel("Date"),
    			arg1 = new JFormattedTextField(getMf("**.**.****-**:**:**","1234567890")),
    	};
		JOptionPane.showMessageDialog(null, inputs, "AddEvent", JOptionPane.PLAIN_MESSAGE);
		SimpleDateFormat dateFormat = new SimpleDateFormat(MessagesAndRegularExpressions.dateTimeFormat);
		try {
			model.addRow(new Object[]{arg0.getText().toString().replaceAll("\\s","")
					,dateFormat.format(dateFormat.parse(arg1.getText().toString()))});
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String out = "AddEvent("
				+ user.getName()
				+ ","
				+ arg0.getText().toString().replaceAll("\\s","")
				+ ","
				+ arg1.getText().toString()
				+ ")";
		sendToListener(out);
	}     
    
  //RemoveEventFromTable
    private void removeEventFromTable(java.awt.event.ActionEvent evt,JTable table,User user) {                                         
    	if (gl==null || table.getSelectedRow()==-1) return;
    	DefaultTableModel model =  (DefaultTableModel) table.getModel();
    	String eName = (String)model.getValueAt(table.getSelectedRow(), 0);
    	model.removeRow(table.getSelectedRow());
		
		
    	String out = "RemoveEvent("
				+ user.getName()
				+ ","
				+ eName
				+ ")";
    	
		sendToListener(out);
	}     
    
  
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				} //New JTextArea().
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ScheduleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ScheduleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ScheduleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ScheduleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        //</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ScheduleFrame().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane jTextArea1;
    // End of variables declaration                void alertDeath();   
	@Override
	public synchronized void addText(String msg, boolean isSchedule) {
		Document doc = jTextArea1.getDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, (isSchedule ? Color.BLUE : Color.BLACK));
		try {
			doc.insertString(doc.getLength(), msg, keyWord);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//jTextArea1.setText(jTextArea1.getText() + msg);
		jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
		
	}

	@Override
	public void setGListener(GListener gl) {
		this.gl=gl;
		
	}
}