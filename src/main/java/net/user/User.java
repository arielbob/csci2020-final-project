package net.user;

import java.util.UUID;

public class User {
	private String username;
	private UUID id; // we have this so we can send the data of one person to another without sharing IPs, but still having some identifier

	private int[][] board;
	private UserState state;

	public User(String username) {
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

	public String getUsername() {
		return username;
	}

	public UUID getId() {
		return id;
	}
}
