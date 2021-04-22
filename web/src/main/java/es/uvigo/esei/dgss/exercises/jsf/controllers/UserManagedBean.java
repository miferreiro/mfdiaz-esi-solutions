package es.uvigo.esei.dgss.exercises.jsf.controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import es.uvigo.esei.dgss.exercise.service.UserEJB;
import es.uvigo.esei.dgss.exercises.domain.User;

@Named("user")
@SessionScoped
public class UserManagedBean implements Serializable {

	@Inject
	private UserEJB userService;
	
	private String login;
	private String role;
	private String name;
	private String picture;
	private String searchString;
	
	private List<User> users;
	
	public void initUsers() {
		this.users = this.userService.findAllUsers();
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
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}		
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public List<User> getUsers() {
		return this.users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String searchByUser(String login) {		
		 this.users = this.userService.findAllUsersLikeLogin(login);
		 return this.redirectTo(this.getUserUsersViewId());
	}	
	
	public String getUserUsersViewId() {
		return "/users/users.xhtml";
	}	
	private String redirectTo(String url) {
		return url  + "?faces-redirect=true";
	}
}
