package net.i2cat.csade.repositories;

import java.util.List;

import net.i2cat.csade.models.User;

public interface UserDAO {
	public User addUser(User user) ;
	public User updateUser(User user);
	public User getUser(long idUser);
	public User getUser(String username);
	public void deleteUser(long idUser);
	public boolean isUsernameAvailable(String username);
	public List<User> getAll();
}
