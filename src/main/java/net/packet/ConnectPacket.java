package net.packet;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.util.UUID;

public class ConnectPacket extends Packet {
	private String username;

	public ConnectPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		this.username = data[1];
	}

	public ConnectPacket(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {username};
		return createByteArray(PacketType.CONNECT, entries);
	}
}
