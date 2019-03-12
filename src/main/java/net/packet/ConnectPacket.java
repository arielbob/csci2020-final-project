package net.packet;

import java.io.ByteArrayOutputStream;

public class ConnectPacket extends Packet {
	@Override
	public byte[] getBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(PacketType.CONNECT.id);
		return output.toByteArray();
	}
}
