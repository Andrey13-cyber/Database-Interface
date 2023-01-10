package dao;

import core.DataClass;
import core.Flight;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


public class FlightADO implements ADO {
	
	private Connection myConn;
	
	public FlightADO() throws Exception {
		
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
//	public List<Flight> getAllFlights() throws Exception {
//		List<Flight> list = new ArrayList<>();
//		
//		Statement myStmt = null;
//		ResultSet myRs = null;
//		
//		try {
//			myStmt = myConn.createStatement();
//			myRs = myStmt.executeQuery("select * from рейс");
//			
//			while (myRs.next()) {
//				Flight tempFlight = convertRowToFlight(myRs);
//				list.add(tempFlight);
//			}
//			return list;
//		}
//		finally {
//			close(myStmt, myRs);
//		}
	@Override
	public List<DataClass> getAll() throws Exception {
		List<DataClass> list = new ArrayList<>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from рейс");
			
			while (myRs.next()) {
				Flight tempFlight = convertRowToFlight(myRs);
				list.add(tempFlight); //todo convert
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	 
	};
	

	

	private Flight convertRowToFlight(ResultSet myRs) throws SQLException {
		
		
		List<Object> val = new ArrayList<Object>();
		
		val.add(myRs.getInt("номер рейса"));
		val.add(myRs.getString("пункт отправления"));
		val.add(myRs.getString("пункт назначения"));
		try {
			val.add(convertDateToString(myRs.getDate("дата")));
			val.add(convertTimeToString(myRs.getTime("время вылета")));
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		val.add(myRs.getInt("номер самолета"));
	
		return new Flight(val);
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		try { 
			if (myStmt != null) myStmt.close();
			if (myRs != null) myRs.close();
		} catch (Exception e) {};
		
	}



	@Override
	public DataClass getEmpty() {
		return new Flight();
	}

	@Override
	public void update(DataClass tempFlight) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `рейс`" 
					+ " set `пункт отправления`=?, `пункт назначения`=?, `дата`=?, `время вылета`=?, `номер самолета`=?"
					+ " where `номер рейса`=?");
			myStmt.setString(1, tempFlight.getData(1).toString());	
			myStmt.setString(2, tempFlight.getData(2).toString());
			myStmt.setDate(3, convertStringToDate(tempFlight.getData(3).toString()));
			myStmt.setTime(4, convertStringToTime(tempFlight.getData(4).toString()));
			myStmt.setInt(5, Integer.parseInt(tempFlight.getData(5).toString()));
			myStmt.setInt(6, tempFlight.getId());
			
			myStmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			myStmt.close();
		}		
		
	}

	@Override
	public void add(DataClass tempFlight) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into рейс " + "values (NULL, ?, ?, ?, ?, ?)");
			
			myStmt.setString(1, (String) tempFlight.getData(1));
			myStmt.setString(2, (String) tempFlight.getData(2));
			myStmt.setDate(3, convertStringToDate(tempFlight.getData(3).toString()));
			myStmt.setTime(4, convertStringToTime(tempFlight.getData(4).toString()));
			myStmt.setInt(5, Integer.parseInt(tempFlight.getData(5).toString()));
			myStmt.executeUpdate();
		}
		catch(Exception exc) {
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
			myStmt = myConn.prepareStatement("delete from `рейс` where `номер рейса`=?");
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
	
	java.sql.Time convertStringToTime(String timeStr) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
		java.util.Date date = rus.parse(timeStr);
		return new java.sql.Time(date.getTime());
	}
	
	String convertDateToString(java.sql.Date date) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
		return rus.format(date);
	}
	
	String convertTimeToString(java.sql.Time time) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
		return rus.format(time);
	}

}
