package ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import core.DataClass;

public class TableModel extends AbstractTableModel {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final int OBJECT_COL = -1;
	
	private String[] columnNames;
	private List<DataClass> clients;
	
	public TableModel(List<DataClass> theClients, String[] columnNames) {
		clients = theClients;
		this.columnNames = columnNames;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public int getRowCount() {
		return clients.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if (col == OBJECT_COL) return clients.get(row);
		return clients.get(row).getData(col);
	}
	
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

}
