package net.client;

import net.Callback;
import net.packet.MessagePacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class TetrisClient extends Client {
	private Callback<DatagramPacket> receiveHandler;
	private UUID id;

	// receiveHandler is a callback that allows the owner class to access packets when they come in
	// it should be replaced by a controller object that can modify the view so that packet
	// parsing can be hidden inside TetrisClient rather than having parsing being coupled with a
	// view object
	public TetrisClient(InetAddress address, int port, Callback<DatagramPacket> receiveHandler) throws SocketException {
		super(address, port);
		this.receiveHandler = receiveHandler;
	}

	public void sendMessage(String message) throws IOException {
		// TODO: add packet that gives clients their UUID when they join
		MessagePacket packet = new MessagePacket(UUID.randomUUID(), message);
		sendPacket(packet);
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		receiveHandler.execute(packet);
	}
}
