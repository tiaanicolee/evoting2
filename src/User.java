
public class User {
	private String username;
	private String password;
	private String role;
	
	public User(String uname, String pass, String rol)
	{
		username = uname;
		password = pass;
		role = rol;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getRole()
	{
		return role;
	}
}
