package net.packet;

import java.net.DatagramPacket;

public enum PacketType {
	INVALID(-0x01),				// client <-> server: invalid packet type
	ID(0x00),					// server -> client: to send the user their id on connection
	CONNECT(0x01),				// client -> server: to connect to server and get added to user pool
	JOIN(0x02),					// client -> server: for joining a game
	MESSAGE(0x03),				// client <-> server: to send a message
	ADD_PLAYER(0x04),			// server -> client: to add a player to the game
	UPDATE_CLIENT_STATE(0x05),	// server -> client: to update client state
	UPDATE_USER_STATE(0x06),		// server -> client: to update user state
	BOARD(0x07),					// server -> client: to update a user's board
	QUIT(0x08),
	SERVER_CLOSE(0x09);

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
