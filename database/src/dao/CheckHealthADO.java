package dao;


import core.CheckHealth;
import core.DataClass;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


public class CheckHealthADO implements ADO {
	
	private Connection myConn;
	
	public CheckHealthADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	
	private CheckHealth convertRowToHealth(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();

		val.add(myRs.getInt("номер медкомиссии"));
		
		try {
			val.add(convertDateToString(myRs.getDate("дата медкомиссии")));
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		val.add(myRs.getString("статус проверки"));
		
	
		return new CheckHealth(val);
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		try { 
			if (myStmt != null) myStmt.close();
			if (myRs != null) myRs.close();
		} catch (Exception e) {};
		
	}

	@Override
	public List<DataClass> getAll() throws Exception {
		List<DataClass> list = new ArrayList<>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from медкомиссия");
			
			
			while (myRs.next()) {
				CheckHealth tempCheckHealth = convertRowToHealth(myRs);
				list.add(tempCheckHealth);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	}

	@Override
	public DataClass getEmpty() {
		return new CheckHealth();
	}

	@Override
	public void update(DataClass tempCheckHealth) throws SQLException {
		
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `медкомиссия`" 
					+ " set `дата медкомиссии`=?, `статус проверки`=?"
					+ " where `номер медкомиссии`=?");
			myStmt.setDate(1, convertStringToDate(tempCheckHealth.getData(1).toString()));
			myStmt.setString(2, tempCheckHealth.getData(2).toString());
			myStmt.setInt(3, tempCheckHealth.getId());
			
			myStmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			myStmt.close();
		}
	}

	@Override
	public void add(DataClass tempCheckHealth) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into медкомиссия " + "values (NULL, ?, ?)");
			myStmt.setDate(1, convertStringToDate(tempCheckHealth.getData(1).toString()));
			myStmt.setString(2, (String) tempCheckHealth.getData(2));
			myStmt.executeUpdate();
		}
		catch(Exception exc) {
			System.out.println(exc.getMessage());
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
			myStmt = myConn.prepareStatement("delete from `медкомиссия` where `номер медкомиссии`=?");
			myStmt.setInt(1, id);
			myStmt.executeUpdate();
		}
		finally {
			myStmt.close();
		}
		
	}
	
	java.sql.Date convertStringToDate(String dateStr) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
		java.util.Date date = rus.parse(dateStr);
		return new java.sql.Date(date.getTime());
	}
	
	
	String convertDateToString(java.sql.Date date) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
		return rus.format(date);
	}
	


}