package dao;


import core.CheckAirplane;
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


public class CheckAirplaneADO implements ADO {
	
	private Connection myConn;
	
	public CheckAirplaneADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	
	private CheckAirplane convertRowToAirplane(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();

		val.add(myRs.getInt("номер проверки"));
		
		try {
			val.add(convertDateToString(myRs.getDate("дата проверки")));
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		val.add(myRs.getString("состояние"));
		
	
		return new CheckAirplane(val);
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
			myRs = myStmt.executeQuery("select * from исправность");
			
			while (myRs.next()) {
				CheckAirplane tempCheck = convertRowToAirplane(myRs);
				list.add(tempCheck);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	}

	@Override
	public DataClass getEmpty() {
		return new CheckAirplane();
	}

	@Override
	public void update(DataClass tempCheck) throws SQLException {
		
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `исправность`" 
					+ " set `дата проверки`=?, `состояние`=?"
					+ " where `номер проверки`=?");
			myStmt.setDate(1, convertStringToDate(tempCheck.getData(1).toString()));
			myStmt.setString(2, tempCheck.getData(2).toString());
			myStmt.setInt(3, tempCheck.getId());
			
			myStmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			myStmt.close();
		}
	}

	@Override
	public void add(DataClass tempCheck) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into исправность " + "values (NULL, ?, ?)");
			myStmt.setDate(1, convertStringToDate(tempCheck.getData(1).toString()));
			myStmt.setString(2, (String) tempCheck.getData(2));
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
			myStmt = myConn.prepareStatement("delete from `исправность` where `номер проверки`=?");
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