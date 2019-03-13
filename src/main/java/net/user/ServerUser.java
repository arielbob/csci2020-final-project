package net.user;

import java.net.InetAddress;

// User subclass for storing user information on the server
// has fields like address and port which are not necessary in the client
public class ServerUser extends User {
	private InetAddress address;
	private int port;

	public ServerUser(InetAddress address, int port, String username) {
		super(username);
		this.address = address;
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
}
