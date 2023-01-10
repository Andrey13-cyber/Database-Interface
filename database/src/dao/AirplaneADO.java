package dao;


import core.Airplane;
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

public class AirplaneADO implements ADO {
	
	private Connection myConn;
	
	public AirplaneADO() throws Exception {
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
			myRs = myStmt.executeQuery("select * from самолет");
			
			while (myRs.next()) {
				Airplane tempPlane = convertRowToPlane(myRs);
				list.add(tempPlane);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	};
	
	
	private Airplane convertRowToPlane(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();
		
		val.add(myRs.getInt("номер самолета"));
		val.add(myRs.getString("модель"));
		val.add(myRs.getInt("дальность полета"));
		val.add(myRs.getInt("грузоподъемность"));
	
		return new Airplane(val);
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		try { 
			if (myStmt != null) myStmt.close();
			if (myRs != null) myRs.close();
		} catch (Exception e) {};
		
	}

	@Override
	public DataClass getEmpty() {
		return new Airplane();
	}

	@Override
	public void update(DataClass tempPlane) throws SQLException{
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `самолет`" 
					+ " set `модель`=?, `дальность полета`=?, `грузоподъемность`=?"
					+ " where `номер самолета`=?");
			myStmt.setString(1, tempPlane.getData(1).toString());	
			myStmt.setInt(2, Integer.parseInt(tempPlane.getData(2).toString()));
			myStmt.setInt(3, Integer.parseInt(tempPlane.getData(3).toString()));
			myStmt.setInt(4, tempPlane.getId());
			
			myStmt.executeUpdate();
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			myStmt.close();
		}
		
	}

	@Override
	public void add(DataClass tempPlane) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into `самолет`" 
					+ "values (NULL, ?, ?, ?)");
			myStmt.setString(1, tempPlane.getData(1).toString());	
			myStmt.setInt(2, Integer.parseInt(tempPlane.getData(2).toString()));
			myStmt.setInt(3, Integer.parseInt(tempPlane.getData(3).toString()));
			
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
			myStmt = myConn.prepareStatement("delete from `самолет` where `номер самолета`=?");
			
			myStmt.setInt(1, id);
			
			myStmt.executeUpdate();
		}
		finally {
			myStmt.close();
		}
		
	}

}