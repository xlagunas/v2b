package net.i2cat.csade.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.Relationship.RelationshipStatus;
import net.i2cat.csade.models.User;


@Repository
public class RelationshipDAOImpl implements RelationshipDAO {
	private static final Logger logger = LoggerFactory.getLogger(RelationshipDAO.class);
	private final SessionFactory sessionFactory;

	@Autowired
	public RelationshipDAOImpl(SessionFactory sessionFactory) {
		logger.info("Initializing RelationshipDAO implementation...");
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Relationship addRelationship(Relationship relationship) {
		this.getSession().save(relationship);
		return relationship;
	}

	@Override
	public Relationship updateRelationship(Relationship relationship) {
		this.getSession().update(relationship);
		return relationship;
	}

	@Override
	public Relationship getRelationship(Long idRelationship) {
		Relationship rel = (Relationship)getSession().get(Relationship.class, idRelationship);
		if (rel == null) {
			rel = new Relationship();
		}
		return rel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Relationship> getRelationshipsByProposer(User proposer) {
		List<Relationship> relationships = getSession().createCriteria(Relationship.class)
		.add(Restrictions.eq("proposer", proposer)).list();
		return relationships;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Relationship> getRelationshipsByContact(User contact) {
		List<Relationship> relationships = getSession().createCriteria(Relationship.class)
				.add(Restrictions.eq("contact", contact)).list();
				return relationships;
	}

	
	public void deleteRelationship(Relationship relationship) {
		this.getSession().delete(relationship);
	}
	
	public void deleteRelationship(long idRelationship){
		Session s = this.getSession();
		s.delete(s.get(Relationship.class, idRelationship));
	}

	@Override
	public boolean isFoundInDatabase(Relationship r) {
		Map<String, Object> constraints = new HashMap<>();
			constraints.put("contact", r.getContact());
			constraints.put("proposer", r.getProposer());
		return getSession().createCriteria(Relationship.class)
				.add(Restrictions.allEq(constraints))
				.setProjection(Projections.id())
				.uniqueResult()!=null
				?true:false;
	}


	@Override
	public List<Relationship> getRelationshipsByStatus(long idUser, RelationshipStatus status) {
		
		String userType = (status == RelationshipStatus.REQUESTED)?"contact":"proposer";
		
		return getSession().createCriteria(Relationship.class, "rel")
				.createAlias(userType, "c")
				.add(Restrictions.eq("c.idUser", idUser))
				.add(Restrictions.eq("status", status))
			.list();
	}
	
	
	
	
}
