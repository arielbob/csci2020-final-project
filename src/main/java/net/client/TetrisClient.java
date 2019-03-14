package net.client;

import net.Callback;
import net.packet.*;
import net.test.ClientTest;
import net.user.User;
import net.user.UserPool;
import net.user.UserState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

// TODO: add client states, so that they can't send certain packets without first joining
public class TetrisClient extends Client {
	private UserPool userPool; // only contains the ones that are joined in the game (i.e. no spectators)
	private UUID id;
	private ClientTest view;
	private ClientState state;

	public TetrisClient(InetAddress address, int port) throws SocketException {
		super(address, port);
		this.userPool = new UserPool();
		this.state = ClientState.WAITING;
	}

	public void setView(ClientTest view) {
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

	// TODO: add TetrisClient states
	public void sendMessage(String message) throws IOException {
		if (this.id != null) {
			MessagePacket packet = new MessagePacket(this.id, message);
			sendPacket(packet);
		}
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
				ClientState clientState = updateClientStatePacket.getClientState();
				this.state = clientState;
				view.appendText("NEW CLIENT STATE: " + this.state + '\n');
				break;
			case UPDATE_USER_STATE:
				UpdateUserStatePacket updateUserStatePacket = new UpdateUserStatePacket(packet);
				user = userPool.findUserById(updateUserStatePacket.getId());
				if (user != null) {
					user.setState(updateUserStatePacket.getUserState());
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
		}
	}
}
