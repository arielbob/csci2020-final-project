package net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

public class AddPlayerPacket extends Packet {
	private UUID id;
	private String username;

	public AddPlayerPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		if (data[1].length() > 0) this.id = UUID.fromString(data[1]);
		this.username = data[2].trim();
	}

	public AddPlayerPacket(UUID id, String username) {
		this.id = id;
		this.username = username;
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {id.toString(), username};
		return createByteArray(PacketType.ADD_PLAYER, entries);
	}
}
