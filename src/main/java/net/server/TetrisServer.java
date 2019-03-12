package net.server;

import net.packet.IDPacket;
import net.test.ServerTest;
import net.user.User;
import net.user.UserPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class TetrisServer extends Server {
	private UserPool userPool;
	private ServerTest view;

	public TetrisServer(int port) throws SocketException {
		super(port);
		this.userPool = new UserPool();
	}

	public void setView(ServerTest view) {
		this.view = view;
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		view.appendText("[PACKET DATA]: " + new String(packet.getData(), StandardCharsets.US_ASCII) + '\n');

		User createdUser = userPool.addUser(packet.getAddress(), packet.getPort(), "user");
		if (createdUser != null) {
			IDPacket idPacket = new IDPacket(createdUser.getId());
			sendPacket(idPacket, createdUser);
		}
		sendData(packet.getData(), userPool.getUsers());
	}
}
