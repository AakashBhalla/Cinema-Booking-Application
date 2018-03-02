package application;

public class User {
	public String userName;
	public String ID;
	public String role;

	/**
	 * User constructor that is used for employees.
	 * 
	 * @author Aakash
	 * @param userName
	 *            the name
	 * @param role
	 *            the role
	 */
	public User(String userName, String role) {
		this.userName = userName;
		this.role = role;
	}

	/**
	 * User constructor that is used for employees.
	 * 
	 * @author Aakash
	 * @param ID
	 *            the ID
	 * @param userName
	 *            the name
	 * @param role
	 *            the role
	 */
	public User(String ID, String userName, String role) {
		this.ID = ID;
		this.userName = userName;
		this.role = role;
	}

	/**
	 * Get user name.
	 * 
	 * @author Aakash
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set user name.
	 * 
	 * @author Aakash
	 * @param userName
	 *            user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get user ID.
	 * 
	 * @author Aakash
	 * @return user ID
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Set user ID.
	 * 
	 * @author Aakash
	 * @param iD
	 *            user ID
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * Get user role.
	 * 
	 * @author Aakash
	 * @return user role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Set user role.
	 * 
	 * @author Aakash
	 * @param role
	 *            user role
	 */
	public void setRole(String role) {
		this.role = role;
	}
}
