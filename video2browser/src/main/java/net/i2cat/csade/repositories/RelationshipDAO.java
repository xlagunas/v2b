package net.i2cat.csade.repositories;

import java.util.List;

import org.hibernate.Session;

import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.Relationship.RelationshipStatus;

public interface RelationshipDAO {
	
	public Relationship addRelationship(Relationship relationship);

	public Relationship updateRelationship(Relationship relationship);

	public Relationship getRelationship(Long idRelationship);

	public List<Relationship> getRelationshipsByProposer(User proposer);

	public List<Relationship> getRelationshipsByContact(User contact);

	public void deleteRelationship(Relationship relationship);

	public void deleteRelationship(long idRelationship);
	
	public boolean isFoundInDatabase(Relationship r);
	
	/**
	 * 
	 * That method returns a List filtered by the type of {@link RelationshipStatus} we want to filter,
	 * 
	 * in case we want the pending relationships, we fetch the relationships where the idUser plays a 
	 * role of contact instead of a proposer
	 * 
	 * @param idUser userId to extract the relationships
	 * @param status relationshipStatus to filter
	 * 
	 * @return a List with the relationships fetched
	 * */
	public List<Relationship> getRelationshipsByStatus(long idUser, RelationshipStatus status);
	

}
