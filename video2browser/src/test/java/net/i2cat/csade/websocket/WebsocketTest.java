package net.i2cat.csade.websocket;

import static org.junit.Assert.*;

import java.io.IOException;

import net.i2cat.csade.configuration.RootContext;
import net.i2cat.csade.models.Room;
import net.i2cat.csade.models.Room.RoomType;
import net.i2cat.csade.models.User;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Header;
import net.i2cat.csade.web.websocket.JsonWebSocketMessage.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = { RootContext.class })
@Transactional
public class WebsocketTest {
	private static final Logger logger = LoggerFactory.getLogger(WebsocketTest.class);

	@Test
	public void JsonGeneration() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonWebSocketMessage jwm = new JsonWebSocketMessage();
		Room r = new Room();
		r.setRoomType(RoomType.VIDEO);
		jwm.setContent(r);
		jwm.setHeader(Header.ROSTER);
		jwm.setMethod(Method.ROSTER_ADD);
		
		String json = mapper.writeValueAsString(jwm);
		
		ObjectMapper mapper2 = new ObjectMapper();
		//mapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonWebSocketMessage m = mapper2.readValue(json, JsonWebSocketMessage.class);
		Room jnode = mapper2.convertValue(m.getContent(), Room.class);
//		JsonNode jnode = m.getContent();
		System.out.println(jnode.getId());
}

}
