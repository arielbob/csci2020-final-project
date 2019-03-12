package net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

public class IDPacket extends Packet {
	private UUID id;

	private IDPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		this.id = UUID.fromString(data[1]);
	}

	private IDPacket(UUID id) {
		this.id = id;
	}

	@Override
	public byte[] getBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(PacketType.ID.id);
		output.write(0x1F);
		try {
			output.write(id.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toByteArray();
	}
}
