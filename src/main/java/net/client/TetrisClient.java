package net.client;

import net.Callback;
import net.packet.MessagePacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class TetrisClient extends Client {
	public TetrisClient(InetAddress address, int port, Callback<DatagramPacket> receiveHandler) throws SocketException {
		super(address, port, receiveHandler);
	}

	public void sendMessage(String message) throws IOException {
		// TODO: add packet that gives clients their UUID when they join
		MessagePacket packet = new MessagePacket(UUID.randomUUID(), message);
		sendPacket(packet);
	}
}
