package net.client;

import net.packet.*;
//import net.test.ClientTest;
import net.user.User;
import net.user.UserPool;
import net.user.UserState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

import UserInterface.ClientView;

public class TetrisClient extends Client {
	private UserPool userPool; // only contains the ones that are joined in the game (i.e. no spectators)
	private UUID id;
	private ClientView view;
	private ClientState state;

	public TetrisClient(InetAddress address, int port, ClientView view) throws SocketException {
		super(address, port);
		this.userPool = new UserPool();
		this.state = ClientState.WAITING;
		this.view = view;
	}

	public void connect() throws IOException {
		ConnectPacket packet = new ConnectPacket();
		sendPacket(packet);
	}

	public void joinGame() throws IOException {
		JoinPacket packet = new JoinPacket();
		sendPacket(packet);
	}

	public void quitGame() throws IOException {
		QuitPacket packet = new QuitPacket(id);
		sendPacket(packet);
	}

	public void sendMessage(String message) throws IOException {
		if (this.id != null) {
			MessagePacket packet = new MessagePacket(this.id, message);
			sendPacket(packet);
		}
	}

	public void sendBoardState(int[][] board) throws IOException {
		if (userPool.findUserById(id).getState() == UserState.PLAYING) {
			BoardPacket packet = new BoardPacket(this.id, board);
			sendPacket(packet);
		}
	}

	public void sendLose() throws IOException {
		view.setOpponentWin();
		sendUserState(id, UserState.LOST);
	}

	private void sendUserState(UUID id, UserState userState) throws IOException {
		UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(id, UserState.LOST);
		sendPacket(updateUserStatePacket);
	}

	public void stopClient() {
		try {
			quitGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.stopClient();
	}

	@Override
	void receivePacket(DatagramPacket packet) throws IOException {
		PacketType type = PacketType.lookupPacket(packet);
		User user;

		switch (type) {
			case ID:
				IDPacket idPacket = new IDPacket(packet);
				this.id = idPacket.getID();
				view.appendText("CONNECTED WITH ID " + this.id.toString() + '\n');
				view.setConnected();
				break;
			case ADD_PLAYER:
				AddPlayerPacket addPlayerPacket = new AddPlayerPacket(packet);
				user = new User(addPlayerPacket.getId(), addPlayerPacket.getUsername());
				user.setState(UserState.WAITING);
				userPool.addUser(user);
				view.appendText("PLAYER JOINED " + addPlayerPacket.getId() + '\n');
				break;
			case UPDATE_CLIENT_STATE:
				UpdateClientStatePacket updateClientStatePacket = new UpdateClientStatePacket(packet);
				this.state = updateClientStatePacket.getClientState();
				view.appendText("NEW CLIENT STATE: " + this.state + '\n');
				break;
			case UPDATE_USER_STATE:
				UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(packet);
				user = userPool.findUserById(updateUserStatePacket.getId());
				if (user != null) {
					user.setState(updateUserStatePacket.getUserState());
					boolean isClient = updateUserStatePacket.getId().toString().equals(id.toString());
					if (isClient) {
						switch (user.getState()) {
							case PLAYING:
								view.startGame();
								break;
						}
					} else {
						switch (user.getState()) {
							case LOST:
								view.setOpponentLose();
								view.setClientWin();
								break;
						}
					}
				}
				break;
			case MESSAGE:
				MessagePacket messagePacket = new MessagePacket(packet);
				user = userPool.findUserById(messagePacket.getId());
				if (user != null) {
					UserState userState = user.getState();
					view.appendText("[" + userState + "] " + messagePacket.getId() + ": " + messagePacket.getMessage() + '\n');
				}
				break;
			case BOARD:
				BoardPacket boardPacket = new BoardPacket(packet);
				view.appendText("board update\n");

				user = userPool.findUserById(boardPacket.getId());
				if (!user.getId().toString().equals(this.id.toString())) {
					view.receiveBoardState(boardPacket.getBoard());
				}
				break;
			case QUIT:
				QuitPacket quitPacket = new QuitPacket(packet);
				boolean isClient = quitPacket.getId().toString().equals(id.toString());
				if (!isClient && state == ClientState.IN_PROGRESS) {
					view.setClientWin();
					view.setOpponentLose();
				}
				break;
		}
	}
}
