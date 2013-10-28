package net.i2cat.csade.services;

import net.i2cat.csade.configuration.RootContext;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.User.Role;
import net.i2cat.csade.services.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = { RootContext.class })
@Transactional
public class UserServiceImplTest {
	@Autowired UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);

	@Test
	public void addUser() {
		User u = new User();
		u.setUsername("xlagunas");
		u.setName("Xavier");
		u.setMiddlename("Lagunas");
		u.setSurname("Calpe");
		u.setRole(Role.USER);
		u.setThumbnail("http://www.google.es");
		
		try {
			logger.info("Attempting to create User");
			User user = userService.createUser(u);
			logger.info("User successfully created");
			logger.info("id: "+user.getIdUser());
			
		} catch (ExistingEntityException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void loadUserById(){
		logger.info("Atempting to get User xlagunas by id");
		try {
			User u = userService.findUserById(1);
			logger.info("User {} was found", u.getUsername());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void loadUserByUsername(){
		logger.info("Atempting to get User xlagunas by username");
		try {
			User u = userService.findUserByUsername("xlagunas");
			logger.info("User {} was found", u.getIdUser());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void deleteUserById(){
		logger.info("Attempting to delete User 1");
		logger.info("Deletion finished with status: {}",userService.deleteUser(1));
	}

}
