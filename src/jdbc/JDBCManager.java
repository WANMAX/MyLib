/**
 * 
 */
package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 对数据库操做的工具类
 *
 */
public final class JDBCManager {
	/**数据库的url，默认值为本机的数据库url，可以在配置文件中改*/
	private static String url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=weibo";
	/**登录数据库的用户名，默认值为weibo，可以在配置文件中改*/
	private static String user = "weibo";
	/**登录数据库的密码，默认值为gdufs，可以在配置文件中改*/
	private static String password = "gdufs";
	/**驱动名称*,默认为sqlServer，可以在配置文件中改*/
	private static String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static {
		try {
			Properties prop = new Properties();
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			if(prop.containsKey("dataBaseUrl"))
				url = prop.getProperty("dataBaseUrl");
			if(prop.containsKey("dataBaseU"))
				user = prop.getProperty("dataBaseUser");
			if(prop.containsKey("dataBasePassword"))
				password = prop.getProperty("dataBasePassword");
			if(prop.containsKey("JDriver"))
				JDriver = prop.getProperty("JDriver");
			Class.forName(JDriver);
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError("ERROR：JDBCTool初始化失败！");
		} catch (IOException e) {
			System.err.println("ERROR:config.properties丢失或无权限读取，将使用类内定义值！");
			e.printStackTrace();
		}
	}
	 
	private JDBCManager(){};

	/**
	 * 连接到数据库
	 * @return 返回与数据库的连接
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * 释放连接的方法
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			System.err.println("ERROR:数据库无法释放连接！");
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.err.println("ERROR:数据库无法释放连接！");
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						System.err.println("ERROR:数据库无法释放连接！");
						e.printStackTrace();
					}
			}
		}
	}
}