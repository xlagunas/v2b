package net.i2cat.csade.services;

import java.util.List;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.Relationship.RelationshipStatus;

public interface RelationshipService {
	public Relationship addRelationship(Relationship relationship) throws ExistingEntityException;

	public Relationship updateRelationship(Relationship relationship);

	public Relationship getRelationship(Long idRelationship) throws EntityNotFoundException;

	public List<Relationship> getRelationshipsByProposer(User proposer);

	public List<Relationship> getRelationshipsByContact(User contact);

	public boolean deleteRelationship(long idRelationship);
	
	public List<Relationship> getRelationshipsByStatus(long idUser, RelationshipStatus status);

	
}
