package net.user;

import java.net.InetAddress;
import java.util.HashMap;

public class UserPool {
	private HashMap<String, User> users;

	public UserPool() {
		users = new HashMap<String, User>();
	}

	public void addUser(InetAddress ip, int port, String username) {
		String key = ip.toString() + ":" + Integer.toString(port);
		if (!users.containsKey(key)) {
			users.put(key, new User(ip, port, username));
			System.out.println(users.toString());
		}
	}

	public HashMap<String, User> getUsers() {
		return users;
	}
}
