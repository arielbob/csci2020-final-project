package net.user;

import java.net.InetAddress;
import java.util.UUID;

public class User {
	private InetAddress address;
	private int port;
	private String username;
	private UUID id; // we have this so we can send the data of one person to another without sharing IPs, but still having some identifier

	private int[][] board;
	private UserState state;

	public User(InetAddress address, int port, String username) {
		this.address = address;
		this.port = port;
		this.username = username;
		this.id = UUID.randomUUID();
		this.state = UserState.SPECTATING;
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public UUID getId() {
		return id;
	}
}
