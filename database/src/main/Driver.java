package main;
import java.sql.*;

public class Driver {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5_scheme", "root", "1234");
			
			Statement myStmt = myConn.createStatement();
			
			ResultSet myRs = myStmt.executeQuery("select * from рейс");
			
			while (myRs.next()) {
				System.out.println(myRs.getString("Номер рейса") + ", " + myRs.getString("Пункт назначения"));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
