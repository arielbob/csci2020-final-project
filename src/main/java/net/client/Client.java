package net.client;

import net.packet.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class Client extends Thread {
	private InetAddress address;
	private int port;
	private DatagramSocket socket;
	private volatile boolean isRunning;

	public Client(InetAddress address, int port) throws SocketException {
		this.address = address;
		this.port = port;
		this.socket = new DatagramSocket();
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

	private void sendData(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
	}

	void sendPacket(Packet packet) throws IOException{
		sendData(packet.getBytes());
	}

	public void stopClient() {
		System.out.println("closing client socket");
		socket.close();
		isRunning = false;
	}
}
