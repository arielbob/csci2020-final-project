package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class Server extends Thread {
	private DatagramSocket socket;
	private Callback<DatagramPacket> onReceive;
	private volatile boolean isRunning;
	private UserPool userPool;

	public Server(int port, Callback<DatagramPacket> onReceive) throws SocketException {
		this.socket = new DatagramSocket(port);
		this.onReceive = onReceive;
		this.userPool = new UserPool();
	}

	public void run() {
		isRunning = true;
		while (isRunning) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
				// TODO: this callback can just be an abstract method
				// TODO: add packet types and parsing packets
				onReceive.execute(packet);
				userPool.addUser(packet.getAddress(), packet.getPort(), "user");
				sendData(packet.getData(), userPool.getUsers());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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