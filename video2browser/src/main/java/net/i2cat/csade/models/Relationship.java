package net.i2cat.csade.models;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Relationship {
	public enum RelationshipStatus {ACCEPTED, REQUESTED, REJECTED, BLOCKED}
	
	@Id@GeneratedValue
	private long idRelationship;
	@OneToOne
	private User proposer;
	@OneToOne
	private User contact;
	private Calendar requestDate;
	private Calendar updateDate;
	private RelationshipStatus status; 
	
	public Relationship(){
		this.requestDate = Calendar.getInstance();
		this.status = RelationshipStatus.REQUESTED;
	}
	
	public long getIdRelationship() {
		return idRelationship;
	}
	public void setIdRelationship(long idRelationship) {
		this.idRelationship = idRelationship;
	}
	public User getProposer() {
		return proposer;
	}
	public void setProposer(User proposer) {
		this.proposer = proposer;
	}
	public User getContact() {
		return contact;
	}
	public void setContact(User contact) {
		this.contact = contact;
	}
	public Calendar getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Calendar requestDate) {
		this.requestDate = requestDate;
	}
	public Calendar getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}
	public RelationshipStatus getStatus() {
		return status;
	}
	public void setStatus(RelationshipStatus status) {
		this.status = status;
	}
	
}
