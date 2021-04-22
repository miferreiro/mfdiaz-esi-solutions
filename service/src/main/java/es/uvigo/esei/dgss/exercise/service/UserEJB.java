package es.uvigo.esei.dgss.exercise.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.Friendship;
import es.uvigo.esei.dgss.exercises.domain.FriendshipId;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private StatisticsEJB statisticsEJB;
	
	@EJB
	private EmailServiceEJB emailServiceEJB;
	
	@Resource
	private SessionContext ctx; // a object who gives access to the logged user
	
	// Exercise 2: Task 1 EJB
	public List<User> findAllUsers() {		
		final String query = "SELECT u FROM User u";		
		return em.createQuery(query, User.class).getResultList();
	}
	
	// Exercise 2: Task 1 EJB
	public User findUserById(String login) {
		return em.find(User.class, login);
	}
	
	// Exercise 2: Task 1 EJB and Exercise 3: JAX-RS
	public User createUser(User user) {
		em.persist(user);
		statisticsEJB.addUser();
		return user;
	}
	
	// Exercise 2: Task 1 EJB
	public User updateUser(User user) {
		return em.merge(user);
	}
	
	// Exercise 2: Task 1 EJB
	public void removeUser(User user) {
		statisticsEJB.removeUser();
		em.remove(em.contains(user) ? user: em.merge(user));
	}
	
	// Exercise 2: Task 1 EJB
	public Friendship findFriendshipsByIds(String login1, String login2) {
		return em.find(Friendship.class, new FriendshipId(login1, login2));
	}

	// Exercise 2: Task 1 EJB
	public Friendship addFriendship(Friendship friendship) {	
		
		final String query = "SELECT f "
				   + "FROM Friendship f "
				   + "WHERE (f.user1.login = :login1 AND f.user2.login = :login2) OR "
				   + "(f.user1.login = :login2 AND f.user2.login = :login1)";		
		
		List<Friendship> friendshipList = em.createQuery(query, Friendship.class)
				.setParameter("login1", friendship.getUser1().getLogin())
				.setParameter("login2", friendship.getUser2().getLogin())
				.getResultList();
	    
		if (friendshipList.size() != 0) {
			throw new EntityExistsException("The friendship already exists");
		}
		
		em.persist(friendship);
		return friendship;
	}
	
	// Exercise 2: Task 1 and 3 EJB
	public Post addLikePost(Post post, User user) {
				
		post.addLikesPost(findUserById(user.getLogin()));
		post = em.merge(post);
		
		emailServiceEJB.sendEmail(post.getUser(), "sendingEmail" , "The user " + user.getLogin() + " liked the post " + post.getId());
		
		return post;
	}
	
	// Exercise 3: JAX-RS
	public List<Friendship> listFriendshipRequests(String login) {		
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login)) {
	    	throw new SecurityException("Unable to get the friends of " + login + " if you are not " + login);
	    }
		
		final String query = "SELECT f "
						   + "FROM Friendship f "
						   + "WHERE (f.user1.login = :login OR f.user2.login = :login) AND statusRequest = false";		
		
		return em.createQuery(query, Friendship.class)
				.setParameter("login", login)
				.getResultList();		
	}
	
	// Exercise 3: JAX-RS
	public Friendship requestFriendship(String login1, String login2, Date date) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login2)) {
	    	throw new SecurityException("Unable to accept friendships if you are not " + login2);
	    }
	    
		User user1 = this.findUserById(login1);
		User user2 = this.findUserById(login2);
		Friendship friendship = new Friendship(user1, user2, date, false);
		
		final String query = "SELECT f "
				   + "FROM Friendship f "
				   + "WHERE (f.user1.login = :login1 AND f.user2.login = :login2) OR "
				   + "(f.user1.login = :login2 AND f.user2.login = :login1)";		
		
		List<Friendship> friendshipList = em.createQuery(query, Friendship.class)
				.setParameter("login1", friendship.getUser1().getLogin())
				.setParameter("login2", friendship.getUser2().getLogin())
				.getResultList();
	    
		if (friendshipList.size() != 0) {
			throw new EntityExistsException("The friendship already exists");
		}
		
		em.persist(friendship);
		
		return friendship;
	}
	
	// Exercise 3: JAX-RS
	public Friendship acceptFriendship(String login1, String login2, boolean statusRequest) {
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login1)) {
	    	throw new SecurityException("Unable to accept friendships if you are not " + login1);
	    }
	    
		Friendship friendship = this.findFriendshipsByIds(login1, login2);
		friendship.setStatusRequest(statusRequest);
		em.merge(friendship);
		
		return friendship;		
	}
	
	// Exercise 3: JAX-RS
	public List<Post> getWallPosts(String login) {
		
	    String loginLogged = ctx.getCallerPrincipal().getName(); // accessing the logged user

	    if (!ctx.isCallerInRole("user") && !ctx.isCallerInRole("admin")) {
	      throw new SecurityException("Insufficient privileges");
	    }
	    
	    if (!loginLogged.equals(login)) {
	    	throw new SecurityException("Unable to get the friends of " + login + " if you are not " + login);
	    }
		
		final String query = "SELECT DISTINCT p "
                           + "FROM Post p, User u "
				           + "WHERE "
				           + "((u.login IN (SELECT f.user2.login FROM Friendship f WHERE :login = f.user1.login) "
	                       + "OR u.login IN (SELECT f.user1.login FROM Friendship f WHERE :login = f.user2.login)) "
				           + "AND u.login = p.user.login ) "				
				           + "OR p.user.login = :login";
		
		return em.createQuery(query, Post.class)
				.setParameter("login", login)
				.getResultList();	
	}	
	
	// Exercise 4: JSF
	public List<User> findAllUsersLikeLogin(String login) {
		final String query = "SELECT u FROM User u WHERE u.login LIKE :login";		
		return em.createQuery(query, User.class)
				.setParameter("login", "%" + login + "%")
				.getResultList();
	}
}
