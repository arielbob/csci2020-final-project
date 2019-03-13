package net.packet;

import net.user.UserState;

import java.net.DatagramPacket;
import java.util.UUID;

public class UpdateUserStatePacket extends Packet {
	private UUID id;
	private UserState userState;

	public UpdateUserStatePacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		this.id = UUID.fromString(data[1].trim());
		this.userState = UserState.valueOf(data[2].trim());
	}

	public UpdateUserStatePacket(UUID id, UserState userState) {
		this.id = id;
		this.userState = userState;
	}

	public UUID getId() {
		return id;
	}

	public UserState getUserState() {
		return userState;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {id.toString(), userState.toString()};
		return createByteArray(PacketType.UPDATE_USER_STATE, entries);
	}
}
