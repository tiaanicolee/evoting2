import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Properties;

import javax.crypto.*;

public class UserDBHandler {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "root";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String dbName = "test";
	
	/** The name of the table we are testing with */
	private final String tableName = "REGISTERED_USERS";
	
	private final SecureRandom random;
	
	public UserDBHandler() {
		random = new SecureRandom();
	}
	
	
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
				connectionProps);

		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	public void createDB()
	{
		Connection conn = null;
		try { 
			conn = this.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
			
		} catch (SQLException e) {
			System.out.println("cannot connect to create database");
			//e.printStackTrace();
		}
	}
	
	public void createTable()
	{
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		// Create a table
		try {
		    String createString =
			        "CREATE TABLE " + this.tableName + " ( " +
			        "USERNAME varchar(40) NOT NULL, " +
			        "ROLE varchar(40) NOT NULL," +
			        "NUMVOTES INTEGER NOT NULL, " +
			        "ID INTEGER NOT NULL, " +
			        "LOGINATTEMPTS INTEGER NOT NULL, " +
			        "PRIMARY KEY (ID))";
			this.executeUpdate(conn, createString);
			System.out.println("Created a table");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
	}
	
	public void dropTable(){
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		//drop voting table
		try {
			System.out.println("Connected to database");
		    String dropString = "DROP TABLE " + this.tableName;
			this.executeUpdate(conn, dropString);
			System.out.println("Dropped the voting table");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not drop the table");
			e.printStackTrace();
		}
	}
	
	public void insertUsers()
	{
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		try {
			String sql;
			int i;
			for (i = 1000; i < 1097; i++)
			{
				sql = "INSERT INTO " + this.tableName + " VALUES ('" + getRandomUsername() +"', 'V', 0, " + i +", 0)";
				this.executeUpdate(conn, sql);
			}
			sql = "INSERT INTO " + this.tableName + " VALUES ('HFDGFHC', 'V', 0, " + i++ +", 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('RTYDHSAA', 'V', 0, " + i++ +", 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('sFGHsa', 'V', 0, " + i++ +", 0)";
			this.executeUpdate(conn, sql);
			
			sql = "INSERT INTO " + this.tableName + " VALUES ('Dr. X', 'EO', 0, " + i++ +", 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('CVBNS', 'EO', 0, " + i++ +", 0)";
			this.executeUpdate(conn, sql);
			
			
		}
		catch(SQLException e) {
			System.out.println("ERROR: Could not insert into the table");
			e.printStackTrace();
			return;
		}
	}
	
	public int findUser(String username, String role)
	{
		Connection conn = null;
		int ID = 0;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users` WHERE `username` = ? AND `role` = ?");
			stmt.setString(1, username);
			stmt.setString(2, role);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  ID = rs.getInt("ID");
			}
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return ID;
		}
		return ID;
	}
	
	public int correctLogin(String username, Integer id, String role)
	{
		Connection conn = null;
		int vID = -1;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users` WHERE `username` = ? AND `id` = ? AND `role` = ?");
			stmt.setString(1, username);
			stmt.setInt(2, id);
			stmt.setString(3,  role);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				  vID = rs.getInt("ID");
			}
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database or find user");
			e.printStackTrace();
			return -1;
		}
		return vID;
	}
	
	public int getUserNumVotes(int ID)
	{
		Connection conn = null;
		int numVotes = -1;
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users` WHERE ID = ?");
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  numVotes = rs.getInt("NUMVOTES");
			}
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database or find user");
			e.printStackTrace();
			return numVotes;
		}
		return numVotes;
	}
	
	public int getLoginAttempts(int ID)
	{
		Connection conn = null;
		int numAttempts = -1;
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users` WHERE ID = ?");
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  numAttempts = rs.getInt("LOGINATTEMPTS");
			}
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database or find user");
			e.printStackTrace();
			return numAttempts;
		}
		return numAttempts;
	}
	
	public void incrementLoginAttempts(int ID)
	{
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		try {
			int numVotes = -2 ;
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users` WHERE ID = ?");
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  numVotes = rs.getInt("NUMVOTES");
			}

		    stmt = conn.prepareStatement("update registered_users set LOGINATTEMPTS = ? WHERE ID = ?");
			stmt.setInt(1, numVotes + 1);
			stmt.setInt(2, ID);
			stmt.executeUpdate();
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not update database");
			e.printStackTrace();
		}
	}
	
	public void setVoteCount(int id) 
	{
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = "update registered_users set NUMVOTES = 1 WHERE id = " + "'"+ id + "'";
		    PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

	}
	
	public String getRandomUsername()
	{
		String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

		java.util.Random rand = new java.util.Random();
		StringBuilder builder = new StringBuilder();
	    while(builder.toString().length() == 0) {
	        int length = rand.nextInt(5)+5;
	        for(int i = 0; i < length; i++) {
	            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
	        }
	    }
	    return builder.toString();
	}
	
	public int getNumVotersWhoVoted()
	{
		Connection conn = null;
		int numVotes = 0;
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `registered_users`");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  numVotes = numVotes + rs.getInt("NUMVOTES");
			}
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database or find user");
			e.printStackTrace();
			return numVotes;
		}
		return numVotes;
	}
}
