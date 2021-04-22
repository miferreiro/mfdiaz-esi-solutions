package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(FriendshipId.class)
public class Friendship {
	
	@Id
	@ManyToOne
	private User user1;
	@Id
	@ManyToOne
	private User user2;
	
	private Date date;
	
	private Boolean statusRequest;
	
	Friendship() {}
	
	public Friendship(User user1, User user2, Date date, Boolean statusRequest) {
		super();
		this.user1 = user1;
		this.user2 = user2;
		this.date = date;
		this.statusRequest = statusRequest;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean isStatusRequest() {
		return statusRequest;
	}

	public void setStatusRequest(Boolean statusRequest) {
		this.statusRequest = statusRequest;
	}	
}