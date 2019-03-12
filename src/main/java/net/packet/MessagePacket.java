package net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

public class MessagePacket extends Packet {
	private UUID id;
	private String message;

	public MessagePacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		if (data[1].length() > 0) this.id = UUID.fromString(data[1]);
		this.message = data[2];
	}

	public MessagePacket(UUID id, String message) {
		this.id = id;
		this.message = message;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {id.toString(), message};
		return createByteArray(PacketType.MESSAGE, entries);
	}
}
