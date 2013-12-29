package net.i2cat.csade.services;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.i2cat.csade.models.Room;
import net.i2cat.csade.models.Room.RoomType;
import net.i2cat.csade.models.User;

@Service
public class RoomServiceImpl implements RoomService{
	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
	private Map<String, Room> rooms;
	
	public RoomServiceImpl(){
		logger.info("Initializing RoomService...");
		rooms = new Hashtable<String, Room>();
	}
	
	@Override
	public void addRoom(Room room){
		rooms.put(room.getId(), room);
	}
	
	@Override
	public Room createRoom(RoomType type) {
		Room r = new Room();
		r.setRoomType(type);
		rooms.put(r.getId(), r);
		logger.info("Room with id: {} successfully created ", r.getId());
		return r;
	}

	@Override
	public Room findRoom(String UUID) {
		logger.info("Searching room with id: {}", UUID);
		Room r = rooms.get(UUID);
		if (r!= null){
			return r;
		}
		else
			logger.error("Room with id: {} not found", UUID);
		return null;
	}

	@Override
	public boolean deleteRoom(String UUID) {
		logger.info("Deleting room with id: {}", UUID);
		 return rooms.remove(UUID)!= null?true:false;
	}
	
	@Override
	public boolean addUsertoRoom(String UUID, User user){
		String username = user.getUsername();
		logger.info("Appending user {} to room {}", username, UUID);
		Room r = rooms.get(UUID);
		if (r == null){
			logger.error("User {} attempted to join inexisting room {}",username, UUID);
			return false;
		}
		List<User> users = rooms.get(UUID).getUsers();
		if (users == null){
			logger.debug("Creating arrayList of User Sessions");
			users = new ArrayList<User>();
		}
		else{
			for (User conferenceUser : users) {
				if (conferenceUser.getUsername().equals(username)){
					logger.warn("User {} not added, it is already inside the room",username);
					if (conferenceUser.getSession().equals(user.getSession())){
						logger.warn("User {} not modified, has the same session registered", username);
						return false;
					}
					else{
						conferenceUser.setSession(user.getSession());
					}
					return true;
				}
			}
		}
		users.add(user);
		r.setUsers(users);
		return true;
	}

	@Override
	public boolean removeUserFromRoom(String UUID, User user) {
		String username = user.getUsername();
		logger.info("Removing user {} from room {}", username, UUID);
		Room r = rooms.get(UUID);
		if (r == null){
			logger.error("User {} attempted to exit inexisting room {}",username, UUID);
			return false;
		}
		Iterator<User> it = r.getUsers().iterator();
		boolean found = false;
		while (it.hasNext() && !found){
			User conferenceUser = it.next();
			if (conferenceUser.getUsername().equals(username)){
				it.remove();
				found = true;
			}
		}
		if (r.getUsers().size()==0){
			logger.info("Last user leaved room {}, deleting it", r.getId());
			rooms.remove(r.getId());
		}
		return found;
	}
	
	@Override
	public List<String> getUsersInRoom(String UUID){
		logger.info("Requesting active users in room {}", UUID);
		List<String> users = null;
		Room r = this.findRoom(UUID);
		if (r.getUsers()!= null){
			int length = r.getUsers().size();
			if (length>0){
				users = new ArrayList<String>(length);
				for (User contacts : r.getUsers()) {
					if (contacts.getSession().isOpen())
						users.add(contacts.getUsername());
				}
			}
		}
		return users;
	}
	
	@Override
	public List<String> getOtherUsersInRoom(String UUID, String username){
		logger.info("Requesting active users in room {}", UUID);
		List<String> users = null;
		Room r = this.findRoom(UUID);
		if (r.getUsers()!= null){
			int length = r.getUsers().size();
			if (length>0){
				users = new ArrayList<String>(length);
				for (User contacts : r.getUsers()) {
					if (contacts.getSession().isOpen() && !contacts.getUsername().equals(username))
						users.add(username);
				}
			}
		}
		return users;
	}


}
