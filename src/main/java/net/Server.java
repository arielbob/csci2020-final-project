package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Thread {
	private DatagramSocket socket;
	private Callback<byte[]> onReceive;
	private volatile boolean isRunning;

	public Server(int port, Callback<byte[]> onReceive) throws SocketException {
		this.socket = new DatagramSocket(port);
		this.onReceive = onReceive;
	}

	public void run() {
		isRunning = true;
		while (isRunning) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
				byte[] data = packet.getData();
				onReceive.execute(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopServer() {
		socket.close();
		isRunning = false;
	}
}