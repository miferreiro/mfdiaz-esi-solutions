package es.uvigo.esei.dgss.exercise.service;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Lock(LockType.READ)
public class StatisticsEJB {

	@PersistenceContext
	private EntityManager em;
	
	private int numUsers;
	private int numPosts;
	
	@PostConstruct
	public void init() {
		this.numUsers = em.createQuery("SELECT u FROM User u").getResultList().size();
		this.numPosts = em.createQuery("SELECT p FROM Post p").getResultList().size();
	}
	
	public int getNumUsers() {
		return this.numUsers;
	}
	
	public int getNumPosts() {
		return this.numPosts;
	}
	
	@Lock(LockType.WRITE)
	public void addUser() {
		this.numUsers++;
	}
	
	@Lock(LockType.WRITE)
	public void removeUser() {
		this.numUsers--;
	}
	
	@Lock(LockType.WRITE)
	public void addPost() {
		this.numPosts++;
	}
	
	@Lock(LockType.WRITE)
	public void removePost() {
		this.numPosts--;
	}
}
