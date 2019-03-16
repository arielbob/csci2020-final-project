package net.server;

import net.packet.IDPacket;
import net.packet.Packet;
import net.user.ServerUser;
import net.user.User;
import net.user.UserPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public abstract class Server extends Thread {
	private DatagramSocket socket;
	private volatile boolean isRunning;

	public Server(int port) throws SocketException {
		this.socket = new DatagramSocket(port);
	}

	public void run() {
		isRunning = true;
		while (isRunning) {
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
				receivePacket(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// method that handles receiving packets
	abstract void receivePacket(DatagramPacket packet) throws IOException;

	protected void sendPacket(Packet packet, ServerUser user) throws IOException{
		sendData(packet.getBytes(), user);
	}

	protected void sendPacket(Packet packet, HashMap<String, ServerUser> users) throws IOException {
		for (ServerUser user : users.values()) {
			sendPacket(packet, user);
		}
	}

	protected void sendData(byte[] data, ServerUser user) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, user.getAddress(), user.getPort());
		socket.send(packet);
	}

	protected void sendData(byte[] data, HashMap<String, ServerUser> users) throws IOException {
		for (ServerUser user : users.values()) {
			sendData(data, user);
		}
	}

	public void stopServer() {
		socket.close();
		isRunning = false;
	}
}
