package es.uvigo.esei.dgss.exercises.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class User {
	
	@Id
	private String login;
	private String role;
	private String name;
	private String password;
	@Size(max = 10000)
	private String picture;
	
	@OneToMany(mappedBy="user2")
	private Collection<Friendship> friend;
	
	User() { }
	
	public User(String login) {
		super();
		this.login = login;
	}

	public User(String login, String role, String name, String password, String picture) {
		super();
		this.login = login;
		this.role = role;
		this.name = name;
		this.password = password;
		this.picture = picture;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}