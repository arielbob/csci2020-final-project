package net;

import net.packet.IDPacket;
import net.packet.Packet;
import net.user.User;
import net.user.UserPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class Server extends Thread {
	private DatagramSocket socket;
	private Callback<DatagramPacket> receiveHandler;
	private volatile boolean isRunning;
	private UserPool userPool;

	public Server(int port, Callback<DatagramPacket> receiveHandler) throws SocketException {
		this.socket = new DatagramSocket(port);
		this.receiveHandler = receiveHandler;
		this.userPool = new UserPool();
	}

	public void run() {
		isRunning = true;
		while (isRunning) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
				receiveHandler.execute(packet);
				User createdUser = userPool.addUser(packet.getAddress(), packet.getPort(), "user");
				if (createdUser != null) {
					IDPacket idPacket = new IDPacket(createdUser.getId());
					sendPacket(idPacket, createdUser);
				}
				sendData(packet.getData(), userPool.getUsers());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void sendPacket(Packet packet, User user) throws IOException{
		sendData(packet.getBytes(), user);
	}

	private void sendData(byte[] data, User user) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, user.getAddress(), user.getPort());
		socket.send(packet);
	}

	private void sendData(byte[] data, HashMap<String, User> users) throws IOException {
		for (User user : users.values()) {
			sendData(data, user);
		}
	}

	public void stopServer() {
		socket.close();
		isRunning = false;
	}
}