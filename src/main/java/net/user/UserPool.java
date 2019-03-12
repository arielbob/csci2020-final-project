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

	public User findUserByIp(InetAddress ip, int port) {
		return users.get(createKeyString(ip, port));
	}

	public User addUser(InetAddress ip, int port, String username) {
		String key = createKeyString(ip, port);
		User user = new User(ip, port, username);

		users.put(key, new User(ip, port, username));
		System.out.println(users.toString());
		return user;
	}

	private String createKeyString(InetAddress ip, int port) {
		return ip.toString() + ":" + Integer.toString(port);
	}
}
