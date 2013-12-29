package net.i2cat.csade.web.websocket;

import net.i2cat.csade.exceptions.presence.UserAlreadyConnectedException;
import net.i2cat.csade.models.Room;
import net.i2cat.csade.models.User;
import net.i2cat.csade.services.PresenceService;
import net.i2cat.csade.services.RoomService;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Header;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketMainHandler extends TextWebSocketHandler {
	private Logger log = LoggerFactory.getLogger(WebSocketMainHandler.class);
	@Autowired private PresenceService presenceService;
	@Autowired private RoomService roomService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		log.info("Connection Established");

		if (session.getPrincipal()!= null){
			String username = session.getPrincipal().getName();
			log.info("principal: {}", username);
			try{
				presenceService.addConnection(username, session);
			}catch(UserAlreadyConnectedException e){
				log.warn("Warning, user {} already connected, proceeding to disconnect previous sessions", username);
				presenceService.deleteConnection(username);
				presenceService.addConnection(username, session);
			}
			session.sendMessage(JsonWebSocketMessage.createMessage(Header.ROSTER, Method.ROSTER_LIST, presenceService.getRoster(username)));
		}
		else{
			log.info("Unauthenticated user");
		}
		super.afterConnectionEstablished(session);
	}

	@Override
	public void handleMessage(WebSocketSession session,	WebSocketMessage<?> message) throws Exception {
		log.info("A Message Reached the server: {}", message.getPayload());
		ObjectMapper mapper = new ObjectMapper();
		
		JsonWebSocketMessage rcvMessage = mapper.readValue((String)message.getPayload(), JsonWebSocketMessage.class);
		
		switch(rcvMessage.getHeader()){
		case CALL:
			Room room = mapper.convertValue(rcvMessage.getContent(), Room.class);
			switch(rcvMessage.getMethod()){
			case CALL_CREATE:
				roomService.addRoom(room);
				session.sendMessage(JsonWebSocketMessage.createMessage(Header.CALL_ACK, Method.CALL_CREATE, room));
				String receiver = rcvMessage.getReceiver();
				if (receiver != null){
					User dstUser = presenceService.getConnection(receiver);
					WebSocketSession dstSession = dstUser.getSession();
					if (dstSession.isOpen()){
						dstSession.sendMessage(JsonWebSocketMessage.createMessage(Header.CALL_ACK, Method.CALL_INVITE, room));
					}
				}
				break;
			case CALL_JOIN:
				String sender = rcvMessage.getSender();
				room = roomService.findRoom(room.getId());
				session.sendMessage(JsonWebSocketMessage.createMessage(Header.CALL_ACK, Method.CALL_JOIN, room));
				//First we send the Room back as is, without adding the new user, afterwards we add the new user
				User user = presenceService.getConnection(sender);
				roomService.addUsertoRoom(room.getId(), user);
				break;
			case CALL_INVITE:
				
				break;
			default:
				break;
			}
			
			break;
		case ROSTER:
			break;
		case WEBRTC:
			//Server doesn't process webRTC messages, just forwards it to its receiver
			String destination = rcvMessage.getReceiver();
			if (destination!= null){
				User receiver = presenceService.getConnection(destination);
				WebSocketSession receiverSession = receiver.getSession();
				if (receiverSession.isOpen()){
					receiverSession.sendMessage(message);
				}
			}
			break;
		default:
			break;
		}

	
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,	CloseStatus status) throws Exception {
		String username = session.getPrincipal().getName();
		log.info("Connection closed: {}", username);
		presenceService.deleteConnection(username);
//		super.afterConnectionClosed(session, status);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info("Connection error");
//		super.handleTransportError(session, exception);
	}
	
	

}
