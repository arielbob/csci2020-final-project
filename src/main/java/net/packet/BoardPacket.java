package net.packet;

import java.net.DatagramPacket;

public class BoardPacket extends Packet {
	private int[][] board;

	public BoardPacket(DatagramPacket packet) {
		String[] data = getDataEntries(packet);
	}

	public BoardPacket(int[][] board) {
		this.board = board;
	}

	@Override
	public byte[] getBytes() {
		return createByteArray(PacketType.BOARD, null);
	}
}
