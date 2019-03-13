package net.user;

import java.net.InetAddress;
import java.util.HashMap;

public class ServerUserPool {
	// TODO: we might want to have a HashMap mapping user IDs to their ip:port key in users
	private HashMap<String, ServerUser> users;

	public ServerUserPool() {
		users = new HashMap<String, ServerUser>();
	}

	public HashMap<String, ServerUser> getUsers() {
		return users;
	}

	public ServerUser findUserByIp(InetAddress ip, int port) {
		return users.get(createKeyString(ip, port));
	}

	public ServerUser addUser(InetAddress ip, int port, String username) {
		String key = createKeyString(ip, port);
		ServerUser user = new ServerUser(ip, port, username);

		users.put(key, user);
		System.out.println(users.toString());
		return user;
	}

	private String createKeyString(InetAddress ip, int port) {
		return ip.toString() + ":" + Integer.toString(port);
	}
}
