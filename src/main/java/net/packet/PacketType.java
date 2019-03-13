package net.packet;

import java.net.DatagramPacket;

public enum PacketType {
	INVALID(-0x01),			// client <-> server: invalid packet type
	ID(0x00),				// server -> client: to send the user their id on connection
	CONNECT(0x01),			// client -> server: to connect to server and get added to user pool
	JOIN(0x02),				// client -> server: for joining a game
	MESSAGE(0x03),			// client <-> server: to send a message
	ADD_PLAYER(0x04),		// server -> client: to add a player to the game
	UPDATE_USER_STATE(0x05);// server -> client: to update user state

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
