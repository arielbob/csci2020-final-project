package net.user;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.UUID;

public class UserPool {
	// TODO: we might want to have a HashMap mapping user IDs to their ip:port key in users
	private HashMap<String, User> users;

	public UserPool() {
		users = new HashMap<String, User>();
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public User findUserById(UUID id) {
		return users.get(id.toString());
	}

	public User addUser(User user) {
		users.put(user.getId().toString(), user);
		return user;
	}

	public User addUser(UUID id, String username) {
		User user = new User(id, username);

		users.put(user.getId().toString(), user);
		return user;
	}
}
