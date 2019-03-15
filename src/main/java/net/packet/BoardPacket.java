package net.packet;

import java.net.DatagramPacket;

public class BoardPacket extends Packet {
	private int[][] board;
	private final int BOARD_WIDTH = 10;
	private final int BOARD_HEIGHT = 24;

	public BoardPacket(DatagramPacket packet) {
		board = new int[BOARD_HEIGHT][BOARD_WIDTH];
		String[] data = getDataEntries(packet);
		String boardData = data[1];
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				board[row][col] = boardData.charAt(row * BOARD_WIDTH + col);
			}
		}
	}

	public BoardPacket(int[][] board) {
		this.board = board;
	}

	@Override
	public byte[] getBytes() {
		String boardData = "";
		StringBuilder sb = new StringBuilder(boardData);
		for (int[] ints : board) {
			for (int j = 0; j < board[0].length; j++) {
				sb.append(ints[j]);
			}
		}

		String[] entries = {sb.toString()};
		return createByteArray(PacketType.BOARD, entries);
	}
}
