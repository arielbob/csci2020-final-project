package net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

public class IDPacket extends Packet {
	private UUID id;

	public IDPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		this.id = UUID.fromString(data[1].trim());
	}

	public IDPacket(UUID id) {
		this.id = id;
	}

	public UUID getID() {
		return id;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {id.toString()};
		return createByteArray(PacketType.ID, entries);
	}
}
