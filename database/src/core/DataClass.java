package core;

import java.util.ArrayList;
import java.util.List;

public abstract class DataClass {
	
	//private int id;
	private List<Object> data;
	protected String[] columnNames;
	protected String title;
	
	final public int getId() { return (Integer) data.get(0); } 
	
	public String[] getColumnNames() { return columnNames; }
	
	public void setCol(String[] columnNames) { this.columnNames  = columnNames; }
			
	public Object getData(int index) { return data.get(index);}
	public void setData(List<Object> data) { this.data = data; }
	
	
	public String getTitle() {
		return title;
	}

	public List<Object> getAllData() { return data; }
	
	protected DataClass() {
		data = new ArrayList<Object>();
		data.add(-1);
	}
	
	public DataClass(List<Object> data) { this.data = data; }
	
	@Override
	public final String toString() {
		String res = "";
		for (int i = 0; i < columnNames.length; i++) {
			res += columnNames[i];
			if (i < data.size()) {
				res += " = " + data.get(i).toString();
			}
			res += ", ";
		}
		return res;
	}
	
}
