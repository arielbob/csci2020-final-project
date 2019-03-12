package net.user;

import java.net.InetAddress;
import java.util.HashMap;

public class UserPool {
	// TODO: we might want to have a HashMap mapping user IDs to their ip:port key in users
	private HashMap<String, User> users;

	public UserPool() {
		users = new HashMap<String, User>();
	}

	// returns the new user object if a user is created, else null
	public User addUser(InetAddress ip, int port, String username) {
		String key = ip.toString() + ":" + Integer.toString(port);
		User user = new User(ip, port, username);

		if (!users.containsKey(key)) {
			users.put(key, new User(ip, port, username));
			System.out.println(users.toString());
			return user;
		}

		return null;
	}

	public HashMap<String, User> getUsers() {
		return users;
	}
}
