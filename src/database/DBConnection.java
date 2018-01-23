package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 数据库连接词
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class DBConnection {
	static private BasicDataSource ds = null;
	
	private DBConnection() {}
	
	public static void main(String[] args) throws SQLException {
		
/*		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT `name`,`id` FROM `synonyms`");
		while (rs.next()) {
			System.out.println(rs.getString("name"));
			System.out.println(rs.getString("id"));
		}
		rs.close();
		stmt.close();
		conn.close();*/
		System.out.println(getPage("http://mil.huanqiu.com/china/2017-09/11213701.html"));
		
	}
	
	
	public static Connection getConnection(String  dataBase) {
		Connection conn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dataBase,"root","123456");
	    } catch (Exception e) {
	    	System.out.println("数据库连接失败");
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    //返回Connection对象  
	    return conn;
	}
	
	/**
	 * 根据url返回页面内容
	 * @param url
	 * @return
	 */
	public static String getPage(String url){
		String page="";
		Connection conn = getConnection("entity");
		try{
			Statement stmt = conn.createStatement();
			String sql = "select content from page where url='"+url+"'";
			System.out.println("page======"+sql);
			ResultSet rs  = stmt.executeQuery(sql);//
			if(rs.next()){
				page = rs.getString("content");
				System.out.println("page======"+page);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return page;
	}
	
	
	/**
	 * 获取连接
	 */
	public static Connection getConnection() throws SQLException {
		if ( ds == null ) {
			ds = new BasicDataSource();
			try {
				Properties prop = new Properties();
				prop.load(DBConnection.class.getClassLoader().
							getResource("conf.properties").openStream());
				ds.setMaxIdle(Integer.parseInt(prop.getProperty("maxIdle")));
				ds.setMaxActive(Integer.parseInt(prop.getProperty("maxActive")));
				ds.setUsername(prop.getProperty("username"));
				ds.setPassword(prop.getProperty("password"));
				ds.setUrl(prop.getProperty("url"));
				ds.setDriverClassName(prop.getProperty("driverClassName"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ds.getConnection();
	}
}
