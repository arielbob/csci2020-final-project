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

	@Override
	public byte[] getBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(PacketType.ADD_PLAYER.id);
		output.write(0x1F);
		try {
			if (id != null) output.write(id.toString().getBytes());
			output.write(0x1F);
			output.write(username.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toByteArray();
	}
}
