package net.i2cat.csade.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

public class WebSocketMainHandler extends TextWebSocketHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(WebSocketMainHandler.class);
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		log.info("Connection Established");

		if (session.getPrincipal()!= null){
			log.info("principal: {}", session.getPrincipal().getName());
		}
		else{
			log.info("Unauthenticated user");
		}
		super.afterConnectionEstablished(session);
	}

	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {
		log.info("A Message Reached the server: {}", message.getPayload());
		super.handleMessage(session, message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,	CloseStatus status) throws Exception {
		log.info("Connection closed");
		super.afterConnectionClosed(session, status);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info("Connection error");
		super.handleTransportError(session, exception);
	}
	
	

}
