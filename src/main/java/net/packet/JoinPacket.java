package net.packet;

import java.io.ByteArrayOutputStream;

public class JoinPacket extends Packet {
	@Override
	public byte[] getBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(PacketType.JOIN.id);
		return output.toByteArray();
	}
}
