package net.packet;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public abstract class Packet {
	public abstract byte[] getBytes();

	// returns an array of the data entries in the packet
	public static String[] getDataEntries(DatagramPacket packet) {
		// converts data to string then splits on ASCII character 1F (data separator character)
		return new String(packet.getData(), StandardCharsets.US_ASCII).split("\\x1F");
	}
}
