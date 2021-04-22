package es.uvigo.esei.dgss.exercise.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PostEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private UserEJB userEJB;
	
	@EJB
	private StatisticsEJB statisticsEJB;
	
	@Resource
	private SessionContext ctx; // a object who gives access to the logged user
	
	// Task 1 EJB
	public List<Post> findAllPosts() {
		String query = "SELECT p FROM Post p";
		return em.createQuery(query, Post.class).getResultList();
	}
	
	// Task 1 EJB
	public Post findPostById(String id) {
		return em.find(Post.class, id);
	}
	
	// Task 1 EJB
	public Link createLink(Link link) {
		em.persist(link);
		statisticsEJB.addPost();		
		return link;
	}
	
	// Task 1 EJB
	public Video createVideo(Video video) {
		em.persist(video);
		statisticsEJB.addPost();		
		return video;
	}
	
	// Task 1 EJB
	public Photo createPhoto(Photo photo) {		
		em.persist(photo);
		statisticsEJB.addPost();
		return photo;
	}
	
	// Task 1 EJB
	public void removePost(Post post) {
		statisticsEJB.removePost();
		em.remove(em.contains(post) ? post: em.merge(post));
	}
	
	// Task 1 EJB
	public Comment addComment(Comment comment) {
		return em.merge(comment);
	}
	
	// Task 1 EJB
	public Post updatePost(Post post) {
		return em.merge(post);
	}
	
	// Task 1 EJB
	public Post likePost(String login, String id) {
	    
	    Post post = this.findPostById(id);
	    User user = this.userEJB.findUserById(login);
	    
	    post.addLikesPost(user);

	    return em.merge(post);
	}
		
	// Exercise 3: JAX-RS
	public List<Post> findPostByAuthor(String login) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login)) {
	    	throw new SecurityException("Unable to get the post of " + login + " if you are not his author");
	    }
		
		String query = "SELECT p "
				     + "FROM Post p "
				     + "WHERE p.user.login = :login";
		
		return em.createQuery(query, Post.class)
				.setParameter("login", login)
				.getResultList();
	}
	
	// Exercise 3: JAX-RS
	public Post likePostSecurity(String login, String id) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login)) {
	    	throw new SecurityException("Unable to get the post of " + login + " if you are not his author");
	    }
	    
	    Post post = this.findPostById(id);
	    User user = this.userEJB.findUserById(login);
	    
	    post.addLikesPost(user);

	    return em.merge(post);
	}
	
	// Exercise 3: JAX-RS
	public Link createLinkSecurity(Link link, String login) {

	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login) && !link.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to create the post of " + login + " if you are not the author");
	    }
		
		em.persist(link);
		statisticsEJB.addPost();		
		return link;
	}
	
	// Exercise 3: JAX-RS
	public Video createVideoSecurity(Video video, String login) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login) && !video.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to create the post of " + login + " if you are not the author");
	    }
		
		em.persist(video);
		statisticsEJB.addPost();		
		return video;
	}
	
	// Exercise 3: JAX-RS
	public Photo createPhotoSecurity(Photo photo, String login) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login) && !photo.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to create the post of " + login + " if you are not the author");
	    }
		
		em.persist(photo);
		statisticsEJB.addPost();		
		return photo;
	}
		
	// Exercise 3: JAX-RS
	public Link updateLinkSecurity(Link link) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!link.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to update the link of " + loginLogged + " if you are not the author");
	    }
							
		return em.merge(link);
	}
	
	// Exercise 3: JAX-RS
	public Photo updatePhotoSecurity(Photo photo) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!photo.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to update the post of " + loginLogged + " if you are not the author");
	    }
							
		return em.merge(photo);
	}
	
	// Exercise 3: JAX-RS
	public Video updateVideoSecurity(Video video) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!video.getUser().getLogin().equals(loginLogged)) {
	    	throw new SecurityException("Unable to update the video of " + loginLogged + " if you are not the author");
	    }
							
		return em.merge(video);
	}
	
	// Exercise 4: JSB
	public List<Post> findPostByAuthorJSF(String login) {		
		
		String query = "SELECT p "
				     + "FROM Post p "
				     + "WHERE p.user.login = :login";
		
		return em.createQuery(query, Post.class)
				.setParameter("login", login)
				.getResultList();
	}
}
