package net;

import net.packet.MessagePacket;
import net.packet.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class Client extends Thread {
	private InetAddress address;
	private int port;
	private DatagramSocket socket;
	private Callback<DatagramPacket> receiveHandler;
	private volatile boolean isRunning;

	public Client(InetAddress address, int port, Callback<DatagramPacket> receiveHandler) throws SocketException {
		this.address = address;
		this.port = port;
		this.socket = new DatagramSocket();
		this.receiveHandler = receiveHandler;
	}

	public void run() {
		isRunning = true;
		while (isRunning) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
				receiveHandler.execute(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendData(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
	}

	private void sendPacket(Packet packet) throws IOException{
		sendData(packet.getBytes());
	}

	// TODO: move all tetris specific methods to a Client subclass, instead of in Client itself
	public void sendMessage(String message) throws IOException {
		// TODO: add packet that gives clients their UUID when they join
		MessagePacket packet = new MessagePacket(UUID.randomUUID(), message);
		sendPacket(packet);
	}

	public void stopClient() {
		socket.close();
		isRunning = false;
	}
}