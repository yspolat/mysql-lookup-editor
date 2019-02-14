package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbFunc {
	private String server;
	private String database;
	private String userName;
	private String password;
	
	
	public DbFunc() {
	}

	public DbFunc(String server, String database, String userName, String password) {
		this.server = server;
		this.database = database;
		this.userName = userName;
		this.password = password;
	}
	
	public Connection getConnection() {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", getUserName(), getPassword());		
			return conn;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
				
	public Connection getConnectionBySchema() {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + getServer() + "/" + getDatabase(), getUserName(), getPassword());		
			return conn;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
