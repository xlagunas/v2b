package net.i2cat.csade.services;

import java.util.List;

import net.i2cat.csade.models.Room;
import net.i2cat.csade.models.Room.RoomType;
import net.i2cat.csade.models.User;


public interface RoomService {
	public void addRoom(Room room);
	public Room createRoom(RoomType type);
	public Room findRoom(String UUID);
	public boolean deleteRoom(String UUID);
	public boolean addUsertoRoom(String UUID, User user);
	public boolean removeUserFromRoom(String UUID, User user);
	public List<String> getUsersInRoom(String UUID);
	public List<String> getOtherUsersInRoom(String UUID, String username);
}
