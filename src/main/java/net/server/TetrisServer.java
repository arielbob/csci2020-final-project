package net.server;

import net.Callback;
import net.packet.IDPacket;
import net.user.User;
import net.user.UserPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class TetrisServer extends Server {
	private UserPool userPool;
	private Callback<DatagramPacket> receiveHandler;

	public TetrisServer(int port, Callback<DatagramPacket> receiveHandler) throws SocketException {
		super(port);
		this.receiveHandler = receiveHandler;
		this.userPool = new UserPool();
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		receiveHandler.execute(packet);
		User createdUser = userPool.addUser(packet.getAddress(), packet.getPort(), "user");
		if (createdUser != null) {
			IDPacket idPacket = new IDPacket(createdUser.getId());
			sendPacket(idPacket, createdUser);
		}
		sendData(packet.getData(), userPool.getUsers());
	}
}
