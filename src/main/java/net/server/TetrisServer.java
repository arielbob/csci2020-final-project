package net.server;

import net.client.ClientState;
import net.packet.*;
//import net.test.ServerTest;
import net.user.ServerUser;
import net.user.User;
import net.user.ServerUserPool;
import net.user.UserState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import UserInterface.ClientView;

public class TetrisServer extends Server {
	private ServerUserPool userPool; // contains all connected users, they do not have to be joined in the game
	private ClientView view;
	private ServerState state;

	private Thread gameThread;

	public TetrisServer(int port, ClientView view) throws SocketException {
		super(port);
		this.userPool = new ServerUserPool();
		this.state = ServerState.WAITING;
		this.view = view;
	}

	public void startGame() {
		// probably reset the game state
		HashMap<String, ServerUser> users = userPool.getUsers();

		int numWaiting = 0;
		for (ServerUser user : users.values()) {
			if (user.getState() == UserState.WAITING) numWaiting++;
		}

		if (numWaiting >= 2 && state == ServerState.WAITING) {
//			view.appendText("Game Started\n");

			this.state = ServerState.IN_PROGRESS;
			UpdateClientStatePacket updateClientStatePacket = new UpdateClientStatePacket(ClientState.IN_PROGRESS);
			try {
				sendPacket(updateClientStatePacket, users);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (ServerUser u : users.values()) {
				if (u.getState() == UserState.WAITING) {
					u.setState(UserState.PLAYING);
					try {
						UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(u.getId(), u.getState());
						sendPacket(updateUserStatePacket, users);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void endGame() {
		if (state == ServerState.IN_PROGRESS) {
//			view.appendText("Game Ended\n");
			HashMap<String, ServerUser> users = userPool.getUsers();

			this.state = ServerState.WAITING;
			UpdateClientStatePacket updateClientStatePacket = new UpdateClientStatePacket(ClientState.WAITING);
			try {
				sendPacket(updateClientStatePacket, users);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (User u : users.values()) {
				if (u.getState() == UserState.PLAYING) {
					u.setState(UserState.WAITING);
					try {
						UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(u.getId(), u.getState());
						sendPacket(updateUserStatePacket, users);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void stopServer() {
		ServerClosePacket packet = new ServerClosePacket();
		try {
			sendPacket(packet, userPool.getUsers());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.stopServer();
		state = ServerState.WAITING;
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
//		view.appendText("[PACKET DATA]: " + new String(packet.getData()) + "\n");

		InetAddress packetIp = packet.getAddress();
		int packetPort = packet.getPort();
		ServerUser user = userPool.findUserByIp(packetIp, packetPort);

		PacketType type = PacketType.lookupPacket(packet);

		// we don't send a ConnectPacket to everyone since when users first connect, they are spectators
		// the clients only ever need to know a player once they are joined, hence AddPlayerPacket
		if (type == PacketType.CONNECT) {
			if (user == null) {
				if (userPool.getUsers().size() >= 2) return;

				ConnectPacket connectPacket = new ConnectPacket(packet);

				user = userPool.addUser(packetIp, packetPort, connectPacket.getUsername());
				IDPacket idPacket = new IDPacket(user.getId());
				sendPacket(idPacket, user);

				// send all the joined users to just connected person
				// should send all the server's current state to the player when they join
				// TODO: might just want to refactor this so we just send all the users
				// with their state, since there is more UserState than just WAITING
				// it would be easier if the client just knew all the connected people, i.e. not just those that are joined
				for (ServerUser su : userPool.getUsers().values()) {
					System.out.println(su.getUsername());
					AddPlayerPacket addPlayerPacket = new AddPlayerPacket(su.getId(), su.getUsername());
					sendPacket(addPlayerPacket, user);
				}
			}
		} else if (type == PacketType.QUIT) {
			QuitPacket quitPacket = new QuitPacket(packet);
			sendPacket(quitPacket, userPool.getUsers());
			userPool.removeUser(packetIp, packetPort);
			endGame();
		}

		if (user == null) return;

		switch (type) {
			case UPDATE_USER_STATE:
				UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(packet);
				sendPacket(updateUserStatePacket, userPool.getUsers());
				if (updateUserStatePacket.getUserState() == UserState.LOST) {
					endGame();
				}
				break;
			case JOIN:
				if (state == ServerState.WAITING) {
					user.setState(UserState.WAITING);
					AddPlayerPacket addPlayerPacket = new AddPlayerPacket(user.getId(), user.getUsername());
					sendPacket(addPlayerPacket, userPool.getUsers());
				}
				break;
			case MESSAGE:
				MessagePacket messagePacket = new MessagePacket(packet);
				messagePacket.setId(user.getId());
				sendPacket(messagePacket, userPool.getUsers());
				break;
			case BOARD:
				BoardPacket boardPacket = new BoardPacket(packet);
				sendPacket(boardPacket, userPool.getUsers());
				break;
		}
	}
}
