package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

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

	// TODO: move all tetris specific methods to a Client subclass, instead of in Client itself
	public void sendMessage(String message) throws IOException {
		// TODO: have different packet objects that have their own getBytes methods
		byte[] data = message.getBytes(StandardCharsets.US_ASCII);
		sendData(data);
	}

	public void stopClient() {
		socket.close();
		isRunning = false;
	}
}