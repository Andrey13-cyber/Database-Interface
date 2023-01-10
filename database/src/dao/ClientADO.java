package dao;


import core.Client;
import core.DataClass;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientADO implements ADO {
	
	private Connection myConn;
	
	public ClientADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	

	
	public List<DataClass> getAll() throws Exception {
		List<DataClass> list = new ArrayList<>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from клиент");
			
			while (myRs.next()) {
				Client tempClient = convertRowToClient(myRs);
				list.add(tempClient);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	};
	
	
	private Client convertRowToClient(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();
		
		val.add(myRs.getInt("номер клиента"));
		val.add(myRs.getString("название предприятия"));
		val.add(myRs.getString("фио"));
		val.add(myRs.getString("телефон"));
	
		return new Client(val);
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		try { 
			if (myStmt != null) myStmt.close();
			if (myRs != null) myRs.close();
		} catch (Exception e) {};
		
	}

	@Override
	public DataClass getEmpty() {
		return new Client();
	}

	@Override
	public void update(DataClass tempClient) throws SQLException  {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `клиент`" 
					+ " set `название предприятия`=?, `фио`=?, `телефон`=?"
					+ " where `номер клиента`=?");
			myStmt.setString(1, tempClient.getData(1).toString());	
			myStmt.setString(2, tempClient.getData(2).toString());
			myStmt.setString(3, tempClient.getData(3).toString());
			myStmt.setInt(4, tempClient.getId());
			myStmt.executeUpdate();
			
		}finally {
			myStmt.close();
		}
		
	}

	@Override
	public void add(DataClass tempClient) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into `клиент`" 
					+ "values (NULL, ?, ?, ?)");
			myStmt.setString(1, (String) tempClient.getData(1));
			myStmt.setString(2, (String) tempClient.getData(2));
			myStmt.setString(3, (String) tempClient.getData(3));
			
			myStmt.executeUpdate();
		}
		catch(SQLException exc) {
			exc.getStackTrace();
		}
		finally {
			
				myStmt.close();
			
		}
		
	}

	@Override
	public void delete(int id) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("delete from `клиент` where `номер клиента`=?");
			
			myStmt.setInt(1, id);
			System.out.println(id);
			
			myStmt.executeUpdate();
		}
		finally {
			myStmt.close();
		}
		
	}

}