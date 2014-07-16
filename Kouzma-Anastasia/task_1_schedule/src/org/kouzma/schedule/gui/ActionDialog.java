package org.kouzma.schedule.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

import org.kouzma.schedule.gui.ScheduleWindow.DialogCallBack;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Anastasya Kouzma
 */
public class ActionDialog extends JDialog {

	protected List<JTextField> arrControls = new ArrayList<JTextField>();
	private JButton okButton;
	/**
	 * Create the dialog.
	 * @param dialogCallBack 
	 */
	public ActionDialog(String title, String[] arrLabelNames, final InputType[] arrControlTypes, final DialogCallBack dialogCallBack) {
		setBounds(100, 100, 350, 200);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0};
		gbl_contentPanel.columnWidths = new int[] {100, 200};
		contentPanel.setLayout(gbl_contentPanel);

		for (int i = 0; i < arrLabelNames.length; i++) {
			JLabel lblNewLabel = new JLabel(arrLabelNames[i]);
			lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = i;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);

			JTextField textField = createTextField(arrControlTypes[i]);
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.fill = GridBagConstraints.BOTH;
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = i;
			contentPanel.add(textField, gbc_textField);
			arrControls.add(textField);
		}
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				List<String> arrParams = new ArrayList<String>();
				for (int i = 0; i < arrControls.size(); i++) {
					String paramText = arrControls.get(i).getText();
					if (paramText.equals("")) {
						JOptionPane.showMessageDialog(arrControls.get(i), "Empty field", "Error",
					              JOptionPane.ERROR_MESSAGE);
						return;
					}
					arrParams.add(paramText);
				}
				dialogCallBack.sendParams(arrParams);
				ActionDialog.this.setVisible(false);
				
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	
	private JTextField createTextField(InputType inputType) {
		switch (inputType) {
		case TEXT:
			return createTextInput();
		case TIMEZONE:
			return createTimeZoneInput();
		case STATUS:
			return createStatusInput();
		case DATE:
			return createDateInput();
		}
		return null;
	}

	private JTextField createTextInput() {
		DefaultFormatter textFormatter = new DefaultFormatter() {
			@Override
			public Object stringToValue(String string) throws ParseException {
 				Pattern textPattern = Pattern.compile("[a-zA-Zа-яА-Я].*");
				Matcher matcher = textPattern.matcher(string);
				if (!matcher.matches()) {
					return "";
				}
				if (string.length() > 255) {
					return string.substring(0, 255);
				} 
				else {
					return super.stringToValue(string);
				}
			}
		};
		
		JFormattedTextField textInput = new JFormattedTextField(textFormatter);

		return textInput;
	}
	
	private JTextField createTimeZoneInput() {
		DefaultFormatter gmtFormatter = new DefaultFormatter() {
			@Override
			public Object stringToValue(String string) throws ParseException {
				Pattern gmtPattern = Pattern.compile("(GMT[+-]\\d\\d?)(.*)");
				Matcher matcher = gmtPattern.matcher(string);
				if (!matcher.matches()) {
					return "GMT+0";
				} 
				else {
					return super.stringToValue(matcher.group(1));
				}
			}
		};
		
		JFormattedTextField timeZoneInput = new JFormattedTextField(gmtFormatter);
		timeZoneInput.setText("GMT+0");
		
		return timeZoneInput;
	}
	
	private JTextField createStatusInput() {
		//JComboBox statusInput = new JComboBox();
		JFormattedTextField statusInput = new JFormattedTextField(new MessageFormat("{0,choice,0#active|1#passive}"));
		return statusInput;
	}
	
	private JTextField createDateInput() {
		JFormattedTextField dateInput = null;
		try {
			dateInput = new JFormattedTextField(new MaskFormatter("##.##.####-##:##:##"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateInput;
	}
}
