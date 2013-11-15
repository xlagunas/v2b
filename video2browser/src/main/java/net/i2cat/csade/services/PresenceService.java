package net.i2cat.csade.services;

import java.util.List;

import net.i2cat.csade.exceptions.presence.UserAlreadyConnectedException;

import org.springframework.web.socket.WebSocketSession;

public interface PresenceService {
	public void addConnection(String username, WebSocketSession session) throws UserAlreadyConnectedException;
	public List<WebSocketSession> getAvailableConnections();
	public void deleteConnection(String username);
	public void cleanConnections();
}
