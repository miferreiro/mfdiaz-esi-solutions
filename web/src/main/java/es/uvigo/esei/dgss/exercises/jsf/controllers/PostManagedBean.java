package es.uvigo.esei.dgss.exercises.jsf.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import es.uvigo.esei.dgss.exercise.service.PostEJB;
import es.uvigo.esei.dgss.exercise.service.UserEJB;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;

@Named("post")
@SessionScoped
public class PostManagedBean implements Serializable {

	@Inject
	private UserEJB userService;
	
	@Inject
	private PostEJB postService;

	private String id;
	private Date date;
	private User user;	
	private String login;
	private float duration;
	private String content;
	private String URL;	
	
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	private List<Post> postList;
	
	public List<Post> getPostList() {
		return postList;
	}
	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
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
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	
	public boolean isLink(Post post) {
		return post instanceof Link;
	}
	public boolean isPhoto(Post post) {
		return post instanceof Photo;
	}
	public boolean isVideo(Post post) {
		return post instanceof Video;
	}
	
	public String posts(String login) {
		this.user = this.userService.findUserById(login); 
		
		this.login = login;
		
		this.postList =  this.postService.findPostByAuthorJSF(login);
		
		return this.redirectTo(this.getUserPostsViewId());		
	}	 
	public String getUserPostsViewId() {
		return "/posts/posts.xhtml";
	}	
	private String redirectTo(String url) {
		return url  + "?faces-redirect=true";
	}
}