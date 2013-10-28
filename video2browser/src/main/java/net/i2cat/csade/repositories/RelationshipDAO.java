package net.i2cat.csade.repositories;

import java.util.List;

import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;

public interface RelationshipDAO {
	
	public Relationship addRelationship(Relationship relationship);

	public Relationship updateRelationship(Relationship relationship);

	public Relationship getRelationship(Long idRelationship);

	public List<Relationship> getRelationshipsByProposer(User proposer);

	public List<Relationship> getRelationshipsByContact(User contact);

	public void deleteRelationship(Relationship relationship);

	public void deleteRelationship(long idRelationship);
	
	public boolean isFoundInDatabase(Relationship r);

}
