package es.uvigo.esei.dgss.exercises.web;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Friendship;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;

@Dependent
public class Facade {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    // 1. Create a new user given its login, name, password and picture
    public User addUser(String login, String role, String name, String password, String picture) {
    	
    	User user = new User(login);

    	user.setRole(role);
    	user.setName(name);
        user.setPassword(password);
        user.setPicture(picture);

        em.persist(user);

        return user;
    }
    
    // 2. Create a friendship between two given users
    public Friendship addFriendship(User user1, User user2, Date date, boolean statusRequest) {
    	
    	Friendship friendship = new Friendship(user1, user2, date, statusRequest);
    	
    	em.persist(friendship);
    	
    	return friendship;
    }
    
    // 3. Get all friends of a given user
    public List<User> getFriends(User user) {
    
    	final String query = "SELECT u "
				           + "FROM User u "
				           + "WHERE "
				           + "u.login IN (SELECT f.user2.login FROM Friendship f WHERE :userLogin = f.user1.login) OR "
				           + "u.login IN (SELECT f.user1.login FROM Friendship f WHERE :userLogin = f.user2.login)";
    	
    	List<User> users = em.createQuery(query, User.class)
    			.setParameter("userLogin", user.getLogin())
    			.getResultList() ;
      	
    	return users;
    }
    
    public Link addLink(String id, Date date, User user, String url) {
    	
    	Link link = new Link(id, date, user, url);
    	
    	em.persist(link);
    	
    	return link;
    }
    
    public Photo addPhoto(String id, Date date, User user, String content) {
    	
    	Photo photo = new Photo(id, date, user, content);
    	
    	em.persist(photo);
    	
    	return photo;
    }
    
    public Video addVideo(String id, Date date, User user, double duration) {
    	
    	Video video = new Video(id, date, user, duration);
    	
    	em.persist(video);
    	
    	return video;
    }

    // 4. Get all posts of the friends of a given user
    public List<Post> getPostFriends(User user) {
    	
    	final String query = "SELECT p "
				+ "FROM Post p, User u "
				+ "WHERE "
				+ "p.user = u.login AND "
	            + "(u.login IN (SELECT f.user2.login FROM Friendship f WHERE :userLogin = f.user1.login) OR "
	            + "u.login IN (SELECT f.user1.login FROM Friendship f WHERE :userLogin = f.user2.login))";
    	
    	List<Post> posts = em.createQuery(query, Post.class)
			.setParameter("userLogin", user.getLogin())
			.getResultList() ;
    	
    	return posts;
    }
    
    public Comment addComment(String id, String comment, Date date, User user, Post post) {
    	
    	Comment c = new Comment(id, comment, date, user, post);
    	
    	em.persist(c);
    	
    	return c;
    }
    
    // 5. Get the posts that have been commented by the friends of a given user after a given date
    public List<Post> getPostCommentedFriendsAfterDate(User user, Date date) {    	  
    	
    	final String query =  "SELECT DISTINCT p "
				+ "FROM Post p, User u, Comment c "
				+ "WHERE "
	            + "(u.login IN (SELECT f.user2.login "
	                         + "FROM Friendship f "
	                         + "WHERE :userLogin = f.user1.login) OR "
	                         
	            + "u.login IN (SELECT f.user1.login "
	                        + "FROM Friendship f "
	                        + "WHERE :userLogin = f.user2.login)) "
	                        
				+ "AND u.login = c.user.login "
				
				+ "AND p.id = c.post.id "
				
				+ "AND c.date > :date";
    	    	
    	List<Post> posts =  em.createQuery(query, Post.class)
    			.setParameter("userLogin", user.getLogin())
				.setParameter("date", date, TemporalType.TIMESTAMP)
				.getResultList();
    	
    	return posts;
    }
    
    public Post addLikePost(Post post, User user) {
    	
    	post.addLikesPost(user);    	
    	em.persist(post);
    	
    	return post;    	
    }
    
    // 6. Get the users which are friends of a given user who like a given post    
    public List<User> getFriendsUserPostLiked(User user, Post post) {
    	
    	final String query = "SELECT u "
                + "FROM User u, Post p JOIN p.usersLike l "
                + "WHERE "
	            + "(u.login IN (SELECT f.user2.login "
	                         + "FROM Friendship f "
	                         + "WHERE :userLogin = f.user1.login) OR "
	                         
	            + "u.login IN (SELECT f.user1.login "
	                        + "FROM Friendship f "
	                        + "WHERE :userLogin = f.user2.login)) "
	                        
                + "AND l.login = u.login "
                + "AND p.id = :postId";
    	
    	List<User> users = em.createQuery(query, User.class)
    			.setParameter("userLogin", user.getLogin())
    			.setParameter("postId", post.getId())
    			.getResultList();
    	
    	return users;
    }
    
    // 7. Give me all the pictures a given user likes
    public List<Photo> getPhotosLikedUser(User user) {
    	
    	final String query =  "SELECT ph "
							+ "FROM Post p JOIN p.usersLike l, Photo ph "
							+ "WHERE "
							+ "l.login = :userLogin "
							+ "AND p.id = ph.id";
    	
    	List<Photo> photos = em.createQuery(query, Photo.class)
    			.setParameter("userLogin", user.getLogin())
    			.getResultList();
    	
    	return photos;
    }

    // 8. Create a list of potential friends for a given user (feel free to create you own "algorithm")
    // People who have liked posts that the User has liked
    public List<User> getPotentialFriends(User user) {
    	
    	final String query =  "SELECT u "
			    			+ "FROM User u, Post p JOIN p.usersLike l "
			    			+ "WHERE "
			    			+ "u.login != :userLogin "
			    			+ "AND u.login = l.login "
			    			
			    			+ "AND p.id IN "
			    			+ "(SELECT p.id "
			    			+ "FROM Post p JOIN p.usersLike l "
			    			+ "WHERE "
			    			+ "l.login = :userLogin) "
			    			
			    			+ "AND u.login NOT IN "
			    			+ "(SELECT u.login "
			    			+ "FROM User u, Friendship f "
			    			+ "WHERE "
			    			+ "f.user2=u.login "
			    			+ "AND u.login!=:userLogin "
			    			+ "AND ((f.user2=:userLogin) OR (f.user1=:userLogin))) "
			    			;

    	List<User> users = em.createQuery(query, User.class)
    			.setParameter("userLogin", user.getLogin())
    			.getResultList();    	 
    	
    	return users;
    }
}