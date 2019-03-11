package net.packet;

public enum PacketType {
	INVALID(-0x1), MESSAGE(0x0);

	private int id;

	PacketType(int id) {
		this.id = id;
	}

	public static PacketType lookupPacket(int id) {
		for (PacketType pt : PacketType.values()) {
			if (pt.id == id) {
				return pt;
			}
		}
		return PacketType.INVALID;
	}
}
