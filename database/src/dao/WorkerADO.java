package dao;

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

import core.DataClass;
import core.Worker;

public class WorkerADO implements ADO {
	
	
	private Connection myConn;
	
	public WorkerADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	private Worker convertRow(ResultSet myRs) throws SQLException {
		
		List<Object> val = new ArrayList<Object>();

		val.add(myRs.getInt("номер сотрудника"));
		val.add(myRs.getString("должность"));
		val.add(myRs.getString("ФИО"));
		val.add(myRs.getInt("зарплата"));
		val.add(myRs.getInt("номер самолета"));
		val.add(myRs.getInt("номер медкомиссии"));
		val.add(myRs.getInt("номер проверки"));
	
		return new Worker(val);
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
			myRs = myStmt.executeQuery("select * from сотрудник");
			
			while (myRs.next()) {
				Worker tempWorker = convertRow(myRs);
				list.add(tempWorker);
			}
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	}

	@Override
	public DataClass getEmpty() {
		return new Worker();
	}

	@Override
	public void update(DataClass tempWorker) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("update `сотрудник`" 
					+ " set `должность`=?, `ФИО`=?, `зарплата`=?, `номер самолета`=? , `номер медкомиссии`=?, `номер проверки`=?"
					+ " where `номер сотрудника`=?");	
			myStmt.setString(1, tempWorker.getData(2).toString());
			myStmt.setString(2, tempWorker.getData(3).toString());
			myStmt.setInt(3, Integer.parseInt(tempWorker.getData(3).toString()));
			myStmt.setInt(4, Integer.parseInt(tempWorker.getData(4).toString()));
			myStmt.setInt(5, (Integer) tempWorker.getData(5));
			myStmt.setInt(6, (Integer) tempWorker.getData(6));
			myStmt.setInt(7, tempWorker.getId());
			
			myStmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			myStmt.close();
		}
	}

	@Override
	public void add(DataClass tempWorker) throws SQLException {
		PreparedStatement myStmt = null;
		
		try {
			myStmt = myConn.prepareStatement("insert into сотрудник " + "values (NULL, ?, ?, ?, ?, ?, ?)");
			myStmt.setString(1, tempWorker.getData(2).toString());
			myStmt.setString(2, tempWorker.getData(3).toString());
			myStmt.setInt(3, Integer.parseInt(tempWorker.getData(3).toString()));
			myStmt.setInt(4, Integer.parseInt(tempWorker.getData(4).toString()));
			myStmt.setInt(5, (Integer) tempWorker.getData(5));
			myStmt.setInt(6, (Integer) tempWorker.getData(6));
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
			myStmt = myConn.prepareStatement("delete from `сотрудник` where `номер сотрудника`=?");
			myStmt.setInt(1, id);
			myStmt.executeUpdate();
		}
		finally {
			myStmt.close();
		}
	}

}
