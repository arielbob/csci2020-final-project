package net.client;

import net.Callback;
import net.packet.IDPacket;
import net.packet.MessagePacket;
import net.packet.PacketType;
import net.test.ClientTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class TetrisClient extends Client {
	private UUID id;
	private ClientTest view;

	public TetrisClient(InetAddress address, int port) throws SocketException {
		super(address, port);
		this.view = view;
	}

	public void setView(ClientTest view) {
		this.view = view;
	}

	public void sendMessage(String message) throws IOException {
		// TODO: add packet that gives clients their UUID when they join
		MessagePacket packet = new MessagePacket(UUID.randomUUID(), message);
		sendPacket(packet);
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		PacketType type = PacketType.lookupPacket(packet);

		switch (type) {
			case ID:
				IDPacket idPacket = new IDPacket(packet);
				this.id = idPacket.getID();
				break;
			case MESSAGE:
				MessagePacket messagePacket = new MessagePacket(packet);
				view.appendText(messagePacket.getId() + ": " + messagePacket.getMessage() + '\n');
				break;
		}
	}
}
