package net.packet;

import net.client.ClientState;

import java.net.DatagramPacket;

public class UpdateClientStatePacket extends Packet {
	private ClientState clientState;

	public UpdateClientStatePacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
		this.clientState = ClientState.valueOf(data[1].trim());
	}

	public UpdateClientStatePacket(ClientState clientState) {
		this.clientState = clientState;
	}

	public ClientState getClientState() {
		return clientState;
	}

	@Override
	public byte[] getBytes() {
		String[] entries = {clientState.toString()};
		return createByteArray(PacketType.UPDATE_CLIENT_STATE, entries);
	}
}
