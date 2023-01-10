package dao;

import core.Weight;
import core.DataClass;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ManagerWeightADO extends WeightADO {
	
	private int id;

	
	public ManagerWeightADO(int id) throws Exception {
		this.id = id;
		Properties props = new Properties();
		props.load(new FileInputStream("kurs.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	


	@Override
	public List<DataClass> getAll() throws Exception {
		List<DataClass> list = new ArrayList<>();
		
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.prepareStatement("select `груз`.`Номер груза`, `груз`.`Вес`, `груз`.`Состояние перевозки`, `груз`.`Дата доставки` from "
					+ "груз left outer join связь on (груз.`номер груза` = связь.`номер груза`) where связь.`номер сотрудника`=?");
			myStmt.setInt(1, id);
			//myRs = myStmt.executeQuery("select * from груз");
			myRs = myStmt.executeQuery();
			
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


//	@Override
//	public void add(DataClass tempClient) throws SQLException {
//		PreparedStatement myStmt = null;
//		
//		try {
//			myStmt = myConn.prepareStatement("insert into груз " + "values (NULL, ?, ?, ?)");
//			System.out.println(myStmt);
//			myStmt.setInt(1, Integer.parseInt(tempClient.getData(1).toString()));
//			myStmt.setString(2, (String) tempClient.getData(2));
//			myStmt.setDate(3, convertStringToDate(tempClient.getData(3).toString()));
//			System.out.println(myStmt);
//			myStmt.executeUpdate();
//		}
//		catch(Exception exc) {
//			System.out.println(exc.getMessage());
//			exc.getStackTrace();
//		}
//		finally {
//			
//				myStmt.close();
//			
//		}
//	}


	
}