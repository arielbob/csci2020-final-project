package net.packet;

import java.net.DatagramPacket;

public enum PacketType {
	INVALID(-0x01), ID(0x00), CONNECT(0x01), MESSAGE(0x02);

	public int id;

	PacketType(int id) {
		this.id = id;
	}

	public static PacketType lookupPacket(DatagramPacket packet) {
		int packetId = packet.getData()[0];

		for (PacketType pt : PacketType.values()) {
			if (pt.id == packetId) {
				return pt;
			}
		}
		return PacketType.INVALID;
	}
}
