package dao;

import core.Weight;
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


public class WeightADO implements ADO {
	
	protected Connection myConn;
	
	public WeightADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	
	protected Weight convertRowToWeight(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();

		val.add(myRs.getInt("номер груза"));
		val.add(myRs.getInt("вес"));
		val.add(myRs.getString("состояние перевозки"));
		try {
			val.add(convertDateToString(myRs.getDate("дата доставки")));
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new Weight(val);
	}

	protected void close(Statement myStmt, ResultSet myRs) throws SQLException {
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
			myRs = myStmt.executeQuery("select * from груз");
			
			while (myRs.next()) {
				Weight tempWeight = convertRowToWeight(myRs);
				list.add(tempWeight);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	}

	@Override
	public DataClass getEmpty() {
		return new Weight();
	}

	@Override
	public void update(DataClass tempClient) throws SQLException {
		
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `груз`" 
					+ " set `вес`=?, `состояние перевозки`=?, `дата доставки`=?"
					+ " where `номер груза`=?");
			myStmt.setInt(1, Integer.parseInt(tempClient.getData(1).toString()));	
			myStmt.setString(2, tempClient.getData(2).toString());
			myStmt.setDate(3, convertStringToDate(tempClient.getData(3).toString()));
			myStmt.setInt(4, tempClient.getId());
			
			myStmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			myStmt.close();
		}
	}

	@Override
	public void add(DataClass tempClient) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into груз " + "values (NULL, ?, ?, ?)");
			System.out.println(myStmt);
			myStmt.setInt(1, Integer.parseInt(tempClient.getData(1).toString()));
			myStmt.setString(2, (String) tempClient.getData(2));
			myStmt.setDate(3, convertStringToDate(tempClient.getData(3).toString()));
			System.out.println(myStmt);
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
			myStmt = myConn.prepareStatement("delete from `груз` where `номер груза`=?");
			myStmt.setInt(1, id);
			myStmt.executeUpdate();
		}
		finally {
			myStmt.close();
		}
		
	}
	
	protected java.sql.Date convertStringToDate(String dateStr) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
		java.util.Date date = rus.parse(dateStr);
		return new java.sql.Date(date.getTime());
	}
	
	
	protected String convertDateToString(java.sql.Date date) throws ParseException {
		SimpleDateFormat rus = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
		return rus.format(date);
	}
	


}