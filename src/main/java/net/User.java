package net;

import java.net.InetAddress;
import java.util.UUID;

public class User {
	private InetAddress ip;
	private int port;
	private String username;
	private UUID id; // we have this so we can send the data of one person to another without sharing IPs, but still having some identifier

	public User(InetAddress ip, int port, String username) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.id = UUID.randomUUID();
	}
}
