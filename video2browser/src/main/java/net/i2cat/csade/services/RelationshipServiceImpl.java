package net.i2cat.csade.services;

import java.util.Calendar;
import java.util.List;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.repositories.RelationshipDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RelationshipServiceImpl implements RelationshipService{
	private static final Logger logger = LoggerFactory.getLogger(RelationshipService.class);
	private final RelationshipDAO relationshipDAO;
	
	@Autowired
	public RelationshipServiceImpl(RelationshipDAO relationshipDAO) {
		logger.info("Initializing RelationshipService implementation...");
		this.relationshipDAO = relationshipDAO;
	}

	@Override
	public Relationship addRelationship(Relationship relationship) throws ExistingEntityException{
		logger.info("Attempting to add a new Relationship between {} and {}.",
				relationship.getProposer().getUsername(), relationship.getContact().getUsername());
			if (relationshipDAO.isFoundInDatabase(relationship)){
				logger.error("A relationship between {} and {} already exists in database",
						relationship.getProposer().getUsername(),
						relationship.getContact().getUsername());
				throw new ExistingEntityException();
			}
		return relationshipDAO.addRelationship(relationship);
	}

	@Override
	public Relationship updateRelationship(Relationship relationship){
		logger.info("Updating Relationship with id: {}", relationship.getIdRelationship());
		relationship.setUpdateDate(Calendar.getInstance());
		return relationshipDAO.updateRelationship(relationship);
	}

	@Override
	public Relationship getRelationship(Long idRelationship) throws EntityNotFoundException {
		logger.info("Requesting Relationship with id: {}", idRelationship);
		Relationship r = relationshipDAO.getRelationship(idRelationship);
		if (r.getIdRelationship() == 0){
			logger.error("Relationship with id {} was not found", idRelationship);
			throw new EntityNotFoundException();
		}
		return r;
	}

	@Override
	public List<Relationship> getRelationshipsByProposer(User proposer) {
		logger.info("Requesting Relationships whose proposer is {}", proposer.getUsername());
		return relationshipDAO.getRelationshipsByProposer(proposer);
	}

	@Override
	public List<Relationship> getRelationshipsByContact(User contact) {
		logger.info("Requesting Relationships whose contact is {}", contact.getUsername());
		return relationshipDAO.getRelationshipsByContact(contact);
	}


	@Override
	public boolean deleteRelationship(long idRelationship) {
		logger.info("Deleting relationship with id: {}", idRelationship);
		try{
			relationshipDAO.deleteRelationship(idRelationship);
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	

}
