package org.kouzma.schedule;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.kouzma.schedule.SheduleWindow.DialogCallBack;

public class ScheduleDialog extends JDialog {

	private List<JTextField> arrControls = new ArrayList<JTextField>();
	private JButton okButton;
	/**
	 * Create the dialog.
	 * @param dialogCallBack 
	 */
	public ScheduleDialog(String title, String[] arrLabelNames, ControlType[] arrControlTypes, final DialogCallBack dialogCallBack) {
		setBounds(100, 100, 350, 200);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
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
			textField.setColumns(10);
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
				for (JTextField ñontrol : arrControls) {
					arrParams.add(ñontrol.getText());
				}
				dialogCallBack.sendParams(arrParams);
				ScheduleDialog.this.setVisible(false);
				
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	private JTextField createTextField(ControlType controlType) {
		switch (controlType) {
		case TEXT:
			return createTextComponent();
		case TIMEZONE:
			return createTimeZoneComponent();
		case STATUS:
			return createStatusComponent();
		case DATE:
			return createDateComponent();
		}
		return null;
	}
	
	private JTextField createTextComponent() {
		JFormattedTextField textComponent = new JFormattedTextField();

		textComponent.setToolTipText("");
		textComponent.setColumns(10);
		return textComponent;
	}
	
	private JTextField createTimeZoneComponent() {
		MaskFormatter mask = null;
		try {
			mask = new MaskFormatter("GMT*##"); //TODO
			mask.setValidCharacters("+-1234567890");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFormattedTextField timeZoneComponent = new JFormattedTextField(mask);

		return timeZoneComponent;
	}
	
	private JTextField createStatusComponent() {
		//JComboBox statusComponent = new JComboBox();
		JFormattedTextField statusComponent = new JFormattedTextField(new MessageFormat("{0,choice,0#active|1#passive}"));
		return statusComponent;
	}
	
	private JTextField createDateComponent() {
		JFormattedTextField dateComponent = null;
		try {
			dateComponent = new JFormattedTextField(new MaskFormatter("##.##.####-##:##:##"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateComponent;
	}
}
