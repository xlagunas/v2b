package net.i2cat.csade.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.models.User;

public interface UserService extends UserDetailsService{
	public User createUser(User user)  throws ExistingEntityException;
	public User updateUser(User user);
	public User findUserById(long idUser) throws EntityNotFoundException;
	public User findUserByUsername(String username) throws EntityNotFoundException;
	public boolean deleteUser(long idUser);
	public List<User> findAll();
	public Boolean isUsernameAvailable(String username);
	public List<User> getNonRelatedUsersMatchingKeyword(User user, String keyword);


}
