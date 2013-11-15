package net.i2cat.csade.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.models.User;
import net.i2cat.csade.repositories.UserDAO;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final UserDAO userDAO;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO) {
		logger.info("Initializing UserService implementation...");
		this.userDAO = userDAO;
	}

	@Override
	public User createUser(User user) throws ExistingEntityException {
		logger.info("Attempting to add user with username {} to database", user.getUsername());
		if (!userDAO.isUsernameAvailable(user.getUsername())){
			logger.error("A user with that username has been found, aborting creation.");
			throw new ExistingEntityException();
		}
		userDAO.addUser(user);
		logger.info("User {} successfully created with id: {}", user.getUsername(), user.getIdUser());
		return user;
	}

	@Override
	public User findUserById(long idUser) throws EntityNotFoundException {
		logger.info("Attempting to get User with id: {}", idUser );
		User user = userDAO.getUser(idUser);
		if (user.getIdUser() == 0){
			logger.error("User with id:{} not found in the database", idUser);
			throw new EntityNotFoundException();
		}
		return user;
	}

	@Override
	public User findUserByUsername(String username)	throws EntityNotFoundException {
		logger.info("Attempting to get User with username: {}", username );
		User user = userDAO.getUser(username);
		if (user.getIdUser() == 0){
			logger.error("User with username: {} not found in the database", username);
			throw new EntityNotFoundException();
		}
		return user;
	}

	@Override
	public boolean deleteUser(long idUser) {
		try{
			logger.info("Attempting to delete User with id: {}", idUser);
			userDAO.deleteUser(idUser);
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public List<User> findAll() {
		logger.info("Requesting all Users in the database");
		return userDAO.getAll();
	}

	@Override
	public User updateUser(User user) {
		logger.info("Attempting to update User with id: {}", user.getIdUser());
		return userDAO.updateUser(user);
	}
	
	@Scheduled(fixedDelay=5000)
	@Override
	public void cleanConnections(){
		logger.info("entrant al scheduler");
	}

}
