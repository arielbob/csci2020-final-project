package net.server;

import net.client.ClientState;
import net.packet.*;
import net.test.ServerTest;
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

public class TetrisServer extends Server {
	private ServerUserPool userPool; // contains all connected users, they do not have to be joined in the game
	private ServerTest view;
	private ServerState state;

	private Thread gameThread;

	public TetrisServer(int port) throws SocketException {
		super(port);
		this.userPool = new ServerUserPool();
		this.state = ServerState.WAITING;
	}

	public void setView(ServerTest view) {
		this.view = view;
	}

	public void startGame() {
		// probably reset the game state
		HashMap<String, ServerUser> users = userPool.getUsers();

		int numWaiting = 0;
		for (ServerUser user : users.values()) {
			if (user.getState() == UserState.WAITING) numWaiting++;
		}

		if (numWaiting > 1 && state == ServerState.WAITING) {
			view.appendText("Game Started\n");

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

			// start game loop
			gameThread = new Thread(() -> {
				while (state == ServerState.IN_PROGRESS) {
					BoardPacket boardPacket = new BoardPacket(new int[0][0]);
					try {
						// update game
						// send game state to everyone

						/*
						for user in userPool:
							if (user.state == IN_PROGRESS):
								user.update()
								// BoardPacket is board + player piece combined in one single array
								// all clients just need to display their board; they don't need to
								// know the specifics about where their player is, since that is already
								// in the board; and all changes are done through the server
								sendPacket(new BoardPacket(user), users)
								if (user.state == WAITING):
									user.place = numUsers - numWinners++
									sendPacket(new PlacePacket(user.place), users)
									sendPacket(new ClientStatePacket(ClientState.WAITING), user)
									sendPacket(new BoardPacket(user), user)
						 */

						sendPacket(boardPacket, users);
						Thread.sleep(1000);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			gameThread.start();
		}
	}

	public void endGame() {
		if (state == ServerState.IN_PROGRESS) {
			view.appendText("Game Ended\n");
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

	public void close() {
		super.stopServer();
		state = ServerState.WAITING;
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		view.appendText("[PACKET DATA]: " + new String(packet.getData(), StandardCharsets.US_ASCII) + '\n');

		InetAddress packetIp = packet.getAddress();
		int packetPort = packet.getPort();
		ServerUser user = userPool.findUserByIp(packetIp, packetPort);

		PacketType type = PacketType.lookupPacket(packet);

		// we don't send a ConnectPacket to everyone since when users first connect, they are spectators
		// the clients only ever need to know a player once they are joined, hence AddPlayerPacket
		if (type == PacketType.CONNECT) {
			if (user == null) {
				user = userPool.addUser(packetIp, packetPort, "user");
				IDPacket idPacket = new IDPacket(user.getId());
				sendPacket(idPacket, user);

				// send all the joined users to just connected person
				// should send all the server's current state to the player when they join
				// TODO: might just want to refactor this so we just send all the users
				// with their state, since there is more UserState than just WAITING
				// it would be easier if the client just knew all the connected people, i.e. not just those that are joined
				for (ServerUser su : userPool.getUsers().values()) {
					if (su.getState() == UserState.WAITING) {
						AddPlayerPacket addPlayerPacket = new AddPlayerPacket(su.getId(), su.getUsername());
						sendPacket(addPlayerPacket, user);
					}
				}
			}
		}

		if (user == null) return;

		switch (type) {
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
