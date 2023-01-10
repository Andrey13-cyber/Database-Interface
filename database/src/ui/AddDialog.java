package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;


import core.Client;
import core.DataClass;
import dao.ADO;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class AddDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private List<JTextField> fields = new ArrayList();
 	
	private ADO commonADO;
	private DialogTable testApp;
	
	private DataClass previousClient = null;
	private boolean updateMode = false;
	
	public AddDialog(DialogTable theTestApp, ADO theCommonADO, DataClass thePreviousRecord, boolean theUpdateMode) {
		this(theCommonADO);
		commonADO = theCommonADO;
		testApp = theTestApp;
		previousClient = thePreviousRecord;
		updateMode = theUpdateMode;
		
		if (updateMode) {
			try {
				setTitle(commonADO.getEmpty().getTitle());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			populateGUI(previousClient);
		}
	}
	
	private void populateGUI(DataClass previousClient) {
		if (previousClient != null) {
			for (int i = 1; i < previousClient.getAllData().size(); i++) {
				fields.get(i- 1).setText(previousClient.getData(i).toString());
			}
		}
	}

	public AddDialog(DialogTable theTestApp, ADO theClientADO) {
		this(theClientADO);
		commonADO = theClientADO;
		testApp = theTestApp;
	}

	/**
	 * Create the dialog.
	 */
	public AddDialog(ADO theClientADO) {
		commonADO = theClientADO;
		setTitle("Добавить " + commonADO.getEmpty().getTitle());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
		        FormSpecs.DEFAULT_COLSPEC,
		        FormSpecs.RELATED_GAP_COLSPEC,
		        ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
					 FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,
				     FormSpecs.RELATED_GAP_ROWSPEC,
				     FormSpecs.DEFAULT_ROWSPEC,}));
		DataClass empty = commonADO.getEmpty();
		int y = 2;
		for (int i = 1; i < empty.getColumnNames().length; i++) {
			JLabel lblNewLabel = new JLabel(empty.getColumnNames()[i]);
			contentPanel.add(lblNewLabel, "2, " + y + ", left, center");
		
			fields.add(new JTextField());
			contentPanel.add(fields.get(fields.size() - 1), "4, " + y + ", fill, top");
			fields.get(fields.size() - 1).setColumns(10);
			y += 2;
		}
			
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Сохранить");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							saveAny();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Отмена");
		        cancelButton.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		            setVisible(false);
		            dispose();
		          }
		        });
		        cancelButton.setActionCommand("Cancel");
		        buttonPane.add(cancelButton);
			}
		}
	}

	protected void saveAny() throws ParseException {		
		
		List<Object> val = new ArrayList<Object>();
		if (previousClient != null) {
			val.add(previousClient.getId());
		} else {
			val.add(-1);
		}
		for (int i = 0; i < fields.size(); i++) {
			val.add(fields.get(i).getText());
		}
		
		DataClass tempClient = null;
		
		tempClient =  new Client(val);
		
		try {
			if (updateMode) {
				commonADO.update(tempClient);
			}
			else {
				commonADO.add(tempClient);
			}
			
			setVisible(false);
			dispose();
			
			testApp.refreshWeightView();
			
			JOptionPane.showMessageDialog(testApp, "added successfully", "added",
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}

}
