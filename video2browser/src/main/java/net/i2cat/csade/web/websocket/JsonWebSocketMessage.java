package net.i2cat.csade.web.websocket;

import java.io.Serializable;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonAutoDetect
public class JsonWebSocketMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Header {
		ROSTER, CALL, WEBRTC, CALL_ACK
	}

	public enum Method {
		ROSTER_ADD, ROSTER_DELETE, ROSTER_LIST, CALL_JOIN, CALL_CREATE, CALL_INVITE, WEBRTC_ADD_ICE_CANDIDATE, WEBRTC_SEND_OFFER, WEBRTC_SEND_ANSWER
	}

	private Header header;

	private Method method;

	private Object content;

	private String sender;

	private String receiver;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public JsonWebSocketMessage() {
		super();
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public static TextMessage createMessage(Header header, Method method,
			Object content) {
		JsonWebSocketMessage message = new JsonWebSocketMessage();
		message.setContent(content);
		message.setHeader(header);
		message.setMethod(method);

		ObjectMapper mapper = new ObjectMapper();

		String output;
		try {
			output = mapper.writeValueAsString(message);
			return new TextMessage(output);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return new TextMessage("{'error': 'parsing error'}");
	}

}