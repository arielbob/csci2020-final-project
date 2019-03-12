package net.packet;

import java.io.ByteArrayOutputStream;

public class ConnectPacket extends Packet {
	@Override
	public byte[] getBytes() {
		return createByteArray(PacketType.CONNECT, null);
	}
}
