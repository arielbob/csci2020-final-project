package net.server;

import net.packet.AddPlayerPacket;
import net.packet.IDPacket;
import net.packet.MessagePacket;
import net.packet.PacketType;
import net.test.ServerTest;
import net.user.User;
import net.user.UserPool;
import net.user.UserState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class TetrisServer extends Server {
	private UserPool userPool;
	private ServerTest view;
	private ServerState state;

	public TetrisServer(int port) throws SocketException {
		super(port);
		this.userPool = new UserPool();
		this.state = ServerState.WAITING;
	}

	public void setView(ServerTest view) {
		this.view = view;
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		view.appendText("[PACKET DATA]: " + new String(packet.getData(), StandardCharsets.US_ASCII) + '\n');

		InetAddress packetIp = packet.getAddress();
		int packetPort = packet.getPort();
		User user = userPool.findUserByIp(packetIp, packetPort);

		PacketType type = PacketType.lookupPacket(packet);

		if (type == PacketType.CONNECT) {
			if (user == null) {
				user = userPool.addUser(packetIp, packetPort, "user");
				IDPacket idPacket = new IDPacket(user.getId());
				sendPacket(idPacket, user);
			}
		}

		if (user == null) return;

		switch (type) {
			case JOIN:
				if (state == ServerState.WAITING) {
					user.setState(UserState.JOINED);
					AddPlayerPacket addPlayerPacket = new AddPlayerPacket(user.getId(), user.getUsername());
					sendPacket(addPlayerPacket, userPool.getUsers());
				}
				break;
			case MESSAGE:
				MessagePacket messagePacket = new MessagePacket(packet);
				messagePacket.setId(user.getId());
				sendPacket(messagePacket, userPool.getUsers());
				break;
		}
	}
}
