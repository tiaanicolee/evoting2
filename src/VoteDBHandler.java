import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class VoteDBHandler {
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
	private final String tableName = "VOTES";
	
	public VoteDBHandler() 
	{

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
			        "TITLE varchar(40) NOT NULL, " +
			        "NAME varchar(40) NOT NULL, " +
			        "PARTY varchar(40) NOT NULL, " +
			        "NUMVOTES INTEGER NOT NULL, " +
			        "PRIMARY KEY (NAME))";
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
		//drop voting table
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
		    String dropString = "DROP TABLE " + this.tableName;
			this.executeUpdate(conn, dropString);
			System.out.println("Dropped the voting table");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not drop the table");
			e.printStackTrace();
		}
	}
	
	public void insertCandidates()
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
			conn = this.getConnection();
			String sql = "INSERT INTO " + this.tableName + " VALUES ('President', 'Squirtle', 'Democratic Party', 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('President', 'Charmander', 'Republican Party', 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('President', 'Pikachu', 'Libertarian Party', 0)";
			this.executeUpdate(conn, sql);
			sql = "INSERT INTO " + this.tableName + " VALUES ('President', 'Bulbasaur', 'Green Party', 0)";
			this.executeUpdate(conn, sql);
			
		}
		catch(SQLException e) {
			System.out.println("ERROR: Could not insert into the table");
			e.printStackTrace();
			return;
		}
	}
	
	public ArrayList<Candidate> getCandidates()
	{
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM '" + this.tableName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  String name = rs.getString("name");
				  String party = rs.getString("party");
				  String title = rs.getString("title");
				  candidates.add(new Candidate(name, party, title));
			}   
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public String getTally()
	{
		Connection conn = null;
		String output = "";
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `" + this.tableName + "`");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  String name = rs.getString("name");
				  output = output + name + ": " + rs.getInt("numvotes") + " ";
			}   
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return output;
		}
		return output;
	}
	
	public int getTotalNumVotes()
	{
		Connection conn = null;
		int numVotes = 0;
		try {
			conn = this.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `votes`");
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
	
	public void saveVote(String candName)
	{
		int numVotes = 0;
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `votes` WHERE NAME = ?");
			stmt.setString(1, candName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				  numVotes = rs.getInt("NUMVOTES");
			}

		    stmt = conn.prepareStatement("update votes set NUMVOTES = ? WHERE NAME = ?");
			stmt.setInt(1, numVotes + 1);
			stmt.setString(2, candName);
			stmt.executeUpdate();
	        
		} catch (SQLException e) {
			System.out.println("ERROR: Could not update database");
			e.printStackTrace();
		}
	}
	
	public void getRecount()
	{
		
	}
}
