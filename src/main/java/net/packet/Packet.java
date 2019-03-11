package net.packet;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public abstract class Packet {
	public abstract byte[] getBytes();

	public static String getDataString(DatagramPacket packet) {
		return new String(packet.getData(), StandardCharsets.US_ASCII);
	}
}
