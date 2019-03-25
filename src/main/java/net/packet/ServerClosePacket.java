package net.packet;

public class ServerClosePacket extends Packet {
	@Override
	public byte[] getBytes() {
		return createByteArray(PacketType.SERVER_CLOSE, null);
	}
}
