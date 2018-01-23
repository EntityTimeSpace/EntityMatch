/**
 * 
 */
package classTree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;

/**
 * @author "Cunxin Jia"
 *
 */
public class UserDictInit {
	private static List<String[]> nameTypes;
	
	private static void addNewData() {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `dict`(`name`, `type`) " +
					"values(?, ?)");
			for (String[] nameType : nameTypes) {
				System.out.println(nameType[0] + "\t" + nameType[1]);
				pstmt.setString(1, nameType[0]);
				pstmt.setString(2, nameType[1]);
				pstmt.executeUpdate();
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void getData() {
		nameTypes = new ArrayList<String[]> ();
		String drv = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://114.212.85.9:3306/nju28_3";
		try {
			Class.forName(drv).newInstance();
			String infoSchema = "information_schema";
			Connection conn = DriverManager.getConnection(url, "chlni", "chlni");
			String sql = "select mc,lx from zb";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String name = rs.getString(1);
				int typeInt = rs.getInt(2);
				String type = "http://ws.nju.edu.cn/nju28/";
				switch(typeInt) {
				case 1:type += "hkmj";break;
				case 2:type += "dd";break;
				case 3:type += "dlzz";break;
				case 4:type += "jp";break;
				case 5:type += "jzj";break;
				case 6:type += "ld";break;
				}
				String[] nameType = {name, type};
				nameTypes.add(nameType);
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		getData();
		addNewData();
	}
}
