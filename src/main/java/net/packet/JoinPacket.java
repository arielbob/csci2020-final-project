package net.packet;

import java.io.ByteArrayOutputStream;

public class JoinPacket extends Packet {
	@Override
	public byte[] getBytes() {
		return createByteArray(PacketType.JOIN, null);
	}
}
