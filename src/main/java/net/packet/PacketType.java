package net.packet;

import java.net.DatagramPacket;

public enum PacketType {
	INVALID(-0x01),		// invalid packet type
	ID(0x00),			// for sending the user their id
	CONNECT(0x01),		// for connecting to server
	JOIN(0x02),			// for joining a game
	MESSAGE(0x03),		// for sending a message (used by server and client)
	ADD_PLAYER(0x04);	// server -> client: to add a player to the game

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
