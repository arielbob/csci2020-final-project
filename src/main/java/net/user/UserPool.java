package net.user;

import java.net.InetAddress;
import java.util.HashMap;

public class UserPool {
	// TODO: we might want to have a HashMap mapping user IDs to their ip:port key in users
	private HashMap<String, User> users;

	public UserPool() {
		users = new HashMap<String, User>();
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public User findUserById(String id) {
		return users.get(id);
	}

	public User addUser(String id, String username) {
		User user = new User(username);

		users.put(user.getId().toString(), user);
		return user;
	}
}
