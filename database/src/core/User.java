package core;


public class User {
	
	private int id;
	private String login;
	private String password;
	private boolean isAdmin;
	private static Integer loggedIn;
	
	
	public User() {
		super();
	}
	
	public User(int id, String login, String password, boolean isAdmin) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.isAdmin = isAdmin;
	
		
	}
	
	@Override
	public String toString() {
		return id + ", " + login + ", " + password + ", " + isAdmin; 
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void setLoggedIn(Integer loggedIn) {
		User.loggedIn = loggedIn;
	}

	public static Integer getLoggedIn() {
		return loggedIn;
	}

	

}


