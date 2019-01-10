package ex4_example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Stam {
	
	
	public static void BI(int id,String file){
		String jdbcUrl="jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		String jdbcUser="student";
		String jdbcPassword="student";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
			
			
			Statement statement = connection.createStatement();
			
			//select data
			int filekod=file.hashCode();
			String myAvgQuery="SELECT AVG(Point) as avg FROM logs WHERE SomeDouble="+filekod+" and FirstID="+id+";";
			String allStudentsAvgQuery = "SELECT AVG(Point) FROM logs WHERE SomeDouble="+filekod+";";
			String s="select * from logs where point<0;";
			//ResultSet rsMyAvg = statement.executeQuery(myAvgQuery);
			ResultSet rsAllStudentsAvg = statement.executeQuery(allStudentsAvgQuery);
			ResultSet rs = statement.executeQuery(s);
			System.out.println("FirstID\t\tSecondID\tThirdID\t\tLogTime\t\t\t\tPoint\t\tSomeDouble");
			while(rs.next())
			{
				System.out.println(rs.getInt("FirstID")+"\t\t" +
						rs.getInt("SecondID")+"\t\t" +
						rs.getInt("ThirdID")+"\t\t" +
						rs.getTimestamp("LogTime") +"\t\t\t\t" +
						rs.getDouble("Point") +"\t\t" +
						rs.getDouble("SomeDouble"));
			}
			//while (rsAllStudentsAvg.next()) {
			//	String name = rsAllStudentsAvg.getString(1);
			//	System.out.println(name);
		//	}
			
			//rsAllStudentsAvg.close();	
			rs.close();
			statement.close();		
			connection.close();		
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		BI(205835564,"data/Ex4_OOP_example8.csv");
	}
	
}
