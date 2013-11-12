package net.i2cat.csade.web.controllers;

import java.util.List;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.services.RelationshipService;
import net.i2cat.csade.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relationships")
public class RelationshipController extends AbstractExceptionController {
	private final static Logger log = LoggerFactory.getLogger(RelationshipController.class);
	private final RelationshipService relationshipService;
	private final UserService userService;
	
	@Autowired
	public RelationshipController(RelationshipService relationshipService, UserService userService){
		log.info("Initializing Relationship controller...");
		this.relationshipService = relationshipService;
		this.userService = userService;
	}
	@RequestMapping(value="/list",method=RequestMethod.POST )
	public List<Relationship> getContactRelationships(@RequestBody User user){
		log.info("REST Interface. Requesting friendships whose contact is {}", user.getUsername());
		List<Relationship> relations =  relationshipService.getRelationshipsByProposer(user);
		return relations;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.PUT)
	public Relationship addRelationship(@RequestParam("proposer")String proposer, @RequestParam("contact")String contact) throws EntityNotFoundException, ExistingEntityException{
		log.info("REST Interface. Requesting new Relationship");
		Relationship relationship = new Relationship();
		User proposerUser = userService.findUserByUsername(proposer);
		User contactUser = userService.findUserByUsername(contact);
		relationship.setContact(contactUser);
		relationship.setProposer(proposerUser);
		return relationshipService.addRelationship(relationship);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseStatus(value= HttpStatus.OK)
	public void updateRelationship(@RequestBody Relationship relationship){
		log.info("REST Interface. Requesting Relationship update");
		relationshipService.updateRelationship(relationship);
	}
}
