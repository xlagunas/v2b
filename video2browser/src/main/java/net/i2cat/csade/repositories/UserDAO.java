package net.i2cat.csade.repositories;

import java.util.List;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.models.User;

public interface UserDAO {
	public User addUser(User user) ;
	public User updateUser(User user);
	public User getUser(long idUser) throws EntityNotFoundException;
	public User getUser(String username) throws EntityNotFoundException;
	public void deleteUser(long idUser);
	public boolean isUsernameAvailable(String username);
	public List<User> getAll();
	public List<User> getNonRelatedUsersMatchingKeyword(User user, String keyword);

}
