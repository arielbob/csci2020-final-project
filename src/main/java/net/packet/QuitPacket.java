package net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

public class QuitPacket extends Packet {
	private UUID id;
	private String username;

	public QuitPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		if (data[1].length() > 0) this.id = UUID.fromString(data[1]);
	}

	public QuitPacket(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {id.toString(), username};
		return createByteArray(PacketType.QUIT, entries);
	}
}
