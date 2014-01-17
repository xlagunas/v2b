package net.i2cat.csade.models;

import java.util.List;
import java.util.UUID;

import net.i2cat.csade.utils.RoomUtils;


public class Room {
	public enum RoomType {
		VIDEO, AUDIO
	}

	private String id;
	private RoomType roomType;
	private List<User> users;

	@SuppressWarnings("restriction")
	public Room() {
		id = new sun.misc.BASE64Encoder().encode(RoomUtils.asByteArray(UUID.randomUUID())).split("=")[0];
	}
	
	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
