package dao;

import java.sql.SQLException;
import java.util.List;

import core.DataClass;

public interface ADO {

	public List<DataClass> getAll() throws Exception;
	public DataClass getEmpty();
	public void update(DataClass tempClient) throws SQLException;
	public void add(DataClass tempClient) throws SQLException;
	public void delete(int id) throws SQLException;
	
}
