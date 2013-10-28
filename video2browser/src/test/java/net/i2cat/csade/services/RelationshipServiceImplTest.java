package net.i2cat.csade.services;

import java.util.List;

import net.i2cat.csade.configuration.RootContext;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.User.Role;
import net.i2cat.csade.services.RelationshipService;
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
public class RelationshipServiceImplTest {
	@Autowired UserService userService;
	@Autowired RelationshipService relationshipService;
	
	private static final Logger logger = LoggerFactory.getLogger(RelationshipServiceImplTest.class);

	@Test
	public void createRelationshipUsers() {
		logger.info("Creating RelationshipUsers....");
		for (int i=0;i<2;i++){
			User u = new User();
			u.setUsername("user_"+i);
			u.setName("user_"+i);
			u.setMiddlename("middlename_"+i);
			u.setSurname(""+i);
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
	}
	
	@Test
	public void createRelationship(){
		try {
			User proposer = userService.findUserByUsername("user_0");
			User contact = userService.findUserByUsername("user_1");
			
			Relationship r = new Relationship();
			r.setContact(contact);
			r.setProposer(proposer);
			
			relationshipService.addRelationship(r);

		} catch (EntityNotFoundException | ExistingEntityException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void findRelationshipsByContact(){
		User contact;
		try {
			contact = userService.findUserByUsername("user_1");
			List<Relationship> rels = relationshipService.getRelationshipsByContact(contact);
			logger.info("Tamany total del llistat de relacions: {}",rels.size());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void findRelationshipsByProposer(){
		//Que passa si busquem un contacte que no té cap?
		User contact;
		try {
			contact = userService.findUserByUsername("user_1");
			List<Relationship> rels = relationshipService.getRelationshipsByProposer(contact);
			logger.info("Tamany total del llistat de relacions: {}",rels.size());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	@Test
	public void findRelationshipsByProposer2(){
		User proposer;
		try {
			proposer = userService.findUserByUsername("user_0");
			List<Relationship> rels = relationshipService.getRelationshipsByProposer(proposer);
			logger.info("Tamany total del llistat de relacions: {}",rels.size());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
		}
	}
	
}
