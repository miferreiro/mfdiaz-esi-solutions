package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

@Entity
public abstract class Post {

	@Id
	protected String id;
	
	protected Date date;
	
	@ManyToOne
	protected User user;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="postLikes",
    		   joinColumns=@JoinColumn(name="id"), 
    		   inverseJoinColumns=@JoinColumn(name="login")) 
	protected Set<User> usersLike = new HashSet<>();
	
	Post() {}

	public Post(String id, Date date, User user) {
		super();
		this.id = id;
		this.date = date;
		this.user = user;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
	public Set<User> getUsersLike() {
		return usersLike;
	}

	public void setUsersLike(Set<User> usersLike) {
		this.usersLike = usersLike;
	}

	public void addLikesPost(User user){       
        this.usersLike.add(user);
    }
}