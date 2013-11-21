package net.i2cat.csade.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;

@Repository
public class UserDAOImpl implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	private final SessionFactory sessionFactory;
	
	@Autowired
	public UserDAOImpl(SessionFactory sessionFactory) {
		logger.info("Initializing UserDAO implementation...");
		this.sessionFactory = sessionFactory;
	}
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public User addUser(User user) {
		this.getSession().save(user);
		return user;
	}

	@Override
	public User getUser(long idUser) {
		User user =  (User)this.getSession().get(User.class, idUser);
		if (user == null)
			user = new User();
		return user;
	}

	@Override
	public User getUser(String username) {
		User user = (User)this.getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
		if (user == null)
			user = new User();
		return user;
	}

	@Override
	public void deleteUser(long idUser) {
		Session s = this.getSession();
		deleteUser((User)s.get(User.class, idUser));
	}

	
	public void deleteUser(User u){
		Session s = this.getSession();
		@SuppressWarnings("unchecked")
		List<Relationship> rels = s.createCriteria(Relationship.class).add(Restrictions.or(Restrictions.eq("proposer", u), Restrictions.eq("contact", u))).list();
			for (Relationship relationship : rels) {
				s.delete(relationship);
			}
		s.delete(u);

	}

	@Override
	public boolean isUsernameAvailable(String username) {
		return this.getSession().createCriteria(User.class).add(Restrictions.eq("username", username))
				.uniqueResult()==null?true:false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAll() {
		return this.getSession().createCriteria(User.class).list();
	}

	@Override
	public User updateUser(User user) {
		this.getSession().update(user);
		return user;
	}
	
}
