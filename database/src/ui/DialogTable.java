package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.ADO;
import core.DataClass;
import core.User;

import javax.swing.JButton;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;


import java.util.List;

import javax.swing.SwingConstants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;


public class DialogTable extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private ADO commonADO;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnUpdate;
	private JButton btnNewButton;

	/**
	 * Create the frame.
	 * @param selectedUser 
	 * @throws Exception 
	 */
	public DialogTable(User loggedUser, ADO ado) throws Exception {
		try {
			commonADO = ado;
			
		} catch (Exception e) {
			JOptionPane.showInputDialog(this, "Error: " + e, JOptionPane.ERROR_MESSAGE);
		}
		
		setTitle(commonADO.getEmpty().getTitle());
		setBounds(100, 100, 640, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(19, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("200px"),
				ColumnSpec.decode("200px"),
				ColumnSpec.decode("200px"),},
			new RowSpec[] {
				RowSpec.decode("470px"),
				RowSpec.decode("30px"),}));
		
		table = new JTable();
		List<DataClass> list = null;
		list = commonADO.getAll();
		TableModel model = new TableModel(list, commonADO.getEmpty().getColumnNames());
		table.setModel(model); 
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, "1, 1, 3, 1, fill, fill");
		
		JButton btnAdd = new JButton("Добавить " + commonADO.getEmpty().getTitle());
		contentPane.add(btnAdd, "1, 2, left, center");
		btnAdd.setHorizontalAlignment(SwingConstants.LEFT);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddDialog dialog = new AddDialog(DialogTable.this, commonADO);
				dialog.setVisible(true);
			}
		});
		
		btnNewButton = new JButton("Удалить " + commonADO.getEmpty().getTitle());
		if (loggedUser.getIsAdmin()) {
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					
					if(row < 0) {
						JOptionPane.showMessageDialog(DialogTable.this, "Вы должны выбрать " + commonADO.getEmpty().getTitle(), "Ошибка", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					int response = JOptionPane.showConfirmDialog(DialogTable.this, "Удалить этого " + commonADO.getEmpty().getTitle() + "?", "Подтверждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(response != JOptionPane.YES_OPTION) {
						return;
					}
					DataClass tempClient =  (DataClass) table.getValueAt(row, TableModel.OBJECT_COL);
					
					commonADO.delete(tempClient.getId());
					System.out.println(tempClient);
					refreshWeightView();
					
					JOptionPane.showMessageDialog(DialogTable.this, commonADO.getEmpty().getTitle() + " удален успешно", commonADO.getEmpty().getTitle() + " удален", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(Exception exc) {
					JOptionPane.showMessageDialog(DialogTable.this, "Ошибка удаления " + commonADO.getEmpty().getTitle() + exc.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
			}
		 });
		} else {
			btnNewButton.setEnabled(false);
		}
		
		btnUpdate = new JButton("Обновить " + commonADO.getEmpty().getTitle());
		if (loggedUser.getIsAdmin()) {
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				
				if (row < 0) {
					JOptionPane.showMessageDialog(DialogTable.this, "Вы должны выбрать " + commonADO.getEmpty().getTitle(), commonADO.getEmpty().getTitle() + " не выбран", JOptionPane.ERROR_MESSAGE);
					return;
				}
				DataClass tempClient = (DataClass) table.getValueAt(row, TableModel.OBJECT_COL);


				AddDialog dialog = new AddDialog(DialogTable.this, commonADO, tempClient, true);
				
				dialog.setVisible(true);
			}
		 });
		}
		else {
			btnUpdate.setEnabled(false);
		}
		contentPane.add(btnUpdate, "2, 2, center, fill");
		contentPane.add(btnNewButton, "3, 2, right, fill");
	}


	public void refreshWeightView() {
		
		try {
			List<DataClass> list = commonADO.getAll();
			TableModel model = new TableModel(list, commonADO.getEmpty().getColumnNames());
			table.setModel(model);
		}
		catch(Exception exc) {
			JOptionPane.showMessageDialog(this,"Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

