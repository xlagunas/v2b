package net.i2cat.csade.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.i2cat.csade.exceptions.presence.UserAlreadyConnectedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class PresenceServiceImpl implements PresenceService{
	private final Logger logger = LoggerFactory.getLogger(PresenceServiceImpl.class);
	private final Map<String, WebSocketSession> connections;
	
	public PresenceServiceImpl(){
		connections = new HashMap<String, WebSocketSession>();
	}
	
	@Override
	public void addConnection(String username, WebSocketSession session) throws UserAlreadyConnectedException {
		if (connections.get(username)!= null)
			throw new UserAlreadyConnectedException();
		connections.put(username, session);
		logger.info("User {} added to connection list", username);

	}

	@Override
	public List<WebSocketSession> getAvailableConnections() {
		logger.info("Requesting available connections");
		List<WebSocketSession> sessions = new ArrayList<WebSocketSession>(connections.values());
		for (WebSocketSession webSocketSession : sessions) {
			if (webSocketSession.isOpen() == false){
				sessions.remove(webSocketSession);
			}
		}
		return sessions;
	}

	@Override
	public void deleteConnection(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Scheduled(fixedRate=60000)
	public void cleanConnections() {
		logger.info("Hola");
		for (WebSocketSession session : connections.values()) {
			logger.error("NO FA RES ENCARA");
		}
		
	}

}
