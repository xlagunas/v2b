package net.i2cat.csade.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.presence.UserAlreadyConnectedException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.Relationship.RelationshipStatus;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.User.Status;
import net.i2cat.csade.repositories.RelationshipDAO;
import net.i2cat.csade.repositories.UserDAO;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Header;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

@Service
@Transactional
public class PerUserPresenceServiceImpl implements PresenceService{
	private static final Logger logger = LoggerFactory.getLogger(PerUserPresenceServiceImpl.class);
	private final Map<String, User> connectedUsers;
	private final Map<String, List<String>> roster;
	private final RelationshipDAO relationshipDAO;
	private final UserDAO userDAO;
	
	@Autowired
	public PerUserPresenceServiceImpl(RelationshipDAO relationshipDAO, UserDAO userDAO) {
		logger.info("Initializing PerUserPresence Service...");
		connectedUsers = new HashMap<String, User>();
		roster = new HashMap<String, List<String>>();
		this.relationshipDAO = relationshipDAO;
		this.userDAO = userDAO;
	}

	public void addConnection(String username, WebSocketSession session) throws UserAlreadyConnectedException {
		logger.info("addingConnection Websocket to user {}", username);
		User user = connectedUsers.get(username);
		if (user == null){
			try {
				user = userDAO.getUser(username);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (user != null){
			user.setStatus(Status.AVAILABLE);
			user.setSession(session);
			List<Relationship> relations = relationshipDAO
					.getRelationshipsByStatus(user.getIdUser(), RelationshipStatus.ACCEPTED);
			
			List<String> contacts = new ArrayList<String>();
			//TODO falta comprovar
			for (Relationship relationship : relations) {
				User contact = relationship.getContact();
				contacts.add(contact.getUsername());
				contact = connectedUsers.get(contact.getUsername());
				if(contact != null && contact.getSession().isOpen()){
					try {
						contact.getSession().sendMessage(
								JsonWebSocketMessage
									.createMessage(Header.ROSTER, Method.ROSTER_ADD, user));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			connectedUsers.put(username, user);
			roster.put(username, contacts);
		}
		
		
	}

	public void deleteConnection(String username) {
		User droppedUser = this.connectedUsers.remove(username);
		droppedUser.setStatus(Status.OFFLINE);
		List<String> users = roster.get(username);
		
		for (String user : users) {
			User contact = connectedUsers.get(user);
			if (contact != null && contact.getSession().isOpen()){
				try {
					contact.getSession().sendMessage(JsonWebSocketMessage.createMessage(Header.ROSTER, Method.ROSTER_DELETE, droppedUser));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	@Scheduled(fixedRate=60000)
	public void cleanConnections() {
		logger.info("cleaning connections");
		List<User> users = new ArrayList<User>(connectedUsers.values());
		Iterator<User> userIterator = users.iterator();
		
		while(userIterator.hasNext()){
			User u = userIterator.next();
			if (u.getSession().isOpen() == false){
				logger.info("Found one closed session, closing it");
				//First we set it to offline, then we send that message to its contacts and after
				//that we remove the user from the array (not the list)
				u.setStatus(Status.OFFLINE);
				List<String> contacts = roster.get(u.getUsername());
				for (String contact : contacts) {
					User receiver = connectedUsers.get(contact);
					if (receiver != null && receiver.getSession().isOpen() == true){
						try {
							receiver.getSession().sendMessage(
									JsonWebSocketMessage.createMessage(Header.ROSTER, Method.ROSTER_DELETE, u));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				userIterator.remove();
			}
			else{
			}
		}
		
	}

	@Override
	public List<User> getRoster(String username) {
		List<User> users = new ArrayList<User>();
		for (String contactName : roster.get(username)) {
			User user = connectedUsers.get(contactName);
			if (user != null && user.getSession().isOpen() == true){
				users.add(user);
			}
		}
		return users;
	}

	@Override
	public User getConnection(String username) {
		return connectedUsers.get(username);
	}

	

}
