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

	protected static byte[] createByteArray(PacketType type, String[] entries) {
		char separator = 0x1F;
		String dataStr = "";
		StringBuilder sb = new StringBuilder(dataStr);
		sb.append((char) type.id);

		if (entries != null) {
			for (String e : entries) {
				sb.append(separator);
				sb.append(e);
			}
		}

		return sb.toString().getBytes();
	}
}
