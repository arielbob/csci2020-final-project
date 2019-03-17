package net.packet;

import java.net.DatagramPacket;

import java.util.UUID;

public class BoardPacket extends Packet {
	private int[][] board;
	private final int BOARD_WIDTH = 10;
	private final int BOARD_HEIGHT = 24;

	private UUID id;

	public BoardPacket(DatagramPacket packet) {
		board = new int[BOARD_HEIGHT][BOARD_WIDTH];
		String[] data = getDataEntries(packet);
		if (data[1].length() > 0) {
			this.id = UUID.fromString(data[1]);
		}
		String[] boardData = data[2].split(",");
		int i = 0;
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				//board[row][col] = Character.getNumericValue(boardData.charAt(row * BOARD_WIDTH + col));
				board[row][col] = Integer.valueOf(boardData[i]);
				i++;
			}
		}
	}

	public BoardPacket(int[][] board) {
		this.board = board;
	}

	public BoardPacket(UUID id, int[][] board) {
		this.id = id;
		this.board = board;
	}

	public UUID getId() {
		return id;
	}

	public int[][] getBoard() {
		return board;
	}

	@Override
	public byte[] getBytes() {
		String boardData = "";
		StringBuilder sb = new StringBuilder(boardData);
		for (int[] ints : board) {
			for (int j = 0; j < board[0].length; j++) {
				sb.append(ints[j] + ",");
				//System.out.print(ints[j] + ",");
			}
			//System.out.println();
		}

		String[] entries = {id.toString(), sb.toString()};
		return createByteArray(PacketType.BOARD, entries);
	}
}
