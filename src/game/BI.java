package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Class that do BI on the examples of the game
 * @author Shayke Shok and Omer Edut
 *
 */
public class BI {

	/**
	 * This function do BI on the points of every example from the game
	 * The function calcute the average of every example and copmare the avg to my avg in evey example
	 */
	public static void doBI() {
		String jdbcUrl = "jdbc:mysql://ariel-oop.xyz:3306/oop";
		String jdbcUser = "student";
		String jdbcPassword = "student";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

			Statement statement = connection.createStatement();

			int[] arr = createArrOfFiles();
			int i = 1;
			double myavg = 0, allavg = 0;
			for (int filekod : arr) {
				String myAvgQuery = "SELECT AVG(Point) as avg FROM logs WHERE SomeDouble=" + filekod
						+ " and (FirstID=205835564 or SecondID=205835564 or ThirdID=205835564);";
				String allStudentsAvgQuery = "SELECT AVG(Point) as avg FROM logs WHERE SomeDouble=" + filekod + ";";

				ResultSet rsAllStudentsAvg = statement.executeQuery(allStudentsAvgQuery);
				System.out.println();
				System.out.println("Ex4_OOP_example" + i + ":");
				System.out.println();
				while (rsAllStudentsAvg.next()) {
					allavg = rsAllStudentsAvg.getDouble("avg");
					System.out.println("General Avg:" + rsAllStudentsAvg.getDouble("avg"));
				}
				rsAllStudentsAvg.close();
				ResultSet rsMyAvg = statement.executeQuery(myAvgQuery);
				while (rsMyAvg.next()) {
					myavg = rsMyAvg.getDouble("avg");
					System.out.println("My Avg:" + rsMyAvg.getDouble("avg"));
				}
				rsMyAvg.close();
				if (myavg > allavg)
					System.out.println("Im better player");
				else
					System.out.println("I need to improve my grade");
				i++;
			}

			
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

	/**
	 * This function create an array that every field in the array reprsent a hash code of the example name
	 * @return array reprsent a hash code of the example name
	 */
	public static int[] createArrOfFiles() {
		int[] arr = new int[9];
		arr[0] = 2128259830;
		arr[1] = 1149748017;
		arr[2] = -683317070;
		arr[3] = 1193961129;
		arr[4] = 1577914705;
		arr[5] = -1315066918;
		arr[6] = -1377331871;
		arr[7] = 306711633;
		arr[8] = 919248096;
		return arr;
	}

	public static void main(String[] args) {
		doBI();
	}
}
