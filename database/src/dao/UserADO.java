package dao;

import java.sql.Statement;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jasypt.util.password.StrongPasswordEncryptor;

import core.User;

public class UserADO {
	
	private Connection myConn;
	
	public UserADO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("UserADO - Db connection succesful to: " + dburl);
	}
	
	 public List<User> getAllUsers() throws Exception {
		    List<User> list = new ArrayList<>();

		    Statement myStmt = null;
		    ResultSet myRs = null;

		    try {
		      myStmt = myConn.createStatement();
		      myRs = myStmt.executeQuery("select * from users");

		      while (myRs.next()) {
		        User tempUser = convertRowToUser(myRs);
		        list.add(tempUser);
		      }

		      return list;
		    } finally {
		    	close(myStmt, myRs);
		    }
		  }
	
	private User convertRowToUser(ResultSet myRs) throws SQLException {
		User user = new User();

		user.setId(myRs.getInt("user_id"));
		user.setLogin(myRs.getString("login"));
		user.setPassword(myRs.getString("password"));
		user.setIsAdmin(myRs.getBoolean("is_admin"));
		
		return user;
	}
	
	public User convertNameToUser(String login) throws SQLException {
	    
	    User theUser;

	    Statement myStmt = null;
	    ResultSet myRs = null;
	    try {
	      myStmt = myConn.createStatement();
	      myRs = myStmt.executeQuery("select * from users where login=\"" + 
	      login + "\""); //TODO: prevent from creating several equal users 
	      myRs.next();
	      theUser = convertRowToUser(myRs);
	      return theUser;
	    } finally {
	      close(myStmt, myRs);
	    }
	    
	  }
	
	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		try { 
			if (myStmt != null) myStmt.close();
			if (myRs != null) myRs.close();
		} catch (Exception e) {};
		
	}
	
	public boolean authenticate(User theUser, String plainTextPassword) throws Exception {
		boolean result = false;
		
		//String plainTextPassword = theUser.getPassword();
		
		String encryptedPasswordFromDatabase =  getEncryptedPassword(theUser.getId());
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		//System.out.println(passwordEncryptor.encryptPassword("1234"));
		
		result = passwordEncryptor.checkPassword(plainTextPassword, encryptedPasswordFromDatabase);
		
		return result;
		
	}

	private String getEncryptedPassword(int id) {
		String encryptedPassword = null;
		
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.prepareStatement("select `password` from `users` where `user_id` = ?");
			myStmt.setInt(1, id);
			myRs = myStmt.executeQuery();
			myRs.next();
			
			encryptedPassword = myRs.getString(1);
			
			return encryptedPassword;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
